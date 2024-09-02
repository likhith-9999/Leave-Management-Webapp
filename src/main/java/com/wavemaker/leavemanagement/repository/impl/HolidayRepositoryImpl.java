package com.wavemaker.leavemanagement.repository.impl;

import com.wavemaker.leavemanagement.model.Holiday;
import com.wavemaker.leavemanagement.repository.HolidayRepository;
import com.wavemaker.leavemanagement.util.DataBaseConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class HolidayRepositoryImpl implements HolidayRepository {
    private static final String OVERLAPPING_HOLIDAYS_QUERY = "SELECT FROM_DATE, TO_DATE FROM HOLIDAYS WHERE " + "FROM_DATE <= ? AND TO_DATE >= ?";
    private final static String ALL_HOLIDAYS_QUERY = "SELECT * FROM HOLIDAYS";

    private static final Logger LOGGER = LoggerFactory.getLogger(HolidayRepositoryImpl.class);

    @Override
    public Set<LocalDate[]> getOverlappingHolidays(LocalDate startDate, LocalDate endDate) throws SQLException, ClassNotFoundException {
        Connection connection = DataBaseConnection.connect();
        LOGGER.info("getting the holiday between start and end date");

        Set<LocalDate[]> holidays = new HashSet<>();
        PreparedStatement preparedStatement = connection.prepareStatement(OVERLAPPING_HOLIDAYS_QUERY);
        preparedStatement.setDate(2, Date.valueOf(endDate));
        preparedStatement.setDate(1, Date.valueOf(startDate));

        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()) {
            LocalDate start = resultSet.getDate("FROM_DATE").toLocalDate();
            LocalDate end = resultSet.getDate("TO_DATE").toLocalDate();
            holidays.add(new LocalDate[]{start, end});
        }

        return holidays;
    }


    public List<Holiday> getAllHolidays() throws SQLException, ClassNotFoundException {
        List<Holiday> holidayList = new ArrayList<>();
        Connection connection = DataBaseConnection.connect();

        LOGGER.info("getting all holidays list");

        PreparedStatement preparedStatement = connection.prepareStatement(ALL_HOLIDAYS_QUERY);

        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()) {
            String holidayName = resultSet.getString("HOLIDAY_NAME");
            Date sqlFromDate = resultSet.getDate("FROM_DATE");
            Date sqlToDate = resultSet.getDate("TO_DATE");
            String holidayType = resultSet.getString("HOLIDAY_TYPE");
            int daysCount = resultSet.getInt("DAYS_COUNT");

            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

            LocalDate localFromDate = sqlFromDate.toLocalDate();
            String fromDate = localFromDate.format(dateFormatter);
            LocalDate localToDate = sqlToDate.toLocalDate();
            String toDate = localToDate.format(dateFormatter);

            Holiday holiday = new Holiday(holidayName, fromDate, toDate, holidayType, daysCount);
            holidayList.add(holiday);
        }
        return holidayList;
    }
}
