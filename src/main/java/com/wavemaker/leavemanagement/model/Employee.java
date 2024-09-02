package com.wavemaker.leavemanagement.model;

public class Employee {
    private int employeeId;
    private String employeeName;
    private String employeePhone;
    private String employeeDOB;
    private String emailId;
    private int managerId;
    private String employeeGender;

    public Employee(int employeeId, String employeeName, String employeePhone, String employeeDOB, String emailId, int managerId, String employeeGender) {
        this.employeeId = employeeId;
        this.employeeName = employeeName;
        this.employeePhone = employeePhone;
        this.employeeDOB = employeeDOB;
        this.emailId = emailId;
        this.managerId = managerId;
        this.employeeGender = employeeGender;
    }

    public int getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public String getEmployeePhone() {
        return employeePhone;
    }

    public void setEmployeePhone(String employeePhone) {
        this.employeePhone = employeePhone;
    }

    public String getEmployeeDOB() {
        return employeeDOB;
    }

    public void setEmployeeDOB(String employeeDOB) {
        this.employeeDOB = employeeDOB;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public int getManagerId() {
        return managerId;
    }

    public void setManagerId(int managerId) {
        this.managerId = managerId;
    }

    public String getEmployeeGender() {
        return employeeGender;
    }

    public void setEmployeeGender(String employeeGender) {
        this.employeeGender = employeeGender;
    }

    @Override
    public String toString() {
        return "Employee{" + "employeeId=" + employeeId + ", employeeName='" + employeeName + '\'' + ", employeePhone='" + employeePhone + '\'' + ", employeeDOB='" + employeeDOB + '\'' + ", emailId='" + emailId + '\'' + ", managerId=" + managerId + ", employeeGender='" + employeeGender + '\'' + '}';
    }
}
