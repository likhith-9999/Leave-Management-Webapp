package com.wavemaker.leavemanagement.repository;


import com.wavemaker.leavemanagement.model.LeaveRequest;

import java.sql.SQLException;
import java.util.List;

public interface LeaveRequestRepository {
    void applyLeave(LeaveRequest leaveRequest) throws SQLException, ClassNotFoundException;


    List<LeaveRequest> getMyLeaveRequests(String employeeMailId) throws SQLException, ClassNotFoundException;

    List<LeaveRequest> getMyLeavesRequestsByStatus(String employeeMailId, String leaveStatus) throws SQLException, ClassNotFoundException;
}
