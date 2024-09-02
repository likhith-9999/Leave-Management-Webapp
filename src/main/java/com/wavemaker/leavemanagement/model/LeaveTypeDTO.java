package com.wavemaker.leavemanagement.model;

public class LeaveTypeDTO extends LeaveType {
    private int daysCount;

    public LeaveTypeDTO(String leaveType, int leaveLimit, String gender, int daysCount) {
        super(leaveType, leaveLimit, gender);
        this.daysCount = daysCount;
    }

    public int getDaysCount() {
        return daysCount;
    }

    public void setDaysCount(int daysCount) {
        this.daysCount = daysCount;
    }

    @Override
    public String toString() {
        return "LeaveTypeDTO{" + "daysCount=" + daysCount + "} " + super.toString();
    }
}
