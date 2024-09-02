package com.wavemaker.leavemanagement.service.impl;

import com.wavemaker.leavemanagement.model.LeaveDTO;
import com.wavemaker.leavemanagement.repository.MyTeamLeavesRepository;
import com.wavemaker.leavemanagement.repository.impl.MyTeamLeavesRepositoryImpl;
import com.wavemaker.leavemanagement.service.MyTeamLeavesService;

import java.sql.SQLException;
import java.util.List;

public class MyTeamLeavesServiceImpl implements MyTeamLeavesService {
    MyTeamLeavesRepository myTeamLeavesRepository = new MyTeamLeavesRepositoryImpl();

    @Override
    public void changeLeaveStatusService(int leaveId, String leaveStatus) throws SQLException, ClassNotFoundException {
        myTeamLeavesRepository.changeLeaveStatus(leaveId, leaveStatus);
    }

    @Override
    public List<LeaveDTO> getMyTeamLeaveRequestsByStatusService(String managerMailId, String leaveStatus) throws SQLException, ClassNotFoundException {
        return myTeamLeavesRepository.getMyTeamLeaveRequestsByStatus(managerMailId, leaveStatus);
    }

    @Override
    public List<LeaveDTO> getMyTeamLeaves(String managerMailId) throws SQLException, ClassNotFoundException {
        return myTeamLeavesRepository.getMyTeamLeaves(managerMailId);
    }
}
