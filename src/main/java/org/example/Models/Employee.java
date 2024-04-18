package org.example.Models;


import org.example.Enum.Departments;
import org.example.Interfaces.Iemployee;

public class Employee extends Person implements Iemployee {

    private double salary;

    private Departments department;
    private boolean employedStatus = true;
    private Integer bonus;
    public Employee(String name, int age, String phoneNumber, String email, double salary, String department) {
        super(name, age, phoneNumber, email);
        this.salary = salary;
        this.bonus = null;
        setDepartment(department);
    }

    public double getSalary() {
        return salary;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    public Departments getDepartment() {
        return this.department;
    }

    public Integer getBonus() {
        return bonus;
    }

    public void setBonus(Integer bonus) {
        this.bonus = bonus;
    }

    public void setDepartment(String department) {
        try {
            this.department = Departments.valueOf(department.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid department");
        }
    }

    public boolean isEmployedStatus() {
        return employedStatus;
    }

    public void setEmployedStatus(boolean employedStatus) {
        this.employedStatus = employedStatus;
    }

    @Override
    public boolean isEmployed() {
        return this.employedStatus;
    }

    @Override
    public void resign() {
        this.employedStatus = false;
    }

    @Override
    public String toString() {
        return
                " salary=" + salary + '\'' +
                ", department='" + department + '\'' +
                ", status=" + this.employedStatus +
                '\''  +  super.toString();

    }
}
