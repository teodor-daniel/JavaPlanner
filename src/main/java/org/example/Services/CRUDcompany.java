package org.example.Services;

import org.example.Models.Company;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CRUDcompany {

    public static void saveToDataBase(Connection conn, Company company) {
        String sql = "INSERT INTO company (name, address, registration_number, phone_number, email) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, company.getName());
            pstmt.setString(2, company.getAddress());
            pstmt.setString(3, company.getRegistrationNumber());
            pstmt.setString(4, company.getPhoneNumber());
            pstmt.setString(5, company.getEmail());
            pstmt.executeUpdate();
            System.out.println("Company saved to database");
        } catch (SQLException e) {
            System.out.println("Error saving company to database");
            e.printStackTrace();
        }
    }

    public static List<Company> loadAllCompanies(Connection conn) {
        List<Company> companies = new ArrayList<>();
        String sql = "SELECT * FROM company";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Company company = new Company(
                        pstmt.getResultSet().getInt("id"),
                        pstmt.getResultSet().getString("name"),
                        pstmt.getResultSet().getString("address"),
                        pstmt.getResultSet().getString("registration_number"),
                        pstmt.getResultSet().getString("phone_number"),
                        pstmt.getResultSet().getString("email")
                );
                companies.add(company);
            }
            System.out.println("All companies loaded");
            return companies;
        } catch (SQLException e) {
            System.out.println("Error loading all companies");
            e.printStackTrace();
        }
        return null;
    }

    public static void deleteCompany(Connection conn, int id) {
        String sql = "DELETE FROM company WHERE id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error deleting company id: " + id);
            e.printStackTrace();
        }
    }

    public static void updateCompany(Connection conn, Company company) {
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

    public static Company findById(Connection conn, Integer id) {
        String sql = "SELECT * FROM company WHERE id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            Company company = new Company(
                    pstmt.getResultSet().getInt("id"),
                    pstmt.getResultSet().getString("name"),
                    pstmt.getResultSet().getString("address"),
                    pstmt.getResultSet().getString("registration_number"),
                    pstmt.getResultSet().getString("phone_number"),
                    pstmt.getResultSet().getString("email")
            );
            return company;
        } catch (SQLException e) {
            System.out.println("Error finding company id: " + id);
            e.printStackTrace();
        }
        return null;
    }
}
