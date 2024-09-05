package com.wavemaker.leavemanagement.model;

public class LeaveType {
    private String leaveType;
    private int leaveLimit;
    private String gender;

    public LeaveType(String leaveType, int leaveLimit, String gender) {
        this.leaveType = leaveType;
        this.leaveLimit = leaveLimit;
        this.gender = gender;
    }

    public String getLeaveType() {
        return leaveType;
    }

    public void setLeaveType(String leaveType) {
        this.leaveType = leaveType;
    }

    public int getLeaveLimit() {
        return leaveLimit;
    }

    public void setLeaveLimit(int leaveLimit) {
        this.leaveLimit = leaveLimit;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    @Override
    public String toString() {
        return "LeaveType{" + "leaveType='" + leaveType + '\'' + ", leaveLimit=" + leaveLimit + ", gender='" + gender + '\'' + '}';
    }
}
