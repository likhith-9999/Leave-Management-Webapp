package com.wavemaker.leavemanagement.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wavemaker.leavemanagement.exception.APICallException;
import com.wavemaker.leavemanagement.model.Holiday;
import com.wavemaker.leavemanagement.model.LeaveTypeDTO;
import com.wavemaker.leavemanagement.service.HolidayService;
import com.wavemaker.leavemanagement.service.LeaveTypeService;
import com.wavemaker.leavemanagement.service.impl.HolidayServiceImpl;
import com.wavemaker.leavemanagement.service.impl.LeaveTypeServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

@WebServlet("/leaveType")
public class LeaveTypeServlet extends HttpServlet {
    private static final Logger LOGGER = LoggerFactory.getLogger(LeaveTypeServlet.class);
    private final LeaveTypeService leaveTypeService = new LeaveTypeServiceImpl();
    private final HolidayService holidayService = new HolidayServiceImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        String emailID = (String) req.getAttribute("emailId");
        String action = req.getParameter("action");

        try {
            if (Objects.equals(action, "allLeavesType")) {
                LOGGER.debug("Fetching all leave types");
                String requestedMailId = req.getParameter("mail");
                LOGGER.debug("Requested mail ID: {}", requestedMailId);

                List<LeaveTypeDTO> leaveTypeDTOList;
                if (requestedMailId != null) {
                    leaveTypeDTOList = leaveTypeService.getAllLeavesTakenByEmployee(requestedMailId);
                } else {
                    leaveTypeDTOList = leaveTypeService.getAllLeavesTakenByEmployee(emailID);
                }

                ObjectMapper objectMapper = new ObjectMapper();
                String jsonResponse = objectMapper.writeValueAsString(leaveTypeDTOList);
                LOGGER.info("All leave types: {}", jsonResponse);
                resp.getWriter().write(jsonResponse);

            } else if (Objects.equals(action, "allTeamLeavesType")) {
                LOGGER.debug("Fetching all team leave types");
                List<LeaveTypeDTO> leaveTypeDTOList = leaveTypeService.getAllLeavesTakenByTeam(emailID);
                ObjectMapper objectMapper = new ObjectMapper();
                String jsonResponse = objectMapper.writeValueAsString(leaveTypeDTOList);
                LOGGER.info("All team leave types: {}", jsonResponse);
                resp.getWriter().write(jsonResponse);

            } else if (Objects.equals(action, "allHolidays")) {
                LOGGER.debug("Fetching all holidays");
                List<Holiday> holidayList = holidayService.getAllHolidaysService();
                ObjectMapper objectMapper = new ObjectMapper();
                String jsonResponse = objectMapper.writeValueAsString(holidayList);
                LOGGER.info("All holidays: {}", jsonResponse);
                resp.getWriter().write(jsonResponse);

            } else {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                String errorMessage = "{\"message\" : \"Invalid API call\"}";
                resp.getWriter().write(errorMessage);
                LOGGER.warn("Invalid API call: {}", action);
                throw new APICallException("invalid api call error");
            }
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            String errorMessage = "{\"message\" : \"An error occurred\"}";
            resp.getWriter().write(errorMessage);
            LOGGER.error("Error processing request", e);
        }
    }
}
