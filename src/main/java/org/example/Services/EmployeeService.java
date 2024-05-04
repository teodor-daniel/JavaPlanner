package org.example.Services;

import org.example.Crud.CRUDemployees;
import org.example.Validation.EmployeeValidationService;
import org.example.Models.Employee;

import java.sql.Connection;
import java.util.List;
import java.util.Optional;

public class EmployeeService {
    private final CRUDemployees crudEmployees;
    private final EmployeeValidationService employeeValidator;

    public EmployeeService(CRUDemployees crudEmployees, EmployeeValidationService employeeValidator) {
        this.crudEmployees = crudEmployees;
        this.employeeValidator = employeeValidator;
    }

    public void addEmployee(Connection conn, Employee employee) {
        if (!employeeValidator.validate(employee)) {
            throw new IllegalArgumentException("Invalid employee data");
        }

        crudEmployees.save(conn, employee);
    }

    public List<Employee> getAllEmployees(Connection conn) {
        return crudEmployees.findAll(conn);
    }

    public Optional<Employee> getEmployeeById(Connection conn, Integer id) {
        return crudEmployees.findById(conn, id);
    }

    public void updateEmployee(Connection conn, Employee employee) {
        if (!employeeValidator.validate(employee)) {
            throw new IllegalArgumentException("Invalid employee data");
        }

        crudEmployees.update(conn, employee);
    }

    public void deleteEmployeeById(Connection conn, Integer id) {
        crudEmployees.deleteById(conn, id);
    }
}
