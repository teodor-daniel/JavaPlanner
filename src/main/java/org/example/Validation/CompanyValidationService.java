package org.example.Validation;

import org.example.Interfaces.ValidationService;
import org.example.Models.Company;

public class CompanyValidationService implements ValidationService<Company> {

    @Override
    public boolean validate(Company company) {
        if (company == null) {
            throw new IllegalArgumentException("Company object cannot be null.");
        }
        validateName(company.getName());
        validatePhoneNumber(company.getPhoneNumber());
        validateEmail(company.getEmail());
        return true;
    }

    private void validateName(String name) {
        if (name == null || !name.matches("^[a-zA-Z\\s]+$")) {
            throw new IllegalArgumentException("Error: Name is invalid. Names must contain only letters and spaces.");
        }
    }

    private void  validatePhoneNumber(String phoneNumber) {
        if (phoneNumber == null || !phoneNumber.matches("^\\d{10,12}$")) {
            throw new IllegalArgumentException("Error: Phone number is invalid. Phone numbers must be 10 to 12 digits long.");
        }
    }

    private void validateEmail(String email) {
        if (email == null || !email.matches("^[a-zA-Z0-9+_.-]+@[a-zA-Z0-9.-]+$")) {
            throw new IllegalArgumentException("Error: Email is invalid. Emails must be in the format");
        }
    }

}
