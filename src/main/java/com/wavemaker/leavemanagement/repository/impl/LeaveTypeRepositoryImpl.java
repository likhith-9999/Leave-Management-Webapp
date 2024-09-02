package com.wavemaker.leavemanagement.repository.impl;


import com.wavemaker.leavemanagement.model.LeaveTypeDTO;
import com.wavemaker.leavemanagement.repository.LeaveTypeRepository;
import com.wavemaker.leavemanagement.util.DataBaseConnection;
import com.wavemaker.leavemanagement.util.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LeaveTypeRepositoryImpl implements LeaveTypeRepository {
    private final static Logger LOGGER = LoggerFactory.getLogger(LeaveTypeRepositoryImpl.class);
    private final static String LEAVES_TAKEN_BY_EMPLOYEE_QUERY_BY_TYPE = "SELECT\n" + "    E.EMAIL_ID,\n" + "    L.LEAVE_ID,\n" + "    L.LEAVE_FROM,\n" + "    L.LEAVE_TO,\n" + "    L.LEAVE_REASON,\n" + "    L.LEAVE_STATUS\n" + "FROM\n" + "    EMPLOYEES E\n" + "JOIN\n" + "    LEAVES L ON E.EMPLOYEE_ID = L.EMPLOYEE_ID\n" + "JOIN\n" + "    LEAVES_TYPE LT ON L.LEAVE_TYPE = LT.LEAVE_TYPE\n" + "WHERE\n" + "    E.EMAIL_ID = ?\n" + "    AND LT.LEAVE_TYPE = ?;\n";


    private final static String ALL_LEAVES_TAKEN_BY_EMPLOYEE = "SELECT \n" + "    LT.LEAVE_TYPE,\n" + "    COALESCE(SUM(L.DAYS_TAKEN), 0) AS TOTAL_LEAVES_TAKEN,\n" + "    LT.LEAVE_LIMIT,\n" + "    LT.GENDER\n" + "FROM LEAVES_TYPE LT\n" + "LEFT JOIN LEAVES L ON L.LEAVE_TYPE = LT.LEAVE_TYPE\n" + "    AND L.LEAVE_STATUS = 'Approved'\n" + "    AND L.EMPLOYEE_ID = (SELECT EMPLOYEE_ID FROM EMPLOYEES WHERE EMAIL_ID = ?) \n" + "GROUP BY LT.LEAVE_TYPE, LT.LEAVE_LIMIT;";


    private final static String TEAM_SIZE_QUERY = "SELECT EMPLOYEE_ID FROM EMPLOYEES WHERE MANAGER_ID IN (SELECT " + "EMPLOYEE_ID FROM EMPLOYEES WHERE EMAIL_ID = ?);";

    private final static String ALL_LEAVES_TAKEN_BY_TEAM = "SELECT \n" + "    LT.LEAVE_TYPE,\n" + "    COALESCE(SUM(L.DAYS_TAKEN), 0) AS TOTAL_LEAVES_TAKEN,\n" + "    LT.LEAVE_LIMIT, LT.GENDER\n" + "FROM LEAVES_TYPE LT\n" + "LEFT JOIN LEAVES L ON L.LEAVE_TYPE = LT.LEAVE_TYPE\n" + "    AND L.LEAVE_STATUS = 'Approved'\n" + "    AND L.EMPLOYEE_ID IN (\n" + "        SELECT EMPLOYEE_ID \n" + "        FROM EMPLOYEES \n" + "        WHERE MANAGER_ID = (\n" + "            SELECT EMPLOYEE_ID \n" + "            FROM EMPLOYEES \n" + "            WHERE EMAIL_ID = ?\n" + "        )\n" + "    )\n" + "GROUP BY LT.LEAVE_TYPE, LT.LEAVE_LIMIT;";

    @Override
    public int getLeavesTakenByEmployeeOnLeaveType(String employeeMailId, String leaveType) throws SQLException, ClassNotFoundException {
        Connection connection = DataBaseConnection.connect();
        LOGGER.info("{} leaves taken by employee", leaveType);

        PreparedStatement preparedStatement = connection.prepareStatement(LEAVES_TAKEN_BY_EMPLOYEE_QUERY_BY_TYPE);
        preparedStatement.setString(1, employeeMailId);
        preparedStatement.setString(2, leaveType);

        ResultSet resultSet = preparedStatement.executeQuery();
        int count = 0;
        while (resultSet.next()) {
            Date sqlFromDate = resultSet.getDate("LEAVE_FROM");
            Date sqlToDate = resultSet.getDate("LEAVE_TO");

            count = DateUtils.countWeekdaysExcludingHolidays(sqlFromDate.toLocalDate(), sqlToDate.toLocalDate());
        }
        LOGGER.info("leaves count = {}", count);
        return count;
    }


    @Override
    public List<LeaveTypeDTO> getAllLeavesTakenByEmployee(String employeeMailId) throws SQLException, ClassNotFoundException {
        List<LeaveTypeDTO> leaveTypeDTOList = new ArrayList<>();
        LOGGER.info("all leaves taken by employee");
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


        return leaveTypeDTOList;
    }

    @Override
    public List<LeaveTypeDTO> getAllLeavesTakenByTeam(String managerMailId) throws SQLException, ClassNotFoundException {
        List<LeaveTypeDTO> leaveTypeDTOList = new ArrayList<>();
        LOGGER.info("all leaves taken by team");
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
        return leaveTypeDTOList;
    }
}
