package com.wavemaker.leavemanagement.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wavemaker.leavemanagement.exception.APICallException;
import com.wavemaker.leavemanagement.model.Employee;
import com.wavemaker.leavemanagement.model.LeaveDTO;
import com.wavemaker.leavemanagement.service.MyTeamLeavesService;
import com.wavemaker.leavemanagement.service.impl.MyTeamLeavesServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

@WebServlet("/myTeamLeaves")
public class MyTeamLeavesServlet extends HttpServlet {
    private static final Logger LOGGER = LoggerFactory.getLogger(MyTeamLeavesServlet.class);
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private final MyTeamLeavesService myTeamLeavesService = new MyTeamLeavesServiceImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        String emailID = (String) req.getAttribute("emailId");
        String action = req.getParameter("action");

        try {
            List<LeaveDTO> leaveDTOS;
            switch (action) {
                case "Pending":
                    LOGGER.info("Retrieving pending leave requests");
                    leaveDTOS = myTeamLeavesService.getMyTeamLeaveRequestsByStatusService(emailID, "Pending");
                    break;
                case "Rejected":
                    LOGGER.info("Retrieving rejected leave requests");
                    leaveDTOS = myTeamLeavesService.getMyTeamLeaveRequestsByStatusService(emailID, "Rejected");
                    break;
                case "Approved":
                    LOGGER.info("Retrieving approved leave requests");
                    leaveDTOS = myTeamLeavesService.getMyTeamLeaveRequestsByStatusService(emailID, "Approved");
                    break;
                case "All":
                    LOGGER.info("Retrieving all team leaves");
                    leaveDTOS = myTeamLeavesService.getMyTeamLeaves(emailID);
                    break;
                case "teamDetails":
                    LOGGER.info("Retrieving all team leaves");
                    List<Employee> teamDetails = myTeamLeavesService.getTeamDetails(emailID);
                    String jsonResponse = OBJECT_MAPPER.writeValueAsString(teamDetails);
                    resp.setStatus(HttpServletResponse.SC_OK);
                    resp.getWriter().write(jsonResponse);
                    return;
                default:
                    LOGGER.warn("Invalid action parameter: {}", action);
                    resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    resp.getWriter().write("{\"message\": \"Invalid action\"}");
                    throw new APICallException("invalid action on api");
//                    return;
            }

            String jsonResponse = OBJECT_MAPPER.writeValueAsString(leaveDTOS);
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.getWriter().write(jsonResponse);
            LOGGER.info("Successfully retrieved leave requests: {}", jsonResponse);
        } catch (Exception e) {
            LOGGER.error("Error processing GET request: {}", e.getMessage());
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write("{\"message\": \"Error processing request\"}");
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setCharacterEncoding("UTF-8");
        resp.setContentType("application/json");
        LOGGER.info("Processing leave status update");

        String isManager = (String) req.getAttribute("isManager");

        if (!"true".equals(isManager)) {
            LOGGER.warn("Unauthorized access attempt by user who is not a manager");
            resp.setStatus(HttpServletResponse.SC_FORBIDDEN);
            resp.getWriter().write("{\"message\": \"Not authorized\"}");
            return;
        }

        try {
            JsonNode jsonNode = OBJECT_MAPPER.readTree(req.getInputStream());
            String leaveId = jsonNode.get("leaveId").asText();
            String leaveStatus = jsonNode.get("leaveStatus").asText();

            if (leaveId == null || leaveStatus == null) {
                LOGGER.warn("Missing leaveId or leaveStatus in PUT request");
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                resp.getWriter().write("{\"message\": \"Missing leaveId or leaveStatus\"}");
                return;
            }

            myTeamLeavesService.changeLeaveStatusService(Integer.parseInt(leaveId), leaveStatus);
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.getWriter().write("{\"message\": \"Leave status updated successfully\"}");
            LOGGER.info("Leave status updated for leaveId: {}", leaveId);
        } catch (NumberFormatException e) {
            LOGGER.error("Invalid leaveId format: {}", e.getMessage());
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write("{\"message\": \"Invalid leaveId format\"}");
        } catch (Exception e) {
            LOGGER.error("Error updating leave status: {}", e.getMessage());
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write("{\"message\": \"Error updating leave status\"}");
        }
    }
}
