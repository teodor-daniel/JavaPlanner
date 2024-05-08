package org.example.Models;

import java.sql.Date;

public class Task {
    private int id;
    private String name;
    private String description;
    private Date dueDate;
    private String status;
    private Integer assignedTo;
    private Integer projectId;

    public Task(int id, String name, String description, Date dueDate, String status, Integer assignedTo, Integer projectId) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.assignedTo = assignedTo;
        this.projectId = projectId;
        this.dueDate = dueDate;
        this.status = status;
    }

    public Task(String name, String description,  Date dueDate, String status, Integer assignedTo, Integer projectId) {
        this.name = name;
        this.description = description;
        this.assignedTo = assignedTo;
        this.projectId = projectId;
        this.dueDate = dueDate;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getAssignedTo() {
        return assignedTo;
    }

    public void setAssignedTo(int assignedTo) {
        this.assignedTo = assignedTo;
    }

    public int getProjectId() {
        return projectId;
    }

    public void setProjectId(int projectId) {
        this.projectId = projectId;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Task{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", assignedTo=" + assignedTo +
                ", projectId=" + projectId +
                ", dueDate=" + dueDate +
                ", status='" + status + '\'' +
                '}';
    }
}
