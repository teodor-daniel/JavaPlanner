package org.example.Models;

import org.example.Interfaces.Iemployee;

public class Employee extends PersonA implements Iemployee {

    private int id;
    private double salary;
    private Department department;  // Changed from Departments enum to Department object
    private boolean employedStatus = true;
    private Integer bonus;
    private Integer teamLeadId;
    private Integer company_id;

    // Constructors
    public Employee(int id, String name, int age, String phoneNumber, String email, double salary, Department department, int teamLeadId, int company_id) {
        super(name, age, phoneNumber, email);
        this.id = id;
        this.salary = salary;
        this.bonus = null;
        this.department = department;
        this.teamLeadId = teamLeadId;
        this.company_id = company_id;
    }

    public Employee(int id, String name, int age, String phoneNumber, String email, double salary, Department department) {
        super(name, age, phoneNumber, email);
        this.id = id;
        this.salary = salary;
        this.bonus = null;
        this.department = department;
        this.teamLeadId = null;
        this.company_id = null;
    }

    public Employee(String name, int age, String phoneNumber, String email, double salary, Department department, int teamLeadId, int company_id) {
        super(name, age, phoneNumber, email);
        this.salary = salary;
        this.bonus = null;
        this.department = department;
        this.teamLeadId = teamLeadId;
        this.company_id = company_id;
    }

    public Employee(String name, int age, String phoneNumber, String email, double salary, Department department) {
        super(name, age, phoneNumber, email);
        this.salary = salary;
        this.bonus = null;
        this.department = department;
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

    public boolean isEmployedStatus() {
        return employedStatus;
    }

    public void setEmployedStatus(boolean employedStatus) {
        this.employedStatus = employedStatus;
    }

    public Integer getTeamLeadId() {
        return teamLeadId;
    }

    public void setTeamLeadId(Integer teamLeadId) {
        this.teamLeadId = teamLeadId;
    }

    public Integer getCompany_id() {
        return company_id;
    }

    public void setCompany_id(Integer company_id) {
        this.company_id = company_id;
    }

    @Override
    public boolean isEmployed() {
        return employedStatus;
    }

    @Override
    public void resign() {
        this.employedStatus = false;
    }

    @Override
    public String toString() {
        return "Employee{" +
                "id=" + id +
                ", salary=" + salary +
                ", department=" + (department != null ? department.getName() : "No department") +
                ", employedStatus=" + employedStatus +
                ", bonus=" + bonus +
                ", teamLeadId=" + teamLeadId +
                ", company_id=" + company_id +
                "} " + super.toString();
    }
}
