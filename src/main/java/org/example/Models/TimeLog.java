package org.example.Models;

import java.sql.Date;

public class TimeLog {
    private int id;
    private int taskId;
    private int employeeId;
    private double hoursLogged;
    private Date logDate;
    private String description;

    public TimeLog(int id, int taskId, int employeeId, double hoursLogged, Date logDate, String description) {
        this.id = id;
        this.taskId = taskId;
        this.employeeId = employeeId;
        this.hoursLogged = hoursLogged;
        this.logDate = logDate;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    public int getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }

    public double getHoursLogged() {
        return hoursLogged;
    }

    public void setHoursLogged(double hoursLogged) {
        this.hoursLogged = hoursLogged;
    }

    public Date getLogDate() {
        return logDate;
    }

    public void setLogDate(Date logDate) {
        this.logDate = logDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "TimeLog{" +
                "id=" + id +
                ", taskId=" + taskId +
                ", employeeId=" + employeeId +
                ", hoursLogged=" + hoursLogged +
                ", logDate=" + logDate +
                ", description='" + description + '\'' +
                '}';
    }
}
