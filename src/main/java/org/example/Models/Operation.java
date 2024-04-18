package org.example.Models;

import java.util.ArrayList;
import java.util.Date;

public abstract class Operation {
    private String name;
    private Date startDate;
    private Date endDate;
    private Date deadline;

    private static ArrayList<String> operationNames = new ArrayList<>();

    public Operation(String name, Date startDate, Date endDate, Date deadline) {
        setName(name);
        this.startDate = startDate;
        this.endDate = endDate;
        this.deadline = deadline;
    }

    public Operation(String name, Date deadLine){//often you can make a task but don't know when it will start/end
        this.name = name;
        this.deadline = deadLine;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if(this.name != null){
            operationNames.remove(this.name);
        }
        if(!operationNames.contains(name)){
            this.name = name;
            operationNames.add(name);
        }else{
            throw new IllegalArgumentException("This named exists: " + name);
        }
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Date getDeadline() {
        return deadline;
    }

    public void setDeadline(Date deadline) {
        this.deadline = deadline;
    }

    @Override
    public String toString() {
        return
                " name='" + name + '\'' +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", deadline=" + deadline;
    }
}
