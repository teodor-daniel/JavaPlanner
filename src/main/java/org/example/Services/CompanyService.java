package org.example.Services;

import org.example.Crud.CRUDcompany;
import org.example.Validation.CompanyValidationService;
import org.example.Models.Company;

import java.sql.Connection;
import java.util.List;
import java.util.Optional;

public class CompanyService {
    private final CRUDcompany crudCompany;
    private final CompanyValidationService companyValidator;

    public CompanyService(CRUDcompany crudCompany, CompanyValidationService companyValidator) {
        this.crudCompany = crudCompany;
        this.companyValidator = companyValidator;
    }

    public void addCompany(Connection conn, Company company) {
        if (!companyValidator.validate(company)) {
            throw new IllegalArgumentException("Invalid company data");
        }

        crudCompany.save(conn, company);
    }

    public List<Company> getAllCompanies(Connection conn) {
        return crudCompany.findAll(conn);
    }

    public Optional<Company> getCompanyById(Connection conn, Integer id) {
        return crudCompany.findById(conn, id);
    }

    public void updateCompany(Connection conn, Company company) {
        if (!companyValidator.validate(company)) {
            throw new IllegalArgumentException("Invalid company data");
        }

        crudCompany.update(conn, company);
    }

    public void deleteCompanyById(Connection conn, Integer id) {
        crudCompany.deleteById(conn, id);
    }
}
