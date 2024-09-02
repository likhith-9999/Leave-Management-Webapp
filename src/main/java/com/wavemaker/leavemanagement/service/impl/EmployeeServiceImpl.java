package com.wavemaker.leavemanagement.service.impl;


import com.wavemaker.leavemanagement.model.Employee;
import com.wavemaker.leavemanagement.repository.EmployeeRespository;
import com.wavemaker.leavemanagement.repository.impl.EmployeeRepositoryImpl;
import com.wavemaker.leavemanagement.service.EmployeeService;

import java.sql.SQLException;

public class EmployeeServiceImpl implements EmployeeService {
    private static final EmployeeRespository employeeRepository = new EmployeeRepositoryImpl();

    @Override
    public Employee getEmployeeByMail(String emailID) throws SQLException, ClassNotFoundException {
        return employeeRepository.getEmployeeByMail(emailID);
    }

    public boolean checkManagerService(String emailID) throws SQLException, ClassNotFoundException {
        return employeeRepository.checkManager(emailID);
    }
}
