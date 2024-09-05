package com.wavemaker.leavemanagement.repository;

import com.wavemaker.leavemanagement.model.LeaveTypeDTO;

import java.sql.SQLException;
import java.util.List;

public interface LeaveTypeRepository {


    List<LeaveTypeDTO> getAllLeavesTakenByEmployee(String employeeMailId) throws SQLException, ClassNotFoundException;

    List<LeaveTypeDTO> getAllLeavesTakenByTeam(String managerMailId) throws SQLException, ClassNotFoundException;
}
