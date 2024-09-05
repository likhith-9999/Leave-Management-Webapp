package com.wavemaker.leavemanagement.service;

import com.wavemaker.leavemanagement.model.Employee;
import com.wavemaker.leavemanagement.model.LeaveDTO;

import java.sql.SQLException;
import java.util.List;

public interface MyTeamLeavesService {
    void changeLeaveStatusService(int leaveId, String leaveStatus) throws SQLException, ClassNotFoundException;

    List<LeaveDTO> getMyTeamLeaveRequestsByStatusService(String managerMailId, String leaveStatus) throws SQLException,
            ClassNotFoundException;

    List<LeaveDTO> getMyTeamLeaves(String managerMailId) throws SQLException, ClassNotFoundException;

    List<Employee> getTeamDetails(String managerMailId) throws SQLException, ClassNotFoundException;
}
