package com.wavemaker.leavemanagement.service;

import com.wavemaker.leavemanagement.model.LeaveTypeDTO;

import java.sql.SQLException;
import java.util.List;

public interface LeaveTypeService {


    List<LeaveTypeDTO> getAllLeavesTakenByEmployee(String employeeMailId) throws SQLException, ClassNotFoundException;


    List<LeaveTypeDTO> getAllLeavesTakenByTeam(String managerMailId) throws SQLException, ClassNotFoundException;
}
