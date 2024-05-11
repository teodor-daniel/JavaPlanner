package org.example.Validation;

import org.example.Interfaces.IValidation;
import org.example.Models.Project;
import org.example.Exceptions.*;

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
}
