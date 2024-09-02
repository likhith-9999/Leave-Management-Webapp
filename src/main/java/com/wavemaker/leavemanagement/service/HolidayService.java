package com.wavemaker.leavemanagement.service;

import com.wavemaker.leavemanagement.model.Holiday;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

public interface HolidayService {
    Set<LocalDate[]> getOverlappingHolidaysService(LocalDate startDate, LocalDate endDate) throws SQLException,
            ClassNotFoundException;
    List<Holiday> getAllHolidaysService() throws SQLException, ClassNotFoundException;
}
