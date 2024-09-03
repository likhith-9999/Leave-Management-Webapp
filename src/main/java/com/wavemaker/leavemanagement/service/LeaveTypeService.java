package com.wavemaker.leavemanagement.service;

import com.wavemaker.leavemanagement.model.LeaveType;
import com.wavemaker.leavemanagement.model.LeaveTypeDTO;

import java.sql.SQLException;
import java.util.List;

public interface LeaveTypeService {
    int getLeavesTakenByEmployeeOnLeaveTypeService(String employeeMailId, String leaveSatus) throws SQLException,
            ClassNotFoundException;



    List<LeaveTypeDTO> getAllLeavesTakenByEmployee(String employeeMailId) throws SQLException, ClassNotFoundException;


    List<LeaveTypeDTO> getAllLeavesTakenByTeam(String managerMailId) throws SQLException, ClassNotFoundException;
}
