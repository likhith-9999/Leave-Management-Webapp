package com.wavemaker.leavemanagement.repository.impl;

import com.wavemaker.leavemanagement.model.Employee;
import com.wavemaker.leavemanagement.model.LeaveDTO;
import com.wavemaker.leavemanagement.repository.MyTeamLeavesRepository;
import com.wavemaker.leavemanagement.util.DataBaseConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class MyTeamLeavesRepositoryImpl implements MyTeamLeavesRepository {

    private static final Logger LOGGER = LoggerFactory.getLogger(MyTeamLeavesRepositoryImpl.class);
    private final static String MY_TEAM_LEAVE_REQUESTS_BY_STATUS_QUERY = "SELECT L.LEAVE_ID, L.EMPLOYEE_ID, "
            + "E.EMPLOYEE_NAME, E.EMAIL_ID, L.LEAVE_FROM, L.LEAVE_TO, L.LEAVE_TYPE, L.LEAVE_REASON, " +
            "L.LEAVE_STATUS, " + "L.DAYS_TAKEN FROM LEAVES L JOIN EMPLOYEES E ON L.EMPLOYEE_ID = " +
            "E.EMPLOYEE_ID WHERE L.LEAVE_STATUS = ? " + "AND E.EMPLOYEE_ID IN (SELECT EMPLOYEE_ID FROM " +
            "EMPLOYEES WHERE MANAGER_ID = (SELECT EMPLOYEE_ID FROM " + "EMPLOYEES WHERE EMAIL_ID = ?)) " +
            "ORDER BY L.LEAVE_ID DESC;";
    private final static String MY_TEAM_LEAVES_QUERY = "SELECT L.LEAVE_ID, L.EMPLOYEE_ID, E.EMPLOYEE_NAME, " +
            "E.EMAIL_ID, " + "L.LEAVE_FROM, L.LEAVE_TO, L.LEAVE_TYPE, L.LEAVE_REASON, L.LEAVE_STATUS, " +
            "L.DAYS_TAKEN FROM LEAVES L JOIN " + "EMPLOYEES E ON L.EMPLOYEE_ID = E.EMPLOYEE_ID " +
            "WHERE L.EMPLOYEE_ID IN (SELECT EMPLOYEE_ID FROM " + "EMPLOYEES WHERE MANAGER_ID = " +
            "(SELECT EMPLOYEE_ID FROM EMPLOYEES WHERE EMAIL_ID = ?)) " + "ORDER BY L.LEAVE_ID DESC;\n";
    private static final String CHANGE_LEAVE_STATUS_QUERY = "UPDATE LEAVES SET LEAVE_STATUS = ? " +
            "WHERE LEAVE_ID = ?";
    private static final String TEAM_DETAILS_QUERY = "SELECT E.EMPLOYEE_ID, E.EMPLOYEE_NAME, " +
            "E.EMPLOYEE_PHONE, " + "E.EMPLOYEE_DOB, E.EMAIL_ID, E.MANAGER_ID, E.GENDER FROM EMPLOYEES E " +
            "JOIN EMPLOYEES M ON E.MANAGER_ID = " + "M.EMPLOYEE_ID WHERE M.EMAIL_ID = ?;";
    private static MyTeamLeavesRepositoryImpl singleInstance;

    private MyTeamLeavesRepositoryImpl() {

    }

    public static synchronized MyTeamLeavesRepositoryImpl getInstance() {
        if (singleInstance == null) {
            singleInstance = new MyTeamLeavesRepositoryImpl();
        }
        return singleInstance;
    }

    @Override
    public void changeLeaveStatus(int leaveID, String leaveStatus) throws SQLException, ClassNotFoundException {
        Connection connection = DataBaseConnection.connect();
        LOGGER.info("Changing leave status for leave ID {} to {}", leaveID, leaveStatus);

        PreparedStatement preparedStatement = connection.prepareStatement(CHANGE_LEAVE_STATUS_QUERY);
        preparedStatement.setString(1, leaveStatus);
        preparedStatement.setInt(2, leaveID);

        preparedStatement.executeUpdate();
        LOGGER.info("leave status changed");
    }

    @Override
    public List<LeaveDTO> getMyTeamLeaveRequestsByStatus(String managerMailId, String leaveStatus) throws
            SQLException, ClassNotFoundException {
        LOGGER.info("Fetching leave requests by status {} for manager {}", leaveStatus, managerMailId);
        List<LeaveDTO> pendingApprovalsForManager = new ArrayList<>();

        Connection connection = DataBaseConnection.connect();


        PreparedStatement preparedStatement = connection.prepareStatement(MY_TEAM_LEAVE_REQUESTS_BY_STATUS_QUERY);

        preparedStatement.setString(1, leaveStatus);
        preparedStatement.setString(2, managerMailId);

        ResultSet resultSet = preparedStatement.executeQuery();

        while (resultSet.next()) {
            int leaveId = resultSet.getInt("LEAVE_ID");
            int employeeId = resultSet.getInt("EMPLOYEE_ID");
            String employeeName = resultSet.getString("EMPLOYEE_NAME");
            String employeeMailId = resultSet.getString("EMAIL_ID");
            Date sqlFromDate = resultSet.getDate("LEAVE_FROM");
            Date sqlToDate = resultSet.getDate("LEAVE_TO");
            String leaveType = resultSet.getString("LEAVE_TYPE");
            String leaveReason = resultSet.getString("LEAVE_REASON");
            leaveStatus = resultSet.getString("LEAVE_STATUS");
            int daysCount = resultSet.getInt("DAYS_TAKEN");


            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

            LocalDate localFromDate = sqlFromDate.toLocalDate();
            String fromDate = localFromDate.format(dateFormatter);
            LocalDate localToDate = sqlToDate.toLocalDate();
            String toDate = localToDate.format(dateFormatter);

            LeaveDTO leaveDTO = new LeaveDTO(leaveId, employeeId, fromDate, toDate, leaveType, leaveReason,
                    leaveStatus, daysCount, employeeName, employeeMailId);

            pendingApprovalsForManager.add(leaveDTO);
        }

        return pendingApprovalsForManager;

    }

    @Override
    public List<LeaveDTO> getMyTeamLeaves(String managerMailId) throws SQLException, ClassNotFoundException {
        LOGGER.info("Fetching all leaves for manager {}", managerMailId);
        List<LeaveDTO> pendingApprovalsForManager = new ArrayList<>();

        Connection connection = DataBaseConnection.connect();

        PreparedStatement preparedStatement = connection.prepareStatement(MY_TEAM_LEAVES_QUERY);
        preparedStatement.setString(1, managerMailId);
        ResultSet resultSet = preparedStatement.executeQuery();

        while (resultSet.next()) {
            int leaveId = resultSet.getInt("LEAVE_ID");
            int employeeId = resultSet.getInt("EMPLOYEE_ID");
            String employeeName = resultSet.getString("EMPLOYEE_NAME");
            String employeeMailId = resultSet.getString("EMAIL_ID");
            Date sqlFromDate = resultSet.getDate("LEAVE_FROM");
            Date sqlToDate = resultSet.getDate("LEAVE_TO");
            String leaveType = resultSet.getString("LEAVE_TYPE");
            String leaveReason = resultSet.getString("LEAVE_REASON");
            String leaveStatus = resultSet.getString("LEAVE_STATUS");
            int daysCount = resultSet.getInt("DAYS_TAKEN");

            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

            LocalDate localFromDate = sqlFromDate.toLocalDate();
            String fromDate = localFromDate.format(dateFormatter);
            LocalDate localToDate = sqlToDate.toLocalDate();
            String toDate = localToDate.format(dateFormatter);

            LeaveDTO leaveDTO = new LeaveDTO(leaveId, employeeId, fromDate, toDate, leaveType, leaveReason,
                    leaveStatus, daysCount, employeeName, employeeMailId);

            pendingApprovalsForManager.add(leaveDTO);
        }

        return pendingApprovalsForManager;

    }

    @Override
    public List<Employee> getMyTeamDetails(String managerMailId) throws SQLException, ClassNotFoundException {
        LOGGER.info("Fetching team details for manager {}", managerMailId);
        List<Employee> teamDetails = new ArrayList<>();
        Connection connection = DataBaseConnection.connect();
        PreparedStatement preparedStatement = connection.prepareStatement(TEAM_DETAILS_QUERY);
        preparedStatement.setString(1, managerMailId);

        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()) {
            int employeeId = resultSet.getInt("EMPLOYEE_ID");
            String employeeName = resultSet.getString("EMPLOYEE_NAME");
            String employeeMailId = resultSet.getString("EMAIL_ID");
            String employeePhone = resultSet.getString("EMPLOYEE_PHONE");
            int managerId = resultSet.getInt("MANAGER_ID");
            String gender = resultSet.getString("GENDER");
            Date sqlDOB = resultSet.getDate("EMPLOYEE_DOB");

            LocalDate localDOB = sqlDOB.toLocalDate();
            DateTimeFormatter fromDateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            String employeeDOB = localDOB.format(fromDateFormatter);


            Employee employee = new Employee(employeeId, employeeName, employeePhone, employeeDOB,
                    employeeMailId, managerId, gender);
            teamDetails.add(employee);
        }
        return teamDetails;

    }


}
