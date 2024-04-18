package org.example.Services;



import org.example.Models.Employee;

import java.util.Comparator;

public class SalaryComparator implements Comparator<Employee> {
    //sort ascending order
    @Override
    public int compare(Employee e1, Employee e2) {
        return Double.compare(e1.getSalary(), e2.getSalary());
    }
}