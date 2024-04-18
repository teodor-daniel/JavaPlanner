package org.example.Models;


import org.example.Interfaces.IteamLead;

import java.util.ArrayList;

public final class TeamLead extends Employee implements IteamLead {
    private ArrayList<Employee> subordinates;
    public TeamLead(String name, int age, String phoneNumber, String email, double salary, String department, Integer Bonus) {
        super( name, age, phoneNumber, email, salary, department);
        super.setBonus(Bonus);
        this.subordinates = new ArrayList<>();
    }
    public ArrayList<Employee> getSubordinates() {
        return subordinates;
    }

    public void setSubordinates(ArrayList<Employee> subordinates) {
        this.subordinates = subordinates;
    }

    @Override
    public void fireEmployee(Employee e) {
        if(this.subordinates.contains(e)){
            this.subordinates.remove(e);
            e.resign();
        }else{
            throw new IllegalArgumentException("This employee does not exist");
        }
    }

    @Override
    public void hireEmployee(Employee e) {
        if(e == null){
            throw  new IllegalArgumentException("Employee is null");
        }
        this.subordinates.add(e);
    }

    @Override
    public void assignEmployee(Employee e, Task t) {
        if(t != null && e != null){
            t.addAssignedEmployees(e);
        }else{
            throw new IllegalArgumentException("Employee and task must not be null");
        }
    }

    @Override
    public String toString() {
        return "TeamLead{" +
                " yearlyBonus=" + getBonus() +
                ", subordionates=" + subordinates + super.toString() +
                '}';
    }
}
