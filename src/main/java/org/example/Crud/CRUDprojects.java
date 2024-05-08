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
        String sql = "INSERT INTO projects (name,status, budget, department_id, lead_id) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, project.getName());
            pstmt.setString(2, project.getStatus());
            pstmt.setDouble(3, project.getBudget());
            pstmt.setInt(4, project.getTeamLead());
            pstmt.setInt(5, project.getDepartmentId());
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
        try (PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                Project project = new Project(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("status"),
                        rs.getDouble("budget"),
                        rs.getInt("department_id"),
                        rs.getInt("lead_id")
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
                        rs.getInt("department_id"),
                        rs.getInt("lead_id")
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
        String sql = "UPDATE projects SET name = ?, status = ?, budget = ?, department_id = ?, lead_id = ? WHERE id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, project.getName());
            pstmt.setString(2, project.getStatus());
            pstmt.setDouble(3, project.getBudget());
            pstmt.setInt(4, project.getDepartmentId());
            pstmt.setInt(5, project.getTeamLead());
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
