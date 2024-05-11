package org.example.Crud;

import org.example.Interfaces.ICrudRepository;
import org.example.Models.Department;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CRUDdepartments implements ICrudRepository<Department, Integer> {

    @Override
    public void save(Connection conn, Department department) {
        String sql = "INSERT INTO departments (name, company_id, manager_id) VALUES (?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, department.getName());
            pstmt.setInt(2, department.getCompanyId());
            if (department.getManagerId() != null) {
                pstmt.setInt(3, department.getManagerId());
            } else {
                pstmt.setNull(3, java.sql.Types.INTEGER);
            }
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error saving department to database");
            e.printStackTrace();
        }
    }

    @Override
    public List<Department> findAll(Connection conn) {
        List<Department> departments = new ArrayList<>();
        String sql = "SELECT * FROM departments";
        try (PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                Department department = new Department(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getInt("company_id"),
                        (Integer) rs.getObject("manager_id")
                );
                departments.add(department);
            }
        } catch (SQLException e) {
            System.out.println("Error loading departments from database");
            e.printStackTrace();
        }
        return departments;
    }

    @Override
    public Optional<Department> findById(Connection conn, Integer id) {
        String sql = "SELECT * FROM departments WHERE id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                Department department = new Department(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getInt("company_id"),
                        (Integer) rs.getObject("manager_id")
                );
                return Optional.of(department);
            }
        } catch (SQLException e) {
            System.out.println("Error finding department by id");
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public void update(Connection conn, Department department) {
        String sql = "UPDATE departments SET name = ?, company_id = ?, manager_id = ? WHERE id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, department.getName());
            pstmt.setInt(2, department.getCompanyId());
            if (department.getManagerId() != null) {
                pstmt.setInt(3, department.getManagerId());
            } else {
                pstmt.setNull(3, java.sql.Types.INTEGER);
            }
            pstmt.setInt(4, department.getId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error updating department in database");
            e.printStackTrace();
        }
    }

    @Override
    public void deleteById(Connection conn, Integer id) {
        String sql = "DELETE FROM departments WHERE id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error deleting department from database");
            e.printStackTrace();
        }
    }
}
