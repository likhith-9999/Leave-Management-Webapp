package com.wavemaker.leavemanagement.repository.impl;


import com.wavemaker.leavemanagement.model.Employee;
import com.wavemaker.leavemanagement.repository.EmployeeRespository;
import com.wavemaker.leavemanagement.util.DataBaseConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;


public class EmployeeRepositoryImpl implements EmployeeRespository {
    private static final String GET_EMPLOYEE_BY_MAIL_ID_QUERY = "SELECT EMPLOYEE_NAME, EMPLOYEE_PHONE, EMPLOYEE_DOB, " + "EMPLOYEE_ID, MANAGER_ID, GENDER FROM EMPLOYEES WHERE EMAIL_ID=?";

    private static final String GET_EMPLOYEE_ID_QUERY = "SELECT EMPLOYEE_ID FROM EMPLOYEES WHERE EMAIL_ID = ?";
    private static final String CHECK_FOR_MANAGER_QUERY = "SELECT EMPLOYEE_ID FROM EMPLOYEES WHERE MANAGER_ID = ?";
    private final static Logger LOGGER = LoggerFactory.getLogger(EmployeeRepositoryImpl.class);

    @Override
    public Employee getEmployeeByMail(String emailID) throws SQLException, ClassNotFoundException {
        LOGGER.info("Getting employee by mail id");
        Connection connection = DataBaseConnection.connect();

        PreparedStatement preparedStatement = connection.prepareStatement(GET_EMPLOYEE_BY_MAIL_ID_QUERY);

        preparedStatement.setString(1, emailID);

        ResultSet resultSet = preparedStatement.executeQuery();
        Employee employee = null;
        while (resultSet.next()) {
            String employeeName = resultSet.getString(1);
            String employeePhone = resultSet.getString(2);
            Date sqlDOB = resultSet.getDate(3);
            int employeeId = resultSet.getInt(4);
            int managerId = resultSet.getInt(5);
            String employeeGender = resultSet.getString(6);

            LocalDate localDOB = sqlDOB.toLocalDate();
            DateTimeFormatter fromDateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            String employeeDOB = localDOB.format(fromDateFormatter);

            employee = new Employee(employeeId, employeeName, employeePhone, employeeDOB, emailID, managerId, employeeGender);
        }
        LOGGER.info("employee was returned");
        return employee;
    }

    @Override
    public boolean checkManager(String emailID) throws SQLException, ClassNotFoundException {
        LOGGER.info("check for manger");
        Connection connection = DataBaseConnection.connect();


        PreparedStatement preparedStatement = connection.prepareStatement(GET_EMPLOYEE_ID_QUERY);
        preparedStatement.setString(1, emailID);

        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            int employeeID = resultSet.getInt(1);


            preparedStatement = connection.prepareStatement(CHECK_FOR_MANAGER_QUERY);
            preparedStatement.setInt(1, employeeID);

            resultSet = preparedStatement.executeQuery();

            LOGGER.info("employee is manager");
            return resultSet.next();
        }

        LOGGER.info("employee is not a manager");
        return false;
    }


}
