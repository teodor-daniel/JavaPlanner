package org.example.Models;

public class Project extends OperationA {
    private double budget;
    private Integer departmentId;
    private Integer teamLead;

    public Project(int id, String name, String status, double budget, Integer departmentId, Integer teamLead) {
        super(id, name, status);
        this.budget = budget;
        this.departmentId = departmentId;
        this.teamLead = teamLead;
    }

    public Project(String name, String status, double budget, Integer departmentId, Integer teamLead) {
        super(0, name, status);
        this.budget = budget;
        this.departmentId = departmentId;
        this.teamLead = teamLead;
    }

    // Default constructor
    public Project() {
        super(0, "Unnamed Project", "Not started");
        this.budget = 0.0;
        this.departmentId = null;
        this.teamLead = null;
    }

    public double getBudget() {
        return budget;
    }

    public void setBudget(double budget) {
        this.budget = budget;
    }

    public Integer getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Integer departmentId) {
        this.departmentId = departmentId;
    }

    public Integer getTeamLead() {
        return teamLead;
    }

    public void setTeamLead(Integer teamLead) {
        this.teamLead = teamLead;
    }

    @Override
    public String toString() {
        return "Project{" +
                "id=" + getId() +
                ", name='" + getName() + '\'' +
                ", status='" + getStatus() + '\'' +
                ", budget=" + budget +
                ", departmentId=" + departmentId +
                ", teamLead=" + teamLead +
                '}';
    }
}
