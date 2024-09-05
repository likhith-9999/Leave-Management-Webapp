package com.wavemaker.leavemanagement.repository.impl;


import com.wavemaker.leavemanagement.model.LeaveTypeDTO;
import com.wavemaker.leavemanagement.repository.LeaveTypeRepository;
import com.wavemaker.leavemanagement.util.DataBaseConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class LeaveTypeRepositoryImpl implements LeaveTypeRepository {
    private final static Logger LOGGER = LoggerFactory.getLogger(LeaveTypeRepositoryImpl.class);
    private final static String ALL_LEAVES_TAKEN_BY_EMPLOYEE = "SELECT LT.LEAVE_TYPE, COALESCE(SUM(L.DAYS_TAKEN), 0)"
            + " AS TOTAL_LEAVES_TAKEN, LT.LEAVE_LIMIT, LT.GENDER FROM LEAVES_TYPE LT LEFT JOIN LEAVES L ON "
            + "L.LEAVE_TYPE = LT.LEAVE_TYPE AND L.LEAVE_STATUS = 'Approved' AND L.EMPLOYEE_ID = (SELECT EMPLOYEE_ID "
            + "FROM EMPLOYEES WHERE EMAIL_ID = ?) GROUP BY LT.LEAVE_TYPE, LT.LEAVE_LIMIT;";
    private final static String TEAM_SIZE_QUERY = "SELECT EMPLOYEE_ID FROM EMPLOYEES WHERE MANAGER_ID IN "
            + "(SELECT EMPLOYEE_ID FROM EMPLOYEES WHERE EMAIL_ID = ?);";
    private final static String ALL_LEAVES_TAKEN_BY_TEAM = "SELECT LT.LEAVE_TYPE, COALESCE(SUM(L.DAYS_TAKEN), 0)"
            + " AS TOTAL_LEAVES_TAKEN, LT.LEAVE_LIMIT, LT.GENDER FROM LEAVES_TYPE LT " +
            "LEFT JOIN LEAVES L ON L.LEAVE_TYPE" + " = LT.LEAVE_TYPE AND L.LEAVE_STATUS = 'Approved'" +
            " AND L.EMPLOYEE_ID IN ( SELECT EMPLOYEE_ID FROM EMPLOYEES" + " WHERE MANAGER_ID = " +
            "( SELECT EMPLOYEE_ID FROM EMPLOYEES WHERE EMAIL_ID = ? ) ) GROUP BY LT.LEAVE_TYPE," + " LT.LEAVE_LIMIT;";
    private static LeaveTypeRepositoryImpl singleInstance;

    private LeaveTypeRepositoryImpl() {

    }

    public static synchronized LeaveTypeRepositoryImpl getInstance() {
        if (singleInstance == null) {
            singleInstance = new LeaveTypeRepositoryImpl();
        }
        return singleInstance;
    }

    @Override
    public List<LeaveTypeDTO> getAllLeavesTakenByEmployee(String employeeMailId) throws SQLException, ClassNotFoundException {
        List<LeaveTypeDTO> leaveTypeDTOList = new ArrayList<>();
        LOGGER.info("Fetching all leaves taken by employee {}", employeeMailId);
        Connection connection = DataBaseConnection.connect();
        PreparedStatement preparedStatement = connection.prepareStatement(ALL_LEAVES_TAKEN_BY_EMPLOYEE);

        preparedStatement.setString(1, employeeMailId);
        ResultSet resultSet = preparedStatement.executeQuery();

        while (resultSet.next()) {
            String leaveType = resultSet.getString("LEAVE_TYPE");
            int daysCount = resultSet.getInt("TOTAL_LEAVES_TAKEN");
            int leaveLimit = resultSet.getInt("LEAVE_LIMIT");
            String gender = resultSet.getString("GENDER");

            LeaveTypeDTO leaveTypeDTO = new LeaveTypeDTO(leaveType, leaveLimit, gender, daysCount);
            leaveTypeDTOList.add(leaveTypeDTO);
        }

        LOGGER.info("Fetched {} leave types for employee {}", leaveTypeDTOList.size(), employeeMailId);
        return leaveTypeDTOList;
    }

    @Override
    public List<LeaveTypeDTO> getAllLeavesTakenByTeam(String managerMailId) throws SQLException, ClassNotFoundException {
        List<LeaveTypeDTO> leaveTypeDTOList = new ArrayList<>();
        LOGGER.info("Fetching all leaves taken by team under manager {}", managerMailId);
        Connection connection = DataBaseConnection.connect();
        PreparedStatement preparedStatement = connection.prepareStatement(TEAM_SIZE_QUERY);
        preparedStatement.setString(1, managerMailId);
        ResultSet resultSet = preparedStatement.executeQuery();
        int teamSize = 0;
        while (resultSet.next()) {
            teamSize++;
        }

        preparedStatement = connection.prepareStatement(ALL_LEAVES_TAKEN_BY_TEAM);
        preparedStatement.setString(1, managerMailId);
        resultSet = preparedStatement.executeQuery();
        while (resultSet.next()) {
            String leaveType = resultSet.getString("LEAVE_TYPE");
            int daysCount = resultSet.getInt("TOTAL_LEAVES_TAKEN");
            int leaveLimit = resultSet.getInt("LEAVE_LIMIT") * teamSize;
            String gender = resultSet.getString("GENDER");

            LeaveTypeDTO leaveTypeDTO = new LeaveTypeDTO(leaveType, leaveLimit, gender, daysCount);
            leaveTypeDTOList.add(leaveTypeDTO);
        }
        LOGGER.info("Fetched {} leave types for team under manager {}", leaveTypeDTOList.size(), managerMailId);
        return leaveTypeDTOList;
    }
}
