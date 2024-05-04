package org.example.Validation;

import org.example.Interfaces.ValidationService;
import org.example.Models.Company;

public class CompanyValidationService implements ValidationService<Company> {

    @Override
    public boolean validate(Company company) {
        return company != null &&
                validateName(company.getName()) &&
                validatePhoneNumber(company.getPhoneNumber()) &&
                validateEmail(company.getEmail());
    }

    private boolean validateName(String name) {
        return name != null && name.matches("^[a-zA-Z\\s]+$");
    }

    private boolean validatePhoneNumber(String phoneNumber) {
        return phoneNumber != null && phoneNumber.matches("^\\d{10,15}$");
    }

    private boolean validateEmail(String email) {
        return email != null && email.matches("^[\\w.%+-]+@[\\w.-]+\\.[a-zA-Z]{2,6}$");
    }
}
