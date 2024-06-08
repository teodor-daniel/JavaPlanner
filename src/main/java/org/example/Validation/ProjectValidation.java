package org.example.Validation;

import org.example.Exceptions.InvalidDepartmentIdException;
import org.example.Exceptions.InvalidEmailException;
import org.example.Exceptions.InvalidProjectException;
import org.example.Exceptions.InvalidSalaryException;
import org.example.Interfaces.IValidation;
import org.example.Models.Project;

public class ProjectValidation implements IValidation<Project> {

    @Override
    public boolean validate(Project project) {
        if (project == null) {
            throw new InvalidProjectException("Project object cannot be null.");
        }
        validateName(project.getName());
        validateDepartmentId(project.getDepartmentId());
        validateBudget(project.getBudget());
        return true;
    }

    private void validateName(String name) {
        if (name == null || !name.matches("^[a-zA-Z\\s]+$")) {
            throw new InvalidEmailException("Error: Project name is invalid. Names must contain only letters and spaces.");
        }
    }

    private void validateDepartmentId(int departmentId) {
        if (departmentId <= 0) {
            throw new InvalidDepartmentIdException("Error: Department ID is invalid. It must be greater than 0.");
        }
    }

    private void validateBudget(double budget) {
        if (budget < 0) {
            throw new InvalidSalaryException("Error: Budget cannot be negative.");
        }
    }

    private void validateStatus(String status) {
        if (status == null || (!status.equals("Not Started") && !status.equals("In Progress") && !status.equals("Completed"))) {
            throw new InvalidProjectException("Error: Project status is invalid. It must be one of the following: Not Started, In Progress, Completed.");
        }
    }

}
