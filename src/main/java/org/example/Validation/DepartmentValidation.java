package org.example.Validation;

import org.example.Interfaces.IValidation;
import org.example.Models.Department;
import org.example.Exceptions.InvalidDepartmentException;

public class DepartmentValidation implements IValidation<Department> {

    @Override
    public boolean validate(Department department) {
        if (department == null) {
            throw new InvalidDepartmentException("Department object cannot be null.");
        }
        validateName(department.getName());
        validateCompanyId(department.getCompanyId());
        return true;
    }

    private void validateName(String name) {
        if (name == null || !name.matches("^[a-zA-Z\\s]+$")) {
            throw new InvalidDepartmentException("Error: Department name is invalid. Names must contain only letters and spaces.");
        }
    }

    private void validateCompanyId(int companyId) {
        if (companyId <= 0) {
            throw new InvalidDepartmentException("Error: Company ID is invalid. It must be greater than 0.");
        }
    }
}
