package com.wavemaker.leavemanagement.model;

public class LeaveRequest {
    private int leaveId;
    private int employeeID;
    private String fromDate;
    private String toDate;
    private String leaveType;
    private String leaveReason;
    private String leaveStatus;
    private int daysCount;

    public LeaveRequest(int leaveId, int employeeID, String fromDate, String toDate, String leaveType, String leaveReason, String leaveStatus, int daysCount) {
        this.leaveId = leaveId;
        this.employeeID = employeeID;
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.leaveType = leaveType;
        this.leaveReason = leaveReason;
        this.leaveStatus = leaveStatus;
        this.daysCount = daysCount;
    }

    public int getDaysCount() {
        return daysCount;
    }

    public void setDaysCount(int daysCount) {
        this.daysCount = daysCount;
    }

    public int getLeaveId() {
        return leaveId;
    }

    public void setLeaveId(int leaveId) {
        this.leaveId = leaveId;
    }

    public int getEmployeeID() {
        return employeeID;
    }

    public void setEmployeeID(int employeeID) {
        this.employeeID = employeeID;
    }

    public String getFromDate() {
        return fromDate;
    }

    public void setFromDate(String fromDate) {
        this.fromDate = fromDate;
    }

    public String getToDate() {
        return toDate;
    }

    public void setToDate(String toDate) {
        this.toDate = toDate;
    }

    public String getLeaveType() {
        return leaveType;
    }

    public void setLeaveType(String leaveType) {
        this.leaveType = leaveType;
    }

    public String getLeaveReason() {
        return leaveReason;
    }

    public void setLeaveReason(String leaveReason) {
        this.leaveReason = leaveReason;
    }

    public String getLeaveStatus() {
        return leaveStatus;
    }

    public void setLeaveStatus(String leaveStatus) {
        this.leaveStatus = leaveStatus;
    }

    @Override
    public String toString() {
        return "LeaveRequest{" + "leaveId=" + leaveId + ", employeeID=" + employeeID + ", fromDate='" + fromDate + '\'' + ", toDate='" + toDate + '\'' + ", leaveType='" + leaveType + '\'' + ", leaveReason='" + leaveReason + '\'' + ", leaveStatus='" + leaveStatus + '\'' + ", daysCount=" + daysCount + '}';
    }
}
