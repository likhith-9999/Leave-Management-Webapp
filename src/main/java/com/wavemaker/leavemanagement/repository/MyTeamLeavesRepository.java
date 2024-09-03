package com.wavemaker.leavemanagement.repository;

import com.wavemaker.leavemanagement.model.Employee;
import com.wavemaker.leavemanagement.model.LeaveDTO;

import java.sql.SQLException;
import java.util.List;

public interface MyTeamLeavesRepository {
    void changeLeaveStatus(int leaveID, String leaveStatus) throws SQLException, ClassNotFoundException;

    List<LeaveDTO> getMyTeamLeaveRequestsByStatus(String managerMailId, String leaveStatus) throws SQLException, ClassNotFoundException;

    List<LeaveDTO> getMyTeamLeaves(String managerMailId) throws SQLException, ClassNotFoundException;

    List<Employee> getMyTeamDetails(String managerMailId) throws SQLException, ClassNotFoundException;
}
