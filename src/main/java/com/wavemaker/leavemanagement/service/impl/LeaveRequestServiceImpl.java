package com.wavemaker.leavemanagement.service.impl;


import com.wavemaker.leavemanagement.model.LeaveRequest;
import com.wavemaker.leavemanagement.repository.LeaveRequestRepository;
import com.wavemaker.leavemanagement.repository.impl.LeaveRequestRepositoryImpl;
import com.wavemaker.leavemanagement.service.LeaveRequestService;

import java.sql.SQLException;
import java.util.List;

public class LeaveRequestServiceImpl implements LeaveRequestService {
    private static final LeaveRequestRepository leaveRequestRepository = new LeaveRequestRepositoryImpl();

    @Override
    public void applyLeaveService(LeaveRequest leaveRequest) throws SQLException, ClassNotFoundException {
        leaveRequestRepository.applyLeave(leaveRequest);
    }


    @Override
    public List<LeaveRequest> getMyLeaveRequestService(String employeeMailId) throws SQLException, ClassNotFoundException {
        return leaveRequestRepository.getMyLeaveRequests(employeeMailId);
    }

    public List<LeaveRequest> getMyLeaveRequestByStatusService(String employeeMailId, String leaveStatus) throws SQLException, ClassNotFoundException {
        return leaveRequestRepository.getMyLeavesRequestsByStatus(employeeMailId, leaveStatus);
    }
}
