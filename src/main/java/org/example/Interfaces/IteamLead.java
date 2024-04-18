package org.example.Interfaces;


import org.example.Models.Employee;
import org.example.Models.Task;

public interface IteamLead {

    public void fireEmployee(Employee e);
    public void hireEmployee(Employee e);
    public void assignEmployee(Employee e, Task t);

}
