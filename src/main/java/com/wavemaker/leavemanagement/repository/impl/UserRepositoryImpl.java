package com.wavemaker.leavemanagement.repository.impl;

import com.wavemaker.leavemanagement.repository.UserRepository;
import com.wavemaker.leavemanagement.util.DataBaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserRepositoryImpl implements UserRepository {
    private final static String AUTHENTICATION_QUERY = "SELECT EMAIL_ID, PASSWORD FROM USERS WHERE EMAIL_ID = ? " + "AND PASSWORD = ?";

    @Override
    public boolean authenticate(String emailId, String password) throws SQLException, ClassNotFoundException {
        Connection connection = DataBaseConnection.connect();


        PreparedStatement preparedStatement = connection.prepareStatement(AUTHENTICATION_QUERY);
        preparedStatement.setString(1, emailId);
        preparedStatement.setString(2, password);

        ResultSet resultSet = preparedStatement.executeQuery();

        return resultSet.next();
    }
}
