package org.example.Models;


import org.example.Enum.Departments;
import org.example.Interfaces.Itask;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

public class Task extends Operation implements Itask {

    private boolean state = false; //when a task is created it is false by default

    private Departments department;

    private final int id;

    private ArrayList<Employee> assignedEmployees;
    private static int taskId = 0;//to be removed when i add the database.
    public Task(String name, Date startDate, Date endDate, Date deadline, String department) {
        super(name, startDate, endDate, deadline);
        setDepartment(department);
        this.assignedEmployees = new ArrayList<>();
        this.id = taskId++;
    }


    public int getId() {
        return id;
    }

    public static int getTaskId() {
        return taskId;
    }

    public Task(String name, Date deadLine, String department) {
        super(name, deadLine);
        setDepartment(department);
        this.id = taskId++;
    }

    public boolean isState() {
        return state;
    }

    public void setState(boolean state) {
        this.state = state;
    }

    public Departments getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        try {
            this.department = Departments.valueOf(department.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid department");
        }
    }

    public ArrayList<Employee> getAssignedEmployees() {
        return assignedEmployees;
    }

    public void setAssignedEmployees(ArrayList<Employee> assignedEmployees) {
        this.assignedEmployees = assignedEmployees;
    }

    public void addAssignedEmployees(Employee employee){
        this.assignedEmployees.add(employee);
    }

    public void removeAssignedEmployees(Employee employee){
        if(this.assignedEmployees.contains(employee)){
            this.assignedEmployees.remove(employee);
        }else{
            throw new IllegalArgumentException("This employee does not exist");
        }
    }
    @Override
    public void completed() {
        if(this.state == true){
            Date currentDate = Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant());
            this.setEndDate(currentDate);

        }
    }

    @Override
    public boolean isUrgent() {
        if(this.state){ //if the task is completed it is not urgent
            return false;
        }
        Date currentDate = Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant());//convert currentDate
        System.out.println(currentDate);
        System.out.println(this.getDeadline());
        if (this.getDeadline() != null && this.getDeadline().after(currentDate)) {
            return true;
        }
        return false;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return state == task.state && department == task.department; //i do not check the id of the hashcode.
    }

    @Override
    public int hashCode() {
        return Objects.hash(id); //hash only after the id.
    }

    @Override
    public String toString() {
        return "Task{" +
                "state=" + state +
                ", department=" + department +
                ", id=" + id +  " " +
                 super.toString();

    }
}
