package org.example.Validation;

import org.example.Enum.ActivityState;
import org.example.Exceptions.*;
import org.example.Interfaces.IValidation;
import org.example.Models.Task;

public class TaskValidation implements IValidation<Task> {

    @Override
    public boolean validate(Task task) {
        if (task == null) {
            throw new InvalidTaskException("Task object cannot be null.");
        }
        validateName(task.getName());
        validateAssignedTo(task.getAssignedTo());
        validateProjectId(task.getProjectId());
        validateStatus(task.getStatus());
        return true;
    }

    private void validateName(String name) {
        if (name == null || !name.matches("^[a-zA-Z\\s]+$")) {
            throw new InvalidNameException("Error: Task name is invalid. Names must contain only letters and spaces.");
        }
    }

    private void validateAssignedTo(int assignedTo) {
        if (assignedTo <= 0) {
            throw new InvalidTaskAssignmentException("Error: Assigned to field is invalid. It must be a positive integer.");
        }
    }

    private void validateProjectId(int projectId) {
        if (projectId <= 0) {
            throw new InvalidProjectIdException("Error: Project ID is invalid. It must be a positive integer.");
        }
    }

    private void validateStatus(String status) {
        try {
            ActivityState.valueOf(status.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new InvalidTaskStatusException("Error: Status is invalid. Valid statuses are 'IN_PROGRESS', 'COMPLETED', or 'NOT_STARTED'.");
        }
    }
}
