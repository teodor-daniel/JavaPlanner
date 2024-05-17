package org.example.DataBase;

import org.example.Private.Sensitive;

import java.sql.Connection;
import java.sql.DriverManager;

public class DatabaseConnection {
    private static DatabaseConnection instance;
    private final Connection conn;

    private DatabaseConnection() {
        try {
            Class.forName("org.postgresql.Driver");
            String url = Sensitive.URL;
            String user = Sensitive.USER;
            String password = Sensitive.PASSWORD;
            conn = DriverManager.getConnection(url, user, password);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error connecting to the database: " + e.getMessage());

        }
    }

    public static synchronized DatabaseConnection getInstance() {
        if (instance == null) {
            instance = new DatabaseConnection();
        }
        return instance;
    }

    public Connection getConnection() {
        return conn;
    }
}
