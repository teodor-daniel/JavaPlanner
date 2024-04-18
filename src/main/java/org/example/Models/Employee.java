package org.example.Models;


import org.example.Enum.Departments;
import org.example.Interfaces.Iemployee;

public class Employee extends Person implements Iemployee {

    private double salary;

    private Departments department;
    private boolean employedStatus = true;
    private final int id;
    private static int employeeId = 0; //to be removed when I add the database.
    public Employee(String name, int age, String phoneNumber, String email, double salary, String department) {
        super(name, age, phoneNumber, email);
        this.salary = salary;
        setDepartment(department);
        this.id = employeeId++;
    }

    public int getCurrentEmployeeNumber(){
        return employeeId;
    }
    public int getId(){
        return this.id;
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
                ", id=" + id + '\'' +
                ", department='" + department + '\'' +
                ", status=" + this.employedStatus +
                '\''  +  super.toString();

    }
}
