package com.wavemaker.leavemanagement.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wavemaker.leavemanagement.exception.APICallException;
import com.wavemaker.leavemanagement.model.Employee;
import com.wavemaker.leavemanagement.service.EmployeeService;
import com.wavemaker.leavemanagement.service.impl.EmployeeServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

@WebServlet("/employee")
public class EmployeeServlet extends HttpServlet {
    private static final Logger LOGGER = LoggerFactory.getLogger(EmployeeServlet.class);
    private final EmployeeService employeeService = new EmployeeServiceImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        String emailID = (String) req.getAttribute("emailId");
        String isManager = (String) req.getAttribute("isManager");
        String action = req.getParameter("action");

        try {
            if (Objects.equals(action, "employeeDetails")) {
                LOGGER.info("Fetching employee details for email ID: {}", emailID);
                Employee employee = employeeService.getEmployeeByMail(emailID);
                ObjectMapper objectMapper = new ObjectMapper();
                String jsonResponse = objectMapper.writeValueAsString(employee);
                resp.getWriter().write(jsonResponse);
                LOGGER.info("Employee details response: {}", jsonResponse);
            } else if (Objects.equals(action, "isManager")) {
                String jsonResponse = String.format("{\"message\" : \"%s\"}", isManager);
                resp.getWriter().write(jsonResponse);
                LOGGER.info("Manager check response: {}", isManager);
            } else {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                String errorMessage = "{\"message\" : \"Invalid API call\"}";
                resp.getWriter().write(errorMessage);
                LOGGER.warn("Invalid API call: {}", action);
                throw new APICallException("Unexpected Error invalid api call");
            }
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            String errorMessage = "{\"message\" : \"An error occurred\"}";
            resp.getWriter().write(errorMessage);
            LOGGER.error("Error processing request", e);
        }
    }
}
