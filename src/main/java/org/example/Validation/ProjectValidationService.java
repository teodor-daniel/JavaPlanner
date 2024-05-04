package org.example.Validation;

import org.example.Interfaces.ValidationService;
import org.example.Models.Project;

public class ProjectValidationService implements ValidationService<Project> {

    @Override
    public boolean validate(Project project) {
        return project != null &&
                validateName(project.getName()) &&
                project.getDepartmentId() > 0 &&
                project.getBudget() >= 0.0;
    }

    private boolean validateName(String name) {
        return name != null && name.matches("^[a-zA-Z\\s]+$");
    }
}
