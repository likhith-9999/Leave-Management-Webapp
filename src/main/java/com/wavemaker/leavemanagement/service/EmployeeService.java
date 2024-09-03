package com.wavemaker.leavemanagement.service;


import com.wavemaker.leavemanagement.model.Employee;

import java.sql.SQLException;

public interface EmployeeService {
    Employee getEmployeeByMail(String emailID) throws SQLException, ClassNotFoundException;

    boolean checkManagerService(String emailID) throws SQLException, ClassNotFoundException;
}
