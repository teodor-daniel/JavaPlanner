package org.example.Services;

import org.example.Interfaces.CrudRepository;
import org.example.Models.Company;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CRUDcompany implements CrudRepository<Company, Integer> {

    @Override
    public void save(Connection conn, Company company) {
        String sql = "INSERT INTO company (name, address, registration_number, phone_number, email) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, company.getName());
            pstmt.setString(2, company.getAddress());
            pstmt.setString(3, company.getRegistrationNumber());
            pstmt.setString(4, company.getPhoneNumber());
            pstmt.setString(5, company.getEmail());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error saving company to database");
            e.printStackTrace();
        }
    }

    @Override
    public void update(Connection conn, Company company) {
        String sql = "UPDATE company SET name = ?, address = ?, registration_number = ?, phone_number = ?, email = ? WHERE id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, company.getName());
            pstmt.setString(2, company.getAddress());
            pstmt.setString(3, company.getRegistrationNumber());
            pstmt.setString(4, company.getPhoneNumber());
            pstmt.setString(5, company.getEmail());
            pstmt.setInt(6, company.getId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error updating company id: " + company.getId());
            e.printStackTrace();
        }
    }

    @Override
    public void deleteById(Connection conn, Integer id) {
        String sql = "DELETE FROM company WHERE id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error deleting company id: " + id);
            e.printStackTrace();
        }
    }

    @Override
    public Optional<Company> findById(Connection conn, Integer id) {
        String sql = "SELECT * FROM company WHERE id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                Company company = new Company(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("address"),
                        rs.getString("registration_number"),
                        rs.getString("phone_number"),
                        rs.getString("email")
                );
                return Optional.of(company);
            }
        } catch (SQLException e) {
            System.out.println("Error finding company id: " + id);
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public List<Company> findAll(Connection conn) {
        List<Company> companies = new ArrayList<>();
        String sql = "SELECT * FROM company";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Company company = new Company(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("address"),
                        rs.getString("registration_number"),
                        rs.getString("phone_number"),
                        rs.getString("email")
                );
                companies.add(company);
            }
        } catch (SQLException e) {
            System.out.println("Error loading all companies");
            e.printStackTrace();
        }
        return companies;
    }
}
