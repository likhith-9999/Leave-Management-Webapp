package com.wavemaker.leavemanagement.service.impl;

import com.wavemaker.leavemanagement.repository.UserRepository;
import com.wavemaker.leavemanagement.repository.impl.UserRepositoryImpl;
import com.wavemaker.leavemanagement.service.UserService;

import java.sql.SQLException;

public class UserServiceImpl implements UserService {
    UserRepository userRepository = new UserRepositoryImpl();

    @Override
    public boolean authenticate(String emailId, String password) throws SQLException, ClassNotFoundException {
        return userRepository.authenticate(emailId, password);
    }
}
