package com.wavemaker.leavemanagement.model;

public class Holiday {
    private String holidayName;
    private String holidayFromDate;
    private String holidayToDate;
    private String holidayType;
    private int daysCount;

    public Holiday(String holidayName, String holidayFromDate, String holidayToDate, String holidayType, int daysCount) {
        this.holidayName = holidayName;
        this.holidayFromDate = holidayFromDate;
        this.holidayToDate = holidayToDate;
        this.holidayType = holidayType;
        this.daysCount = daysCount;
    }

    public String getHolidayName() {
        return holidayName;
    }

    public void setHolidayName(String holidayName) {
        this.holidayName = holidayName;
    }

    public String getHolidayFromDate() {
        return holidayFromDate;
    }

    public void setHolidayFromDate(String holidayFromDate) {
        this.holidayFromDate = holidayFromDate;
    }

    public String getHolidayToDate() {
        return holidayToDate;
    }

    public void setHolidayToDate(String holidayToDate) {
        this.holidayToDate = holidayToDate;
    }

    public String getHolidayType() {
        return holidayType;
    }

    public void setHolidayType(String holidayType) {
        this.holidayType = holidayType;
    }

    public int getDaysCount() {
        return daysCount;
    }

    public void setDaysCount(int daysCount) {
        this.daysCount = daysCount;
    }

    @Override
    public String toString() {
        return "Holiday{" + "holidayName='" + holidayName + '\'' + ", holidayFromDate='" + holidayFromDate + '\'' + ", holidayToDate='" + holidayToDate + '\'' + ", holidayType='" + holidayType + '\'' + ", daysCount=" + daysCount + '}';
    }
}
