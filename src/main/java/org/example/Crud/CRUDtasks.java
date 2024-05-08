package org.example.Crud;

import org.example.Interfaces.CrudRepository;
import org.example.Models.Task;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CRUDtasks implements CrudRepository<Task, Integer> {

    @Override
    public void save(Connection conn, Task task) {
        String sql = "INSERT INTO tasks (name, description, due_date, status,assigned_to, project_id) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, task.getName());
            pstmt.setString(2, task.getDescription());
            pstmt.setDate(3, task.getDueDate());
            pstmt.setString(4, task.getStatus());
            pstmt.setInt(5, task.getAssignedTo());
            pstmt.setInt(6, task.getProjectId());

            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error saving task to database");
            e.printStackTrace();
        }
    }

    @Override
    public List<Task> findAll(Connection conn) {
        List<Task> tasks = new ArrayList<>();
        String sql = "SELECT * FROM tasks";
        try (PreparedStatement pstmt = conn.prepareStatement(sql); ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                Task task = new Task(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("description"),
                        rs.getDate("due_date"),
                        rs.getString("status"),
                        rs.getInt("assigned_to"),
                        rs.getInt("project_id")
                );
                tasks.add(task);
            }
        } catch (SQLException e) {
            System.out.println("Error loading tasks from database");
            e.printStackTrace();
        }
        return tasks;
    }

    @Override
    public Optional<Task> findById(Connection conn, Integer id) {
        String sql = "SELECT * FROM tasks WHERE id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                Task task = new Task(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("description"),
                        rs.getDate("due_date"),
                        rs.getString("status"),
                        rs.getInt("assigned_to"),
                        rs.getInt("project_id")
                );
                return Optional.of(task);
            }
        } catch (SQLException e) {
            System.out.println("Error finding task by id");
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public void update(Connection conn, Task task) {
        String sql = "UPDATE tasks SET name = ?, description = ?, due_date = ?, status = ?,assigned_to = ?, project_id = ? WHERE id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, task.getName());
            pstmt.setString(2, task.getDescription());
            pstmt.setDate(3, task.getDueDate());
            pstmt.setString(4, task.getStatus());
            pstmt.setInt(5, task.getAssignedTo());
            pstmt.setInt(6, task.getProjectId());
            pstmt.setInt(7, task.getId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error updating task in database");
            e.printStackTrace();
        }
    }

    @Override
    public void deleteById(Connection conn, Integer id) {
        String sql = "DELETE FROM tasks WHERE id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error deleting task from database");
            e.printStackTrace();
        }
    }
}
