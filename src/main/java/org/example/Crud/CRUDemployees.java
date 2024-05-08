package org.example.Crud;

import org.example.Interfaces.CrudRepository;
import org.example.Models.Department;
import org.example.Models.Employee;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CRUDemployees implements CrudRepository<Employee, Integer> {

    public void save(Connection conn, Employee employee) {
        String sql = "INSERT INTO employees (name , age , phone_number, email, salary, department_id, employed_status, team_lead_id, company_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?,?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, employee.getName());
            pstmt.setInt(2, employee.getAge());
            pstmt.setString(3, employee.getPhoneNumber());
            pstmt.setString(4, employee.getEmail());
            pstmt.setDouble(5, employee.getSalary());
            pstmt.setInt(6, employee.getDepartment().getId());
            pstmt.setString(7, employee.getEmployedStatus());
            pstmt.setObject(8, employee.getTeamLeadId(), java.sql.Types.INTEGER);
            pstmt.setInt(9, employee.getCompanyId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error saving employee to the database");
            e.printStackTrace();
        }
    }


    @Override
    public List<Employee> findAll(Connection conn) {
        List<Employee> employees = new ArrayList<>();
        String sql = "SELECT * FROM employees";
        try (PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                Department department = new Department(rs.getInt("department_id"), null, 0);
                Employee employee = new Employee(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getInt("age"),
                        rs.getString("phone_number"),
                        rs.getString("email"),
                        rs.getDouble("salary"),
                        department,
                        rs.getInt("team_lead_id"),
                        rs.getInt("company_id"),
                        rs.getString("employed_status")
                );
                employees.add(employee);
            }
        } catch (SQLException e) {
            System.out.println("Error loading employees from the database");
            e.printStackTrace();
        }
        return employees;
    }

    @Override
    public Optional<Employee> findById(Connection conn, Integer id) {
        String sql = "SELECT * FROM employees WHERE id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                Department department = new Department(rs.getInt("department_id"), null, 0);
                Employee employee = new Employee(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getInt("age"),
                        rs.getString("phone_number"),
                        rs.getString("email"),
                        rs.getDouble("salary"),
                        department,
                        rs.getInt("team_lead_id"),
                        rs.getInt("company_id"),
                        rs.getString("employed_status")
                );
                return Optional.of(employee);
            }
        } catch (SQLException e) {
            System.out.println("Error finding employee by ID");
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public void update(Connection conn, Employee employee) {
        // Updated SQL query to include teamLeadId and companyId
        String sql = "UPDATE employees SET name = ?, age = ?, phone_number = ?, email = ?, salary = ?, department_id = ?, employed_status = ?, team_lead_id = ?, company_id = ? WHERE id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, employee.getName());
            pstmt.setInt(2, employee.getAge());
            pstmt.setString(3, employee.getPhoneNumber());
            pstmt.setString(4, employee.getEmail());
            pstmt.setDouble(5, employee.getSalary());
            pstmt.setInt(6, employee.getDepartment().getId());
            pstmt.setString(7, employee.getEmployedStatus());
            pstmt.setObject(8, employee.getTeamLeadId(), java.sql.Types.INTEGER);
            pstmt.setInt(9, employee.getCompanyId());
            pstmt.setInt(10, employee.getId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error updating employee in the database");
            e.printStackTrace();
        }
    }

    @Override
    public void deleteById(Connection conn, Integer id) {
        String sql = "DELETE FROM employees WHERE id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error deleting employee from the database");
            e.printStackTrace();
        }
    }
}
