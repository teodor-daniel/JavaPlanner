package org.example.Services;

import org.example.Models.Employee;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CRUDemployees {
    //Save to
    public static void saveToDatabase(Connection conn, Employee employee) throws SQLException {
        String sql = "INSERT INTO employees (name, age, phone_number, email, salary, department, employed_status) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, employee.getName());
            pstmt.setInt(2, employee.getAge());
            pstmt.setString(3, employee.getPhoneNumber());
            pstmt.setString(4, employee.getEmail());
            pstmt.setDouble(5, employee.getSalary());
            pstmt.setString(6, employee.getDepartment().name());
            pstmt.setBoolean(7, employee.isEmployed());
            pstmt.executeUpdate();
        }
    }

    public static List<Employee> loadAllEmployees(Connection conn) throws SQLException {
        List<Employee> employees = new ArrayList<>();
        String sql = "SELECT * FROM employees";
        try (PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                Employee employee = new Employee(
                        rs.getString("name"),
                        rs.getInt("age"),
                        rs.getString("phone_number"),
                        rs.getString("email"),
                        rs.getDouble("salary"),
                        rs.getString("department")
                );
                employee.setEmployedStatus(rs.getBoolean("employed_status"));

                employees.add(employee);
            }
        }
        return employees;
    }

    public static void showAllEmployees(Connection conn) throws SQLException {
        List<Employee> employees = new ArrayList<>();
        String sql = "SELECT * FROM employees";
        try (PreparedStatement psmt = conn.prepareStatement(sql);
             ResultSet rs = psmt.executeQuery()) {
            while (rs.next()) {
                Employee employee = new Employee(
                        rs.getString("name"),
                        rs.getInt("age"),
                        rs.getString("phone_number"),
                        rs.getString("email"),
                        rs.getDouble("salary"),
                        rs.getString("department")
                );
                employee.setEmployedStatus(rs.getBoolean("employed_status"));
                employees.add(employee);
            }
        }
        System.out.println(employees);
    }

    public static Employee findById(Connection conn, Integer id) throws SQLException {
        String sql = "SELECT * FROM employees WHERE id = ?";
        System.out.println("Searching for employee with id: " + id);
        try (PreparedStatement pstm = conn.prepareStatement(sql)) {
            pstm.setInt(1, id);
            try (ResultSet rs = pstm.executeQuery()) {
                if (rs.next()) {
                    Employee employee = new Employee(
                            rs.getString("name"),
                            rs.getInt("age"),
                            rs.getString("phone_number"),
                            rs.getString("email"),
                            rs.getDouble("salary"),
                            rs.getString("department")
                    );
                    employee.setEmployedStatus(rs.getBoolean("employed_status"));
                    return employee;
                }
            }
        }
        System.out.println("Not found");
        return null;
    }

    //update the employee with the new employee data in the database using the id of the employee
    public static void updateEmployee(Connection conn, Integer id, Employee newEmployee) throws SQLException{
        Employee currentEmployee = findById(conn, id);
        if (currentEmployee == null) {
            System.out.println("Employee not found");
            return;
        }
        System.out.println("Employee found");
        System.out.println(newEmployee);
        System.out.println(currentEmployee);
        String sql = "UPDATE employees SET name = ?, age = ?, phone_number = ?, email = ?, salary = ?, department = ?, employed_status = ? WHERE id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, newEmployee.getName());
            pstmt.setInt(2, newEmployee.getAge());
            pstmt.setString(3, newEmployee.getPhoneNumber());
            pstmt.setString(4, newEmployee.getEmail());
            pstmt.setDouble(5, newEmployee.getSalary());
            pstmt.setString(6, newEmployee.getDepartment().name());
            pstmt.setBoolean(7, newEmployee.isEmployed());
            pstmt.setInt(8, id);
            pstmt.executeUpdate();
        }
    }
    //Delete employee from the database using the id of the employee
    public static void deleteEmployee(Connection conn, Integer id) throws SQLException {
        String sql = "DELETE FROM employees WHERE id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        }
    }


}
