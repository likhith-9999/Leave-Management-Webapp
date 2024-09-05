package com.wavemaker.leavemanagement.service.impl;

import com.wavemaker.leavemanagement.model.Holiday;
import com.wavemaker.leavemanagement.repository.HolidayRepository;
import com.wavemaker.leavemanagement.repository.impl.HolidayRepositoryImpl;
import com.wavemaker.leavemanagement.service.HolidayService;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

public class HolidayServiceImpl implements HolidayService {
    public final HolidayRepository holidayRepository = HolidayRepositoryImpl.getInstance();

    @Override
    public Set<LocalDate[]> getOverlappingHolidaysService(LocalDate startDate, LocalDate endDate)
            throws SQLException, ClassNotFoundException {
        return holidayRepository.getOverlappingHolidays(startDate, endDate);
    }

    @Override
    public List<Holiday> getAllHolidaysService() throws SQLException, ClassNotFoundException {
        return holidayRepository.getAllHolidays();
    }
}
