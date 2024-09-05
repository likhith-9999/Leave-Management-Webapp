package com.wavemaker.leavemanagement.repository;

import com.wavemaker.leavemanagement.model.Holiday;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

public interface HolidayRepository {
    Set<LocalDate[]> getOverlappingHolidays(LocalDate startDate, LocalDate endDate) throws SQLException,
            ClassNotFoundException;

    List<Holiday> getAllHolidays() throws SQLException, ClassNotFoundException;
}
