package org.example.Validation;

import org.example.Interfaces.ValidationService;
import org.example.Models.Employee;

public class EmployeeValidationService implements ValidationService<Employee> {

    @Override
    public boolean validate(Employee employee) {
        return employee != null &&
                validateName(employee.getName()) &&
                validatePhoneNumber(employee.getPhoneNumber()) &&
                validateEmail(employee.getEmail()) &&
                employee.getAge() > 18 &&
                employee.getSalary() > 0 &&
                validateEmploymentStatus(employee.getEmployedStatus());
    }

    private boolean validateName(String name) {
        return name != null && name.matches("^[a-zA-Z\\s]+$");
    }

    private boolean validatePhoneNumber(String phoneNumber) {
        return phoneNumber != null && phoneNumber.matches("^\\d{10,12}$");
    }

    private boolean validateEmail(String email) {
        return email != null && email.matches("^[\\w.%+-]+@[\\w.-]+\\.[a-zA-Z]{2,6}$");
    }

    private boolean validateEmploymentStatus(String status) {
        return status != null && (status.equals("Working") || status.equals("Unemployed") || status.equals("Retired"));
    }
}
