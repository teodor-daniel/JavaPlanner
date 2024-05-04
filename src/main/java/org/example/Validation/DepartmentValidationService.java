package org.example.Validation;

import org.example.Interfaces.ValidationService;
import org.example.Models.Department;

public class DepartmentValidationService implements ValidationService<Department> {

    @Override
    public boolean validate(Department department) {
        return department != null &&
                validateName(department.getName()) &&
                department.getCompanyId() > 0; // To do refactor this check
    }

    private boolean validateName(String name) {
        return name != null && name.matches("^[a-zA-Z\\s]+$");
    }
}
