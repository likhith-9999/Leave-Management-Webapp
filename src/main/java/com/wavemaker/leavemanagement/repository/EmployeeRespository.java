package com.wavemaker.leavemanagement.repository;


import com.wavemaker.leavemanagement.model.Employee;

import java.sql.SQLException;

public interface EmployeeRespository {
    Employee getEmployeeByMail(String mail) throws SQLException, ClassNotFoundException;

    boolean checkManager(String emailID) throws SQLException, ClassNotFoundException;
}
