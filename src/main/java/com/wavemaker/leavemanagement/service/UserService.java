package com.wavemaker.leavemanagement.service;

import java.sql.SQLException;

public interface UserService {
    boolean authenticate(String emailId, String password) throws SQLException, ClassNotFoundException;
}
