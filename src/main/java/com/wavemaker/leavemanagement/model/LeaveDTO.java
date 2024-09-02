package com.wavemaker.leavemanagement.model;

public class LeaveDTO extends LeaveRequest {
    private String employeeName;
    private String employeeEmailId;

    public LeaveDTO(int leaveId, int employeeID, String fromDate, String toDate, String leaveType, String leaveReason, String leaveStatus, int daysCount, String employeeName, String employeeEmailId) {
        super(leaveId, employeeID, fromDate, toDate, leaveType, leaveReason, leaveStatus, daysCount);
        this.employeeName = employeeName;
        this.employeeEmailId = employeeEmailId;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public String getEmployeeEmailId() {
        return employeeEmailId;
    }

    public void setEmployeeEmailId(String employeeEmailId) {
        this.employeeEmailId = employeeEmailId;
    }

    @Override
    public String toString() {
        return super.toString() + ", LeaveDTO{" + "employeeName='" + employeeName + '\'' + ", employeeEmailId='" + employeeEmailId + '\'' + '}';
    }
}
