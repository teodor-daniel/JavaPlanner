package org.example.Models;


import org.example.Interfaces.IteamLead;

import java.util.ArrayList;

public final class TeamLead extends Employee implements IteamLead {
    private ArrayList<Employee> subordinates;

    public TeamLead(int id, String name, int age, String phoneNumber, String email, double salary, String department, Integer Bonus, Integer team_lead_id,Integer company_id) {
        super(id,name, age, phoneNumber, email, salary, department, team_lead_id, company_id);
    }
    public TeamLead(int id ,String name, int age, String phoneNumber, String email, double salary, String department, Integer Bonus) {
        super(id,name, age, phoneNumber, email, salary, department);
        super.setBonus(Bonus);
        this.subordinates = new ArrayList<>();
    }
    public TeamLead(String name, int age, String phoneNumber, String email, double salary, String department, Integer Bonus) {
        super(name, age, phoneNumber, email, salary, department);
        super.setBonus(Bonus);
        this.subordinates = new ArrayList<>();
    }

    public static boolean isTeamLead(Employee e){
        if(e.getTeamLeadId() == e.getId()){
            return true;
        }
        return false;
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
        e.setTeamLeadId(this.getId());
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
