package com.wavemaker.leavemanagement.repository.impl;

import com.wavemaker.leavemanagement.repository.UserRepository;
import com.wavemaker.leavemanagement.util.DataBaseConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserRepositoryImpl implements UserRepository {
    private final static String AUTHENTICATION_QUERY = "SELECT EMAIL_ID, PASSWORD FROM USERS WHERE EMAIL_ID = ? "
            + "AND PASSWORD = ?";
    private static final Logger LOGGER = LoggerFactory.getLogger(UserRepositoryImpl.class);
    private static UserRepositoryImpl singleInstance;

    private UserRepositoryImpl() {

    }

    public static synchronized UserRepositoryImpl getInstance() {
        if (singleInstance == null) {
            singleInstance = new UserRepositoryImpl();
        }
        return singleInstance;
    }

    @Override
    public boolean authenticate(String emailId, String password) throws SQLException, ClassNotFoundException {
        LOGGER.info("Authenticating user with email ID: {}", emailId);
        Connection connection = DataBaseConnection.connect();


        PreparedStatement preparedStatement = connection.prepareStatement(AUTHENTICATION_QUERY);
        preparedStatement.setString(1, emailId);
        preparedStatement.setString(2, password);

        ResultSet resultSet = preparedStatement.executeQuery();

        return resultSet.next();
    }
}
