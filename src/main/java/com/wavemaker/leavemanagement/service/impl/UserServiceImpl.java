package com.wavemaker.leavemanagement.service.impl;

import com.wavemaker.leavemanagement.repository.UserRepository;
import com.wavemaker.leavemanagement.repository.impl.UserRepositoryImpl;
import com.wavemaker.leavemanagement.service.UserService;

import java.sql.SQLException;

public class UserServiceImpl implements UserService {
    UserRepository userRepository = UserRepositoryImpl.getInstance();

    @Override
    public boolean authenticate(String emailId, String password) throws SQLException, ClassNotFoundException {
        return userRepository.authenticate(emailId, password);
    }
}
