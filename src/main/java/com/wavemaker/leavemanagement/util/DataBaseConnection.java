package com.wavemaker.leavemanagement.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DataBaseConnection {
    private final static Logger LOGGER = LoggerFactory.getLogger(DataBaseConnection.class);
    private final static String url = "jdbc:mysql://localhost:3306/leave_management";
    private final static String user = "root";
    private final static String password = "1234";

    public static Connection connect() throws ClassNotFoundException, SQLException {


        Class.forName("com.mysql.cj.jdbc.Driver");

        Connection connection = DriverManager.getConnection(url, user, password);

        LOGGER.debug("database connected ");

        return connection;
    }
}
