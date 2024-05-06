package org.example.SwingComponents;

import org.example.Private.Sensitive;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.List;

public class MainPage extends JFrame {
    private Connection conn;

    public MainPage() {
        initDBConnection();
        setTitle("Database Viewer");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(1, 1));

        JButton companyButton = new JButton("Company");
        companyButton.addActionListener(new CompanyButtonClickListener());
        add(companyButton);
    }

    private void initDBConnection() {
        try {
            Class.forName("org.postgresql.Driver");

            String url = Sensitive.URL;
            String user = Sensitive.USER;
            String password = Sensitive.PASSWORD;

            conn = DriverManager.getConnection(url, user, password);
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error connecting to the database: " + e.getMessage(),
                    "Database Connection Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private class CompanyButtonClickListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            new CompanyScreen(conn, MainPage.this).setVisible(true);
            MainPage.this.setVisible(false);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MainPage mainPage = new MainPage();
            mainPage.setVisible(true);
        });
    }
}