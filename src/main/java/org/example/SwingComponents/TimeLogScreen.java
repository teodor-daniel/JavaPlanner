package org.example.SwingComponents;

import org.example.Crud.CRUDtimeLogs;
import org.example.Interfaces.IScreen;
import org.example.Models.TimeLog;
import org.example.Services.TimeLogService;
import org.example.Validation.TimeLogValidation;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Optional;

public class TimeLogScreen extends JFrame implements IScreen {
    private JTable timeLogTable;
    private Connection conn;
    private JFrame mainPage;
    private final TimeLogService timeLogService;

    public TimeLogScreen(Connection conn, JFrame mainPage) {
        this.conn = conn;
        this.mainPage = mainPage;
        this.timeLogService = new TimeLogService(new CRUDtimeLogs(), new TimeLogValidation());

        setTitle("Time Log Data");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton addButton = new JButton("Add");
        JButton backButton = new JButton("Back");

        addButton.addActionListener(e -> openAddDataDialog());
        backButton.addActionListener(e -> {
            TimeLogScreen.this.setVisible(false);
            mainPage.setVisible(true);
        });

        bottomPanel.add(addButton);
        bottomPanel.add(backButton);

        timeLogTable = new JTable();
        add(new JScrollPane(timeLogTable), BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        loadData();
    }

    @Override
    public void loadData() {
        ArrayList<TimeLog> timeLogs = (ArrayList<TimeLog>) timeLogService.getAllTimeLogs(conn);
        String[] columnNames = {"ID", "Task ID", "Employee ID", "Hours Logged", "Log Date", "Description", "Update", "Delete"};
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);

        for (TimeLog timeLog : timeLogs) {
            Object[] row = {
                    timeLog.getId(),
                    timeLog.getTaskId(),
                    timeLog.getEmployeeId(),
                    timeLog.getHoursLogged(),
                    timeLog.getLogDate().toString(),
                    timeLog.getDescription(),
                    "Update",
                    "Delete"
            };
            tableModel.addRow(row);
        }

        timeLogTable.setModel(tableModel);

        timeLogTable.getColumn("Update").setCellRenderer(new ButtonRenderer());
        timeLogTable.getColumn("Update").setCellEditor(new ButtonEditor(new JCheckBox(), "Update", this::updateData));
        timeLogTable.getColumn("Delete").setCellRenderer(new ButtonRenderer());
        timeLogTable.getColumn("Delete").setCellEditor(new ButtonEditor(new JCheckBox(), "Delete", this::confirmAndDeleteData));
    }

    @Override
    public void openAddDataDialog() {
        JTextField taskIdField = new JTextField();
        JTextField employeeIdField = new JTextField();
        JTextField hoursLoggedField = new JTextField();
        JTextField logDateField = new JTextField();
        JTextField descriptionField = new JTextField();

        JPanel panel = new JPanel(new GridLayout(5, 2));
        panel.add(new JLabel("Task ID:"));
        panel.add(taskIdField);
        panel.add(new JLabel("Employee ID:"));
        panel.add(employeeIdField);
        panel.add(new JLabel("Hours Logged:"));
        panel.add(hoursLoggedField);
        panel.add(new JLabel("Log Date (YYYY-MM-DD):"));
        panel.add(logDateField);
        panel.add(new JLabel("Description:"));
        panel.add(descriptionField);

        int result = JOptionPane.showConfirmDialog(null, panel, "Add New Time Log", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            TimeLog newTimeLog = new TimeLog(
                    Integer.parseInt(taskIdField.getText()),
                    Integer.parseInt(employeeIdField.getText()),
                    Double.parseDouble(hoursLoggedField.getText()),
                    Date.valueOf(logDateField.getText()),
                    descriptionField.getText()
            );
            timeLogService.addTimeLog(conn, newTimeLog);
            loadData();
        }
    }

    @Override
    public void updateData(int rowIndex) {
        DefaultTableModel model = (DefaultTableModel) timeLogTable.getModel();
        Integer id = (Integer) model.getValueAt(rowIndex, 0);
        Optional<TimeLog> timeLogOptional = timeLogService.getTimeLogById(conn, id);

        if (timeLogOptional.isPresent()) {
            TimeLog timeLog = timeLogOptional.get();

            JTextField taskIdField = new JTextField(String.valueOf(timeLog.getTaskId()));
            JTextField employeeIdField = new JTextField(String.valueOf(timeLog.getEmployeeId()));
            JTextField hoursLoggedField = new JTextField(String.valueOf(timeLog.getHoursLogged()));
            JTextField logDateField = new JTextField(timeLog.getLogDate().toString());
            JTextField descriptionField = new JTextField(timeLog.getDescription());

            JPanel panel = new JPanel(new GridLayout(5, 2));
            panel.add(new JLabel("Task ID:"));
            panel.add(taskIdField);
            panel.add(new JLabel("Employee ID:"));
            panel.add(employeeIdField);
            panel.add(new JLabel("Hours Logged:"));
            panel.add(hoursLoggedField);
            panel.add(new JLabel("Log Date (YYYY-MM-DD):"));
            panel.add(logDateField);
            panel.add(new JLabel("Description:"));
            panel.add(descriptionField);

            int result = JOptionPane.showConfirmDialog(null, panel, "Update Time Log", JOptionPane.OK_CANCEL_OPTION);
            if (result == JOptionPane.OK_OPTION) {
                timeLog.setTaskId(Integer.parseInt(taskIdField.getText()));
                timeLog.setEmployeeId(Integer.parseInt(employeeIdField.getText()));
                timeLog.setHoursLogged(Double.parseDouble(hoursLoggedField.getText()));
                timeLog.setLogDate(Date.valueOf(logDateField.getText()));
                timeLog.setDescription(descriptionField.getText());
                timeLogService.updateTimeLog(conn, timeLog);
                loadData();
            }
        }
    }

    @Override
    public void confirmAndDeleteData(int rowIndex) {
        DefaultTableModel model = (DefaultTableModel) timeLogTable.getModel();
        Integer id = (Integer) model.getValueAt(rowIndex, 0);

        int result = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to delete this time log?",
                "Confirm Deletion",
                JOptionPane.YES_NO_OPTION
        );

        if (result == JOptionPane.YES_OPTION) {
            timeLogService.deleteTimeLogById
                    (conn, id);
            loadData();
        }
    }
}