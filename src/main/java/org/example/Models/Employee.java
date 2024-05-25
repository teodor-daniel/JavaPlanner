package org.example.Models;

public class Employee extends PersonA {
    private int id;
    private double salary;
    private String employedStatus;
    private Integer bonus;
    private Integer companyId;
    private Integer departmentId;
    private Integer managerId;


    public Employee(int id, String name, int age, String phoneNumber, String email, double salary, Integer department, Integer managerId, Integer companyId, String employedStatus) {
        super(name, age, phoneNumber, email);
        this.id = id;
        this.salary = salary;
        this.bonus = null;
        this.departmentId = department;
        this.managerId = managerId;
        this.companyId = companyId;
        this.employedStatus = employedStatus;
    }

    public Employee(int id, String name, int age, String phoneNumber, String email, double salary, Integer department, String employedStatus) {
        super(name, age, phoneNumber, email);
        this.id = id;
        this.salary = salary;
        this.bonus = null;
        this.departmentId = department;
        this.managerId = null;
        this.companyId = null;
        this.employedStatus = employedStatus;
    }

    public Employee(String name, int age, String phoneNumber, String email, double salary, Integer department, Integer managerId, Integer companyId, String employedStatus) {
        super(name, age, phoneNumber, email);
        this.salary = salary;
        this.bonus = null;
        this.departmentId = department;
        this.managerId = managerId;
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

    public Integer getDepartment() {
        return departmentId;
    }

    public void setDepartment(Integer department) {
        this.departmentId = department;
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

    public Integer getManagerId() {
        return managerId;
    }

    public void setManagerId(Integer managerId) {
        this.managerId = managerId;
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    @Override
    public String toString() {
        return "Employee{" +
                "id=" + id +
                ", salary=" + salary +
                ", department=" + departmentId +
                ", employedStatus='" + employedStatus + '\'' +
                ", bonus=" + bonus +
                ", teamLeadId=" + managerId +
                ", companyId=" + companyId +
                "} " + super.toString();
    }
}
