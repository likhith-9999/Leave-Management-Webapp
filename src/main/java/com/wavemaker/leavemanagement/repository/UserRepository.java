package com.wavemaker.leavemanagement.repository;

import java.sql.SQLException;

public interface UserRepository {
    boolean authenticate(String emailId, String password) throws SQLException, ClassNotFoundException;
}
