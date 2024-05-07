package org.example.SwingComponents;

import org.example.Models.Department;
import org.example.Services.DepartmentService;
import org.example.Crud.CRUDdepartments;
import org.example.Validation.DepartmentValidationService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableCellEditor;
import java.awt.*;
import java.util.ArrayList;
import java.util.Optional;
import java.sql.Connection;

class DepartmentScreen extends JFrame {
    private JTable departmentTable;
    private Connection conn;
    private JFrame mainPage;
    private final DepartmentService departmentService;

    public DepartmentScreen(Connection conn, JFrame mainPage) {
        this.conn = conn;
        this.mainPage = mainPage;
        this.departmentService = new DepartmentService(new CRUDdepartments(), new DepartmentValidationService());

        setTitle("Department Data");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton addButton = new JButton("Add");
        JButton backButton = new JButton("Back");

        addButton.addActionListener(e -> openAddDepartmentDialog());
        backButton.addActionListener(e -> {
            DepartmentScreen.this.setVisible(false);
            mainPage.setVisible(true);
        });

        bottomPanel.add(addButton);
        bottomPanel.add(backButton);

        departmentTable = new JTable();
        add(new JScrollPane(departmentTable), BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        loadDepartmentData();
    }

    // Load department data
    private void loadDepartmentData() {
        ArrayList<Department> departments = (ArrayList<Department>) departmentService.getAllDepartments(conn);

        String[] columnNames = {"ID", "Name", "Company ID", "Manager ID", "Update", "Delete"};
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);

        for (Department department : departments) {
            Object[] row = {
                    department.getId(),
                    department.getName(),
                    department.getCompanyId(),
                    department.getManagerId(),
                    "Update",
                    "Delete"
            };
            tableModel.addRow(row);
        }

        departmentTable.setModel(tableModel);

        departmentTable.getColumn("Update").setCellRenderer(new ButtonRenderer());
        departmentTable.getColumn("Update").setCellEditor(new ButtonEditor(new JCheckBox(), "Update", rowIndex -> updateDepartment(rowIndex)));
        departmentTable.getColumn("Delete").setCellRenderer(new ButtonRenderer());
        departmentTable.getColumn("Delete").setCellEditor(new ButtonEditor(new JCheckBox(), "Delete", rowIndex -> confirmAndDeleteDepartment(rowIndex)));
    }

    // Open a form to add a new department
    private void openAddDepartmentDialog() {
        JTextField nameField = new JTextField();
        JTextField companyIdField = new JTextField();
        JTextField managerIdField = new JTextField();

        JPanel panel = new JPanel(new GridLayout(3, 2));
        panel.add(new JLabel("Name:"));
        panel.add(nameField);
        panel.add(new JLabel("Company ID:"));
        panel.add(companyIdField);
        panel.add(new JLabel("Manager ID:"));
        panel.add(managerIdField);

        int result = JOptionPane.showConfirmDialog(null, panel, "Add New Department", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            Department newDepartment = new Department(managerIdField.getText().isEmpty() ? null : Integer.parseInt(managerIdField.getText()), nameField.getText(), Integer.parseInt(companyIdField.getText()));
            departmentService.addDepartment(conn, newDepartment);
            loadDepartmentData();
        }
    }

    //Update a department by opening a dialog with existing data
    private void updateDepartment(int rowIndex) {
        DefaultTableModel model = (DefaultTableModel) departmentTable.getModel();
        Integer id = (Integer) model.getValueAt(rowIndex, 0);
        Optional<Department> departmentOptional = departmentService.getDepartmentById(conn, id);

        if (departmentOptional.isPresent()) {
            Department department = departmentOptional.get();
            JTextField nameField = new JTextField(department.getName());
            JTextField companyIdField = new JTextField(String.valueOf(department.getCompanyId()));
            JTextField managerIdField = new JTextField(department.getManagerId() != null ? String.valueOf(department.getManagerId()) : "");

            JPanel panel = new JPanel(new GridLayout(3, 2));
            panel.add(new JLabel("Name:"));
            panel.add(nameField);
            panel.add(new JLabel("Company ID:"));
            panel.add(companyIdField);
            panel.add(new JLabel("Manager ID:"));
            panel.add(managerIdField);

            int result = JOptionPane.showConfirmDialog(null, panel, "Update Department", JOptionPane.OK_CANCEL_OPTION);
            if (result == JOptionPane.OK_OPTION) {
                department.setName(nameField.getText());
                department.setCompanyId(Integer.parseInt(companyIdField.getText()));
                department.setManagerId(managerIdField.getText().isEmpty() ? null : Integer.parseInt(managerIdField.getText())); //de verificat cu validation daca exista.
                departmentService.updateDepartment(conn, department);
                loadDepartmentData();
            }
        }
    }

    //Confirm and delete a department
    private void confirmAndDeleteDepartment(int rowIndex) {
        DefaultTableModel model = (DefaultTableModel) departmentTable.getModel();
        Integer id = (Integer) model.getValueAt(rowIndex, 0);

        int result = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to delete this department?",
                "Confirm Deletion",
                JOptionPane.YES_NO_OPTION
        );

        if (result == JOptionPane.YES_OPTION) {
            departmentService.deleteDepartmentById(conn, id);
            loadDepartmentData();
        }
    }
}
