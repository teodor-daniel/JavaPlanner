package org.example.SwingComponents;

import org.example.Private.Sensitive;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;

public class MainPage extends JFrame {
    private Connection conn;

    public MainPage() {
        initDBConnection();
        setTitle("Database Management");
        setSize(600, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel navbarPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));

        Dimension buttonSize = new Dimension(150, 30);

        JButton companyButton = new JButton("Company");
        companyButton.setPreferredSize(buttonSize);
        companyButton.addActionListener(new CompanyButtonClickListener());
        navbarPanel.add(companyButton);

        JButton departmentButton = new JButton("Department");
        departmentButton.setPreferredSize(buttonSize);
        departmentButton.addActionListener(new DepartmentButtonClickListener());
        navbarPanel.add(departmentButton);

        JButton employeeButton = new JButton("Employee");
        employeeButton.setPreferredSize(buttonSize);
        employeeButton.addActionListener(new EmployeeButtonClickListener());
        navbarPanel.add(employeeButton);

        JButton projectButton = new JButton("Project");
        projectButton.setPreferredSize(buttonSize);
        projectButton.addActionListener(new ProjectButtonClickListener());
        navbarPanel.add(projectButton);

        JButton taskButton = new JButton("Task");
        taskButton.setPreferredSize(buttonSize);
        taskButton.addActionListener(new TaskButtonClickListener());
        navbarPanel.add(taskButton);

        JButton timelogButton = new JButton("Time Log");
        timelogButton.setPreferredSize(buttonSize);
        timelogButton.addActionListener(new TimeLogButtonClickListener());
        navbarPanel.add(timelogButton);

        add(navbarPanel);
    }

    // Initialize the database connection
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

    private class DepartmentButtonClickListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            new DepartmentScreen(conn, MainPage.this).setVisible(true);
            MainPage.this.setVisible(false);
        }
    }

    private class EmployeeButtonClickListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            new EmployeeScreen(conn, MainPage.this).setVisible(true);
            MainPage.this.setVisible(false);
        }
    }

    private class ProjectButtonClickListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            new ProjectScreen(conn, MainPage.this).setVisible(true);
            MainPage.this.setVisible(false);
        }
    }

    private class TaskButtonClickListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            new TaskScreen(conn, MainPage.this).setVisible(true);
            MainPage.this.setVisible(false);
        }
    }

    private class TimeLogButtonClickListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            new TimeLogScreen(conn, MainPage.this).setVisible(true);
            MainPage.this.setVisible(false);
        }
    }
}
