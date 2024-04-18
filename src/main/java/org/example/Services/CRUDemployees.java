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
    public void saveToDatabase(Connection conn, Employee employee) throws SQLException {
        String sql = "INSERT INTO employees (name, age, phone_number, email, salary, department, employed_status) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, employee.getName());
            pstmt.setInt(2,  employee.getAge());
            pstmt.setString(3, employee.getPhoneNumber());
            pstmt.setString(4,  employee.getEmail());
            pstmt.setDouble(5,  employee.getSalary());
            pstmt.setString(6,  employee.getDepartment().name());
            pstmt.setBoolean(7,  employee.isEmployed());
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


}
