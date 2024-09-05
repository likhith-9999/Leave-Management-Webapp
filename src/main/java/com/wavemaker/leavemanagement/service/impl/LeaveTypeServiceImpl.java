package com.wavemaker.leavemanagement.service.impl;

import com.wavemaker.leavemanagement.model.LeaveTypeDTO;
import com.wavemaker.leavemanagement.repository.LeaveTypeRepository;
import com.wavemaker.leavemanagement.repository.impl.LeaveTypeRepositoryImpl;
import com.wavemaker.leavemanagement.service.LeaveTypeService;

import java.sql.SQLException;
import java.util.List;

public class LeaveTypeServiceImpl implements LeaveTypeService {
    LeaveTypeRepository leaveTypeRepository = LeaveTypeRepositoryImpl.getInstance();


    @Override
    public List<LeaveTypeDTO> getAllLeavesTakenByEmployee(String employeeMailId) throws SQLException,
            ClassNotFoundException {
        return leaveTypeRepository.getAllLeavesTakenByEmployee(employeeMailId);
    }


    @Override
    public List<LeaveTypeDTO> getAllLeavesTakenByTeam(String managerMailId) throws SQLException,
            ClassNotFoundException {
        return leaveTypeRepository.getAllLeavesTakenByTeam(managerMailId);
    }
}
