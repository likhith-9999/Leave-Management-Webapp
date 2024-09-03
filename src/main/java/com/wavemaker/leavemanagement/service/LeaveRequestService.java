package com.wavemaker.leavemanagement.service;


import com.wavemaker.leavemanagement.model.LeaveRequest;

import java.sql.SQLException;
import java.util.List;

public interface LeaveRequestService {
    void applyLeaveService(LeaveRequest leaveRequest) throws SQLException, ClassNotFoundException;


    List<LeaveRequest> getMyLeaveRequestService(String employeeMailId) throws SQLException, ClassNotFoundException;

    List<LeaveRequest> getMyLeaveRequestByStatusService(String employeeMailId, String leaveStatus) throws SQLException,
            ClassNotFoundException;


}
