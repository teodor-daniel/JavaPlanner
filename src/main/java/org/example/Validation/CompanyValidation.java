package org.example.Validation;

import org.example.Interfaces.IValidation;
import org.example.Models.Company;
import org.example.Exceptions.InvalidNameException;
import org.example.Exceptions.InvalidPhoneNumberException;
import org.example.Exceptions.InvalidEmailException;

public class CompanyValidation implements IValidation<Company> {
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
        if (name == null || name.trim().isEmpty()) {
            throw new InvalidNameException("Error: Name cannot be empty.");
        }
        if (name.length() < 2 || name.length() > 50) {
            throw new InvalidNameException("Error: Name must be between 2 and 50 characters long.");
        }
        if (!name.matches("^[a-zA-Z\\s]+$")) {
            throw new InvalidNameException("Error: Name is invalid. Names must contain only letters and spaces.");
        }
    }

    private void validatePhoneNumber(String phoneNumber) {
        if (phoneNumber == null || !phoneNumber.matches("^\\+?[1-9]\\d{1,14}$")) {
            throw new InvalidPhoneNumberException("Error: Phone number is invalid. Phone numbers must start with a country code and be up to 15 digits long.");
        }
    }

    private void validateEmail(String email) {
        if (email == null || !email.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")) {
            throw new InvalidEmailException("Error: Email is invalid. Emails must follow a valid format and include a domain.");
        }
    }

}
