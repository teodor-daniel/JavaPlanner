package org.example.Crud;

import org.example.Interfaces.CrudRepository;
import org.example.Models.Project;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CRUDprojects implements CrudRepository<Project, Integer> {

    @Override
    public void save(Connection conn, Project project) {
        String sql = "INSERT INTO projects (name, lead_id, status, department_id, budget) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, project.getName());
            pstmt.setInt(2, project.getDepartmentId()); // Assuming lead_id corresponds to the department head or similar
            pstmt.setString(3, project.getStatus());
            pstmt.setInt(4, project.getDepartmentId());
            pstmt.setDouble(5, project.getBudget());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error saving project to database");
            e.printStackTrace();
        }
    }

    @Override
    public List<Project> findAll(Connection conn) {
        List<Project> projects = new ArrayList<>();
        String sql = "SELECT * FROM projects";
        try (PreparedStatement pstmt = conn.prepareStatement(sql); ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                Project project = new Project(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("status"),
                        rs.getDouble("budget"),
                        rs.getInt("department_id")
                );
                projects.add(project);
            }
        } catch (SQLException e) {
            System.out.println("Error loading projects from database");
            e.printStackTrace();
        }
        return projects;
    }

    @Override
    public Optional<Project> findById(Connection conn, Integer id) {
        String sql = "SELECT * FROM projects WHERE id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                Project project = new Project(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("status"),
                        rs.getDouble("budget"),
                        rs.getInt("department_id")
                );
                return Optional.of(project);
            }
        } catch (SQLException e) {
            System.out.println("Error finding project by id");
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public void update(Connection conn, Project project) {
        String sql = "UPDATE projects SET name = ?, lead_id = ?, status = ?, department_id = ?, budget = ? WHERE id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, project.getName());
            pstmt.setInt(2, project.getDepartmentId()); // Update this if lead_id should be different
            pstmt.setString(3, project.getStatus());
            pstmt.setInt(4, project.getDepartmentId());
            pstmt.setDouble(5, project.getBudget());
            pstmt.setInt(6, project.getId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error updating project in database");
            e.printStackTrace();
        }
    }

    @Override
    public void deleteById(Connection conn, Integer id) {
        String sql = "DELETE FROM projects WHERE id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error deleting project from database");
            e.printStackTrace();
        }
    }
}
