package org.example.Models;

public class Project extends OperationA {
    private double budget;
    private int departmentId;

    public Project(int id, String name, String status, double budget, int departmentId) {
        super(id, name, status);
        this.budget = budget;
        this.departmentId = departmentId;
    }

    public double getBudget() {
        return budget;
    }

    public void setBudget(double budget) {
        this.budget = budget;
    }

    public int getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(int departmentId) {
        this.departmentId = departmentId;
    }

    @Override
    public String toString() {
        return "Project{" +
                "id=" + getId() +
                ", name='" + getName() + '\'' +
                ", status='" + getStatus() + '\'' +
                ", budget=" + budget +
                ", departmentId=" + departmentId +
                '}';
    }
}
