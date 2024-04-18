package org.example.Services;

import org.example.Models.Task;

import java.util.Comparator;

public class TaskEmployeeCountComparator implements Comparator<Task> {

    @Override
    public int compare(Task t1, Task t2) {
        return Integer.compare(t1.getAssignedEmployees().size(), t2.getAssignedEmployees().size());
    }
}