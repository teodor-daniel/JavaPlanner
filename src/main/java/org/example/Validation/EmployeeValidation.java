package org.example.Validation;

import org.example.Enum.EmployeeState;
import org.example.Interfaces.IValidation;
import org.example.Models.Employee;
import org.example.Exceptions.*;

public class EmployeeValidation implements IValidation<Employee> {

    @Override
    public boolean validate(Employee employee) {
        if (employee == null) {
            throw new InvalidEmployeeException("Employee object cannot be null.");
        }
        validateName(employee.getName());
        validatePhoneNumber(employee.getPhoneNumber());
        validateAge(employee.getAge());
        validateSalary(employee.getSalary());
        validateEmail(employee.getEmail());
        validateEmploymentStatus(employee.getEmployedStatus());
        return true;
    }

    private void validateName(String name) {
        if (name == null || !name.matches("^[a-zA-Z\\s]+$")) {
            throw new InvalidNameException("Error: Name is invalid. Names must contain only letters and spaces.");
        }
    }

    private void validatePhoneNumber(String phoneNumber) {
        if (phoneNumber == null || !phoneNumber.matches("^\\d{10,12}$")) {
            throw new InvalidPhoneNumberException("Error: Phone number is invalid. Phone numbers must be 10 to 12 digits long.");
        }
    }

    private void validateEmail(String email) {
        if (email == null || !email.matches("^[a-zA-Z0-9+_.-]+@[a-zA-Z0-9.-]+$")) {
            throw new InvalidEmailException("Error: Email is invalid. Emails must follow a valid format.");
        }
    }

    private void validateAge(int age) {
        if (age < 18) {
            throw new InvalidAgeException("Error: Employee must be older than 18.");
        }
    }

    private void validateSalary(double salary) {
        if (salary <= 0) {
            throw new InvalidSalaryException("Error: Salary must be greater than zero.");
        }
    }

    private void validateEmploymentStatus(String status) {
        if (status == null || (!status.equalsIgnoreCase(EmployeeState.ACTIVE.toString()) && !status.equalsIgnoreCase(EmployeeState.INACTIVE.toString()) && !status.equalsIgnoreCase(EmployeeState.ON_LEAVE.toString()) && !status.equalsIgnoreCase(EmployeeState.RESIGNED.toString()))) {
            throw new InvalidEmploymentStatusException("Error: Employment status is invalid. Valid statuses are 'Working', 'Unemployed', or 'Retired'.");
        }
    }
}
