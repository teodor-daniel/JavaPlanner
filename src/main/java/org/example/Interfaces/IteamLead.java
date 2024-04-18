package org.example.Interfaces;


import org.example.Models.Employee;
import org.example.Models.Task;

public interface IteamLead {

    void fireEmployee(Employee e);
    void hireEmployee(Employee e);
    void assignEmployee(Employee e, Task t);

}
