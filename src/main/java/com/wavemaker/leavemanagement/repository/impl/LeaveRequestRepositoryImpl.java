package com.wavemaker.leavemanagement.repository.impl;

import com.wavemaker.leavemanagement.model.LeaveRequest;
import com.wavemaker.leavemanagement.repository.LeaveRequestRepository;
import com.wavemaker.leavemanagement.util.DataBaseConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class LeaveRequestRepositoryImpl implements LeaveRequestRepository {

    private static final String APPLY_LEAVE_QUERY = "INSERT INTO LEAVES (EMPLOYEE_ID, LEAVE_FROM, LEAVE_TO,"
            + " LEAVE_TYPE, " + "LEAVE_REASON, LEAVE_STATUS, DAYS_TAKEN) " + "VALUES (?,?,?,?,?,?,?)";
    private final static String MY_LEAVE_REQUEST_QUERY = "SELECT \n" + "    L.LEAVE_ID, \n"
            + "    L.EMPLOYEE_ID, \n" + "    L.LEAVE_FROM, \n" + "    L.LEAVE_TO, \n" + "    L.LEAVE_TYPE, \n"
            + "    L.LEAVE_REASON, \n" + "    L.LEAVE_STATUS, L.DAYS_TAKEN\n" + "FROM \n" + "    LEAVES L\n"
            + "JOIN \n" + "    EMPLOYEES E ON L.EMPLOYEE_ID = E.EMPLOYEE_ID\n" + "WHERE \n"
            + "    L.EMPLOYEE_ID = ( \n" + "        SELECT EMPLOYEE_ID \n" + "        FROM EMPLOYEES \n"
            + "        WHERE EMAIL_ID = ?\n" + "    ) ORDER BY L.LEAVE_ID DESC;\n";
    private final static String MY_LEAVE_REQUEST_BY_STATUS_QUERY = "SELECT \n" + "    L.LEAVE_ID, \n"
            + "    L.EMPLOYEE_ID, \n" + "    L.LEAVE_FROM, \n" + "    L.LEAVE_TO, \n" + "    L.LEAVE_TYPE, \n"
            + "    L.LEAVE_REASON, \n" + "    L.LEAVE_STATUS, L.DAYS_TAKEN\n" + "FROM \n" + "    LEAVES L\n"
            + "JOIN \n" + "    EMPLOYEES E ON L.EMPLOYEE_ID = E.EMPLOYEE_ID\n" + "WHERE \n"
            + "    L.EMPLOYEE_ID = ( \n" + "        SELECT EMPLOYEE_ID \n" + "        FROM EMPLOYEES \n"
            + "        WHERE EMAIL_ID = ?\n" + "    )\n" + "AND \n" + "    L.LEAVE_STATUS = ?  " +
            "ORDER BY L.LEAVE_ID DESC;\n";
    private final static Logger LOGGER = LoggerFactory.getLogger(LeaveRequestRepositoryImpl.class);
    private static LeaveRequestRepositoryImpl singleInstance;

    private LeaveRequestRepositoryImpl() {

    }

    public static synchronized LeaveRequestRepositoryImpl getInstance() {
        if (singleInstance == null) {
            singleInstance = new LeaveRequestRepositoryImpl();
        }
        return singleInstance;
    }

    @Override
    public void applyLeave(LeaveRequest leaveRequest) throws SQLException, ClassNotFoundException {
        Connection connection = DataBaseConnection.connect();
        LOGGER.info("Applying leave request for employee ID: {}", leaveRequest.getEmployeeID());

        PreparedStatement preparedStatement = connection.prepareStatement(APPLY_LEAVE_QUERY);

        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate fromDate = LocalDate.parse(leaveRequest.getFromDate(), dateFormatter);
        LocalDate toDate = LocalDate.parse(leaveRequest.getToDate(), dateFormatter);


        preparedStatement.setInt(1, leaveRequest.getEmployeeID());
        preparedStatement.setDate(2, Date.valueOf(fromDate));
        preparedStatement.setDate(3, Date.valueOf(toDate));
        preparedStatement.setString(4, leaveRequest.getLeaveType());
        preparedStatement.setString(5, leaveRequest.getLeaveReason());
        preparedStatement.setString(6, leaveRequest.getLeaveStatus());
        preparedStatement.setInt(7, leaveRequest.getDaysCount());
//        status = pending

        preparedStatement.executeUpdate();

        LOGGER.info("leave added to leaves table");
    }

    @Override
    public List<LeaveRequest> getMyLeaveRequests(String employeeMailId) throws SQLException, ClassNotFoundException {
        LOGGER.info("Fetching leave requests for employee email: {}", employeeMailId);
        List<LeaveRequest> myLeaveRequests = new ArrayList<>();
        Connection connection = DataBaseConnection.connect();

        PreparedStatement preparedStatement = connection.prepareStatement(MY_LEAVE_REQUEST_QUERY);

        preparedStatement.setString(1, employeeMailId);

        ResultSet resultSet = preparedStatement.executeQuery();

        while (resultSet.next()) {
            int leaveId = resultSet.getInt("LEAVE_ID");
            int employeeId = resultSet.getInt("EMPLOYEE_ID");
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

            LeaveRequest leaveRequest = new LeaveRequest(leaveId, employeeId, fromDate, toDate, leaveType,
                    leaveReason, leaveStatus, daysCount);
            myLeaveRequests.add(leaveRequest);
        }
        LOGGER.info("Fetched {} leave requests for employee email: {}", myLeaveRequests.size(), employeeMailId);
        return myLeaveRequests;
    }

    @Override
    public List<LeaveRequest> getMyLeavesRequestsByStatus(String employeeMailId, String leaveStatus) throws
            SQLException, ClassNotFoundException {
        LOGGER.info("Fetching leave requests for employee email: {} with status: {}", employeeMailId, leaveStatus);
        List<LeaveRequest> myLeaveRequests = new ArrayList<>();
        Connection connection = DataBaseConnection.connect();

        PreparedStatement preparedStatement = connection.prepareStatement(MY_LEAVE_REQUEST_BY_STATUS_QUERY);

        preparedStatement.setString(1, employeeMailId);
        preparedStatement.setString(2, leaveStatus);

        ResultSet resultSet = preparedStatement.executeQuery();

        while (resultSet.next()) {
            int leaveId = resultSet.getInt("LEAVE_ID");
            int employeeId = resultSet.getInt("EMPLOYEE_ID");

            Date sqlFromDate = resultSet.getDate("LEAVE_FROM");
            Date sqlToDate = resultSet.getDate("LEAVE_TO");
            String leaveType = resultSet.getString("LEAVE_TYPE");
            String leaveReason = resultSet.getString("LEAVE_REASON");
            int daysCount = resultSet.getInt("DAYS_TAKEN");


            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

            LocalDate localFromDate = sqlFromDate.toLocalDate();
            String fromDate = localFromDate.format(dateFormatter);
            LocalDate localToDate = sqlToDate.toLocalDate();
            String toDate = localToDate.format(dateFormatter);

            LeaveRequest leaveRequest = new LeaveRequest(leaveId, employeeId, fromDate, toDate, leaveType,
                    leaveReason, leaveStatus, daysCount);
            myLeaveRequests.add(leaveRequest);
        }
        LOGGER.info("Fetched {} leave requests for employee email: {} with status: {}", myLeaveRequests.size(),
                employeeMailId, leaveStatus);
        return myLeaveRequests;
    }
}
