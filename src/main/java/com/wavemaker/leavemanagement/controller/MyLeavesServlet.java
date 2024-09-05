package com.wavemaker.leavemanagement.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wavemaker.leavemanagement.exception.APICallException;
import com.wavemaker.leavemanagement.model.Employee;
import com.wavemaker.leavemanagement.model.LeaveRequest;
import com.wavemaker.leavemanagement.service.EmployeeService;
import com.wavemaker.leavemanagement.service.LeaveRequestService;
import com.wavemaker.leavemanagement.service.impl.EmployeeServiceImpl;
import com.wavemaker.leavemanagement.service.impl.LeaveRequestServiceImpl;
import com.wavemaker.leavemanagement.util.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@WebServlet("/myLeaves")
public class MyLeavesServlet extends HttpServlet {
    private static final Logger LOGGER = LoggerFactory.getLogger(MyLeavesServlet.class);
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private final LeaveRequestService leaveRequestService = new LeaveRequestServiceImpl();
    private final EmployeeService employeeService = new EmployeeServiceImpl();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        String emailID = (String) req.getAttribute("emailId");
        LOGGER.debug("Processing leave request for emailID: {}", emailID);

        Employee employee;
        try {
            employee = employeeService.getEmployeeByMail(emailID);
            if (employee == null) {
                LOGGER.warn("Employee not found for emailID: {}", emailID);
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                resp.getWriter().write("{\"message\": \"Employee not found\"}");
                return;
            }
        } catch (Exception e) {
            LOGGER.error("Error retrieving employee: {}", e.getMessage());
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write("{\"message\": \"Error retrieving employee\"}");
            return;
        }

        JsonNode jsonNode;
        try {
            jsonNode = OBJECT_MAPPER.readTree(req.getInputStream());
        } catch (IOException e) {
            LOGGER.error("Error parsing JSON input: {}", e.getMessage());
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write("{\"message\": \"Invalid JSON input\"}");
            return;
        }

        String fromDate = jsonNode.get("fromDate").asText();
        String toDate = jsonNode.get("toDate").asText();
        String leaveType = jsonNode.get("leaveType").asText();
        String leaveReason = jsonNode.get("leaveReason").asText();
        String leaveStatus = "Pending";

        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate localFromDate;
        LocalDate localToDate;
        int daysCount;
        try {
            localFromDate = LocalDate.parse(fromDate, dateFormatter);
            localToDate = LocalDate.parse(toDate, dateFormatter);
            if (localFromDate.isAfter(localToDate)) {
                LOGGER.warn("Invalid date range: fromDate is after toDate");
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                resp.getWriter().write("{\"message\": \"Invalid date range\"}");
                return;
            }
            daysCount = DateUtils.countWeekdaysExcludingHolidays(localFromDate, localToDate);
            LOGGER.info("Number of days leave applied: {}", daysCount);
        } catch (Exception e) {
            LOGGER.error("Error processing dates: {}", e.getMessage());
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write("{\"message\": \"Error processing dates\"}");
            return;
        }

        LeaveRequest leaveRequest = new LeaveRequest(0, employee.getEmployeeId(), fromDate, toDate, leaveType, leaveReason, leaveStatus, daysCount);
        try {
            leaveRequestService.applyLeaveService(leaveRequest);
            resp.setStatus(HttpServletResponse.SC_OK);
//            resp.getWriter().write("{\"status\": \"ok\", \"message\": \"Leave applied\"}");
            LOGGER.info("Leave request submitted successfully");
            resp.getWriter().write(String.format("{\"status\": \"ok\", \"message\": \"%s applied for %d days\"}", leaveType, daysCount));
        } catch (Exception e) {
            LOGGER.error("Error applying leave: {}", e.getMessage());
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write("{\"message\": \"Error applying leave\"}");
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        String emailID = (String) req.getAttribute("emailId");
        String action = req.getParameter("action");

        try {
            List<LeaveRequest> leaveRequests;
            switch (action) {
                case "myLeaveRequests":
                    LOGGER.debug("Retrieving all leave requests for emailID: {}", emailID);
                    leaveRequests = leaveRequestService.getMyLeaveRequestService(emailID);
                    break;
                case "myPendingLeaveRequests":
                    LOGGER.debug("Retrieving pending leave requests for emailID: {}", emailID);
                    leaveRequests = leaveRequestService.getMyLeaveRequestByStatusService(emailID, "Pending");
                    break;
                case "myRejectedLeaveRequests":
                    LOGGER.debug("Retrieving rejected leave requests for emailID: {}", emailID);
                    leaveRequests = leaveRequestService.getMyLeaveRequestByStatusService(emailID, "Rejected");
                    break;
                case "myApprovedLeaveRequests":
                    LOGGER.debug("Retrieving approved leave requests for emailID: {}", emailID);
                    leaveRequests = leaveRequestService.getMyLeaveRequestByStatusService(emailID, "Approved");
                    break;
                default:
                    LOGGER.warn("Unknown action: {}", action);
                    resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    resp.getWriter().write("{\"message\": \"Invalid action\"}");
                    throw new APICallException("invalid action on api");
//                    return;
            }
            String jsonResponse = OBJECT_MAPPER.writeValueAsString(leaveRequests);
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.getWriter().write(jsonResponse);
            LOGGER.info("Retrieved leave requests: {}", jsonResponse);
        } catch (Exception e) {
            LOGGER.error("Error retrieving leave requests: {}", e.getMessage());
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write("{\"message\": \"Error retrieving leave requests\"}");
        }
    }
}
