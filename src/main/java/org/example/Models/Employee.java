package org.example.Models;

import org.example.Interfaces.Iemployee;

public class Employee extends PersonA implements Iemployee {
    private int id;
    private double salary;
    private Department department;
    private String employedStatus;
    private Integer bonus;
    private Integer teamLeadId;
    private Integer companyId;

    public Employee(int id, String name, int age, String phoneNumber, String email, double salary, Department department, Integer teamLeadId, Integer companyId, String employedStatus) {
        super(name, age, phoneNumber, email);
        this.id = id;
        this.salary = salary;
        this.bonus = null;
        this.department = department;
        this.teamLeadId = teamLeadId;
        this.companyId = companyId;
        this.employedStatus = employedStatus;
    }

    public Employee(int id, String name, int age, String phoneNumber, String email, double salary, Department department, String employedStatus) {
        super(name, age, phoneNumber, email);
        this.id = id;
        this.salary = salary;
        this.bonus = null;
        this.department = department;
        this.teamLeadId = null;
        this.companyId = null;
        this.employedStatus = employedStatus;
    }

    public Employee(String name, int age, String phoneNumber, String email, double salary, Department department, Integer teamLeadId, Integer companyId, String employedStatus) {
        super(name, age, phoneNumber, email);
        this.salary = salary;
        this.bonus = null;
        this.department = department;
        this.teamLeadId = teamLeadId;
        this.companyId = companyId;
        this.employedStatus = employedStatus;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getSalary() {
        return salary;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public Integer getBonus() {
        return bonus;
    }

    public void setBonus(Integer bonus) {
        this.bonus = bonus;
    }

    public String getEmployedStatus() {
        return employedStatus;
    }

    public void setEmployedStatus(String employedStatus) {
        this.employedStatus = employedStatus;
    }

    public Integer getTeamLeadId() {
        return teamLeadId;
    }

    public void setTeamLeadId(Integer teamLeadId) {
        this.teamLeadId = teamLeadId;
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    @Override
    public String isEmployed() {
    return this.employedStatus;
    }

    @Override
    public void resign() {
        this.employedStatus = "Resigned";
    }

    @Override
    public String toString() {
        return "Employee{" +
                "id=" + id +
                ", salary=" + salary +
                ", department=" + (department != null ? department.getName() : "No department") +
                ", employedStatus='" + employedStatus + '\'' +
                ", bonus=" + bonus +
                ", teamLeadId=" + teamLeadId +
                ", companyId=" + companyId +
                "} " + super.toString();
    }
}
