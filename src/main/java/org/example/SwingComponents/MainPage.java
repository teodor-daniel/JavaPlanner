package org.example.SwingComponents;

import org.example.DataBase.DatabaseConnection;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;

public class MainPage extends JFrame {
    private final Connection conn;
    private final StatisticsUpdater statsUpdater;

    public MainPage() {
        conn = DatabaseConnection.getInstance().getConnection();
        statsUpdater = new StatisticsUpdater(conn);

        setTitle("Database Management");
        setSize(1200, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        mainPanel.setBackground(Color.WHITE);

        JPanel navbarPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        navbarPanel.setBorder(BorderFactory.createTitledBorder("Navigation"));
        navbarPanel.setBackground(new Color(240, 240, 240));

        Dimension buttonSize = new Dimension(150, 40);

        JButton companyButton = createStyledButton("Company", buttonSize);
        companyButton.addActionListener(new CompanyButtonClickListener());
        navbarPanel.add(companyButton);

        JButton departmentButton = createStyledButton("Department", buttonSize);
        departmentButton.addActionListener(new DepartmentButtonClickListener());
        navbarPanel.add(departmentButton);

        JButton employeeButton = createStyledButton("Employee", buttonSize);
        employeeButton.addActionListener(new EmployeeButtonClickListener());
        navbarPanel.add(employeeButton);

        JButton projectButton = createStyledButton("Project", buttonSize);
        projectButton.addActionListener(new ProjectButtonClickListener());
        navbarPanel.add(projectButton);

        JButton taskButton = createStyledButton("Task", buttonSize);
        taskButton.addActionListener(new TaskButtonClickListener());
        navbarPanel.add(taskButton);

        JButton timelogButton = createStyledButton("Time Log", buttonSize);
        timelogButton.addActionListener(new TimeLogButtonClickListener());
        navbarPanel.add(timelogButton);

        mainPanel.add(navbarPanel, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBackground(Color.WHITE);
        mainPanel.add(centerPanel, BorderLayout.CENTER);

        JPanel statsPanel = new JPanel(new BorderLayout());
        statsPanel.setBorder(BorderFactory.createTitledBorder("Statistics"));
        statsPanel.setBackground(new Color(255, 255, 255));

        JPanel statsLabelsPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        statsLabelsPanel.setBackground(Color.WHITE);

        JLabel companyStats = createStatsLabel("Companies: ");
        statsLabelsPanel.add(companyStats);

        JLabel departmentStats = createStatsLabel("Departments: ");
        statsLabelsPanel.add(departmentStats);

        JLabel employeeStats = createStatsLabel("Employees: ");
        statsLabelsPanel.add(employeeStats);

        JLabel projectStats = createStatsLabel("Projects: ");
        statsLabelsPanel.add(projectStats);

        JLabel taskStats = createStatsLabel("Tasks: ");
        statsLabelsPanel.add(taskStats);

        JLabel timelogStats = createStatsLabel("Time Logs: ");
        statsLabelsPanel.add(timelogStats);

        statsUpdater.updateStatistics(companyStats, departmentStats, employeeStats, projectStats, taskStats, timelogStats);

        statsPanel.add(statsLabelsPanel, BorderLayout.CENTER);


        //refresh button, could have tried to refresh the stats every x seconds
        JButton refreshButton = createStyledButton("Refresh", new Dimension(100, 40));
        refreshButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                statsUpdater.updateStatistics(companyStats, departmentStats, employeeStats, projectStats, taskStats, timelogStats);
            }
        });

        JPanel refreshButtonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        refreshButtonPanel.setBackground(Color.WHITE);
        refreshButtonPanel.add(refreshButton);
        statsPanel.add(refreshButtonPanel, BorderLayout.SOUTH);

        centerPanel.add(statsPanel, BorderLayout.NORTH);
        //WIP might not even do it, but I will leave it here for now
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        searchPanel.setBackground(Color.WHITE);
        JTextField searchField = new JTextField(20);
        JButton searchButton = new JButton("Search");
        searchPanel.add(searchField);
        searchPanel.add(searchButton);
        mainPanel.add(searchPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }

    private JLabel createStatsLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.PLAIN, 14));
        label.setHorizontalAlignment(SwingConstants.LEFT);
        return label;
    }

    private JButton createStyledButton(String text, Dimension size) {
        JButton button = new JButton(text);
        button.setPreferredSize(size);
        button.setBackground(new Color(100, 150, 200));
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setFocusPainted(false);
        return button;
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
