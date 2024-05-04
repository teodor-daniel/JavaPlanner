package org.example.Validation;

import org.example.Interfaces.ValidationService;
import org.example.Models.Task;

public class TaskValidationService implements ValidationService<Task> {

    @Override
    public boolean validate(Task task) {
        return task != null &&
                validateName(task.getName()) &&
                task.getAssignedTo() > 0 &&
                task.getProjectId() > 0 &&
                task.getDueDate() != null &&
                validateStatus(task.getStatus());
    }

    private boolean validateName(String name) {
        return name != null && name.matches("^[a-zA-Z\\s]+$");
    }

    private boolean validateStatus(String status) {
        return status != null && (status.equalsIgnoreCase("Pending") || status.equalsIgnoreCase("Completed") || status.equalsIgnoreCase("In Progress")); // Might rework this to use enums
    }
}
