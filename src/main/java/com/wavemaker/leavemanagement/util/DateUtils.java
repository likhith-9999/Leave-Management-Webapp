package com.wavemaker.leavemanagement.util;


import com.wavemaker.leavemanagement.service.HolidayService;
import com.wavemaker.leavemanagement.service.impl.HolidayServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Set;

public class DateUtils {
    private final static Logger LOGGER = LoggerFactory.getLogger(DateUtils.class);

    public static boolean isHoliday(LocalDate date, Set<LocalDate[]> holidays) {
        for (LocalDate[] range : holidays) {
            if (!date.isBefore(range[0]) && !date.isAfter(range[1])) {
                return true;
            }
        }
        return false;
    }

    public static int countWeekdaysExcludingHolidays(LocalDate startDate, LocalDate endDate) throws SQLException, ClassNotFoundException {
        HolidayService holidayService = new HolidayServiceImpl();
        Set<LocalDate[]> holidays = holidayService.getOverlappingHolidaysService(startDate, endDate);

        if (startDate.isAfter(endDate)) {
            throw new IllegalArgumentException("Start date before or equal to end date.");
        }

        int count = 0;
        LocalDate current = startDate;

        while (!current.isAfter(endDate)) {
            if (current.getDayOfWeek() != DayOfWeek.SATURDAY && current.getDayOfWeek() != DayOfWeek.SUNDAY && !isHoliday(current, holidays)) {
                count++;
            }
            current = current.plusDays(1);
        }
        LOGGER.info("count = {}", count - 1);
        return count-1;
    }
}
