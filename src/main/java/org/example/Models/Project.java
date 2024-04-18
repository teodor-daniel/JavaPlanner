package org.example.Models;



import org.example.Enum.ActivityState;

import java.util.ArrayList;
import java.util.Date;

public class Project extends Task{
    ArrayList<Task> tasks;

    private ActivityState activityState;

    private double sum;

    private TeamLead teamLead;

    public Project(String name, Date startDate, Date endDate, Date deadline, String department, String activityState, double sum, TeamLead teamLead) {
        super(name, startDate, endDate, deadline, department);
        this.tasks = new ArrayList<>();
        setActivityState(activityState);
        this.sum = sum;
        this.teamLead = teamLead;
    }

    public Project(String name, Date deadLine, String department, String activityState, double sum, TeamLead teamLead) {
        super(name, deadLine, department);
        this.tasks = new ArrayList<>();
        setActivityState(activityState);
        this.sum = sum;
        this.teamLead = teamLead;
    }


    public ArrayList<Task> getTasks() {
        return tasks;
    }

    public void setTasks(ArrayList<Task> tasks) {
        this.tasks = tasks;
    }

    public void addTask(Task task){
        this.tasks.add(task);
    }

    public void removeTask(Task task){
        this.tasks.remove(task);
    }
    public ActivityState getActivityState() {
        return activityState;
    }

    public void setActivityState(String activityState) {
        try{
            this.activityState = ActivityState.valueOf(activityState.toUpperCase());
        }catch (IllegalArgumentException e){
            throw  new IllegalArgumentException("Invalid activityState");
        }
    }

    public double getSum() {
        return sum;
    }

    public void setSum(double sum) {
        this.sum = sum;
    }

    public TeamLead getTeamLead() {
        return teamLead;
    }

    public void setTeamLead(TeamLead teamLead) {
        this.teamLead = teamLead;
    }

    @Override
    public String toString() {
        return "Project{" +
                "tasks=" + tasks +
                ", activityState=" + activityState +
                ", sum=" + sum +
                ", teamLead=" + teamLead + " "
                + super.toString() + '}';
    }
}
