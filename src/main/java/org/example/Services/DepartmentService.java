package org.example.Services;

import org.example.Crud.CRUDdepartments;
import org.example.Validation.DepartmentValidationService;
import org.example.Models.Department;

import java.sql.Connection;
import java.util.List;
import java.util.Optional;

public class DepartmentService {
    private final CRUDdepartments crudDepartments;
    private final DepartmentValidationService departmentValidator;

    public DepartmentService(CRUDdepartments crudDepartments, DepartmentValidationService departmentValidator) {
        this.crudDepartments = crudDepartments;
        this.departmentValidator = departmentValidator;
    }

    public void addDepartment(Connection conn, Department department) {
        if (!departmentValidator.validate(department)) {
            throw new IllegalArgumentException("Invalid department data");
        }

        crudDepartments.save(conn, department);
    }

    public List<Department> getAllDepartments(Connection conn) {
        return crudDepartments.findAll(conn);
    }

    public Optional<Department> getDepartmentById(Connection conn, Integer id) {
        return crudDepartments.findById(conn, id);
    }

    public void updateDepartment(Connection conn, Department department) {
        if (!departmentValidator.validate(department)) {
            throw new IllegalArgumentException("Invalid department data");
        }

        crudDepartments.update(conn, department);
    }

    public void deleteDepartmentById(Connection conn, Integer id) {
        crudDepartments.deleteById(conn, id);
    }
}
