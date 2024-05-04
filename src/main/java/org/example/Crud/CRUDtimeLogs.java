package org.example.Crud;

import org.example.Interfaces.CrudRepository;
import org.example.Models.TimeLog;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CRUDtimeLogs implements CrudRepository<TimeLog, Integer> {

    @Override
    public void save(Connection conn, TimeLog timeLog) {
        String sql = "INSERT INTO timelogs (task_id, employee_id, hours_logged, log_date, description) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, timeLog.getTaskId());
            pstmt.setInt(2, timeLog.getEmployeeId());
            pstmt.setDouble(3, timeLog.getHoursLogged());
            pstmt.setDate(4, timeLog.getLogDate());
            pstmt.setString(5, timeLog.getDescription());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error saving time log to database");
            e.printStackTrace();
        }
    }

    @Override
    public List<TimeLog> findAll(Connection conn) {
        List<TimeLog> timeLogs = new ArrayList<>();
        String sql = "SELECT * FROM timelogs";
        try (PreparedStatement pstmt = conn.prepareStatement(sql); ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                TimeLog timeLog = new TimeLog(
                        rs.getInt("id"),
                        rs.getInt("task_id"),
                        rs.getInt("employee_id"),
                        rs.getDouble("hours_logged"),
                        rs.getDate("log_date"),
                        rs.getString("description")
                );
                timeLogs.add(timeLog);
            }
        } catch (SQLException e) {
            System.out.println("Error loading time logs from database");
            e.printStackTrace();
        }
        return timeLogs;
    }

    @Override
    public Optional<TimeLog> findById(Connection conn, Integer id) {
        String sql = "SELECT * FROM timelogs WHERE id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                TimeLog timeLog = new TimeLog(
                        rs.getInt("id"),
                        rs.getInt("task_id"),
                        rs.getInt("employee_id"),
                        rs.getDouble("hours_logged"),
                        rs.getDate("log_date"),
                        rs.getString("description")
                );
                return Optional.of(timeLog);
            }
        } catch (SQLException e) {
            System.out.println("Error finding time log by id");
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public void update(Connection conn, TimeLog timeLog) {
        String sql = "UPDATE timelogs SET task_id = ?, employee_id = ?, hours_logged = ?, log_date = ?, description = ? WHERE id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, timeLog.getTaskId());
            pstmt.setInt(2, timeLog.getEmployeeId());
            pstmt.setDouble(3, timeLog.getHoursLogged());
            pstmt.setDate(4, timeLog.getLogDate());
            pstmt.setString(5, timeLog.getDescription());
            pstmt.setInt(6, timeLog.getId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error updating time log in database");
            e.printStackTrace();
        }
    }

    @Override
    public void deleteById(Connection conn, Integer id) {
        String sql = "DELETE FROM timelogs WHERE id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error deleting time log from database");
            e.printStackTrace();
        }
    }
}
