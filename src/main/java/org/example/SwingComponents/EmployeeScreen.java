package org.example.SwingComponents;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.example.Crud.CRUDdepartments;
import org.example.Crud.CRUDemployees;
import org.example.Interfaces.IScreen;
import org.example.Models.Department;
import org.example.Models.Employee;
import org.example.Services.DepartmentService;
import org.example.Services.EmployeeService;
import org.example.Validation.DepartmentValidation;
import org.example.Validation.EmployeeValidation;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Optional;

public class EmployeeScreen extends JFrame implements IScreen {
    private JTable employeeTable;
    private Connection conn;
    private JFrame mainPage;
    private final EmployeeService employeeService;
    private boolean sortAscending = true;

    public EmployeeScreen(Connection conn, JFrame mainPage) {
        this.conn = conn;
        this.mainPage = mainPage;
        this.employeeService = new EmployeeService(new CRUDemployees(), new EmployeeValidation());

        setTitle("Employee Data");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton addButton = new JButton("Add");
        JButton backButton = new JButton("Back");
        JButton pdfButton = new JButton("Get PDF");
        JButton sortButton = new JButton("Sort by Name");

        addButton.addActionListener(e -> openAddDataDialog());
        backButton.addActionListener(e -> {
            EmployeeScreen.this.setVisible(false);
            mainPage.setVisible(true);
        });

        pdfButton.addActionListener(e -> {
            try {
                saveTableDataToPDF();
            } catch (DocumentException | FileNotFoundException ex) {
                ex.printStackTrace();
            }
        });

        sortButton.addActionListener(e -> toggleSort());

        bottomPanel.add(addButton);
        bottomPanel.add(backButton);
        bottomPanel.add(pdfButton);
        bottomPanel.add(sortButton);

        employeeTable = new JTable();
        add(new JScrollPane(employeeTable), BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        loadData();
    }

    @Override
    public void loadData() {
        ArrayList<Employee> employees = (ArrayList<Employee>) employeeService.getAllEmployees(conn);
        String[] columnNames = {"ID", "Name", "Age", "Phone", "Email", "Salary",  "Status", "Company ID","Department ID", "Team Lead ID", "Update", "Delete"};
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);

        for (Employee employee : employees) {
            Object[] row = {
                    employee.getId(),
                    employee.getName(),
                    employee.getAge(),
                    employee.getPhoneNumber(),
                    employee.getEmail(),
                    employee.getSalary(),
                    employee.getEmployedStatus(),
                    employee.getCompanyId(),
                    employee.getDepartment().getId(),
                    employee.getTeamLeadId(),
                    "Update",
                    "Delete"
            };
            tableModel.addRow(row);
        }


        employeeTable.setModel(tableModel);

        employeeTable.getColumn("Update").setCellRenderer(new ButtonRenderer());
        employeeTable.getColumn("Update").setCellEditor(new ButtonEditor(new JCheckBox(), "Update", rowIndex -> updateData(rowIndex)));
        employeeTable.getColumn("Delete").setCellRenderer(new ButtonRenderer());
        employeeTable.getColumn("Delete").setCellEditor(new ButtonEditor(new JCheckBox(), "Delete", rowIndex -> confirmAndDeleteData(rowIndex)));

    }

    @Override
    public void openAddDataDialog() {
        JTextField nameField = new JTextField();
        JTextField ageField = new JTextField();
        JTextField phoneField = new JTextField();
        JTextField emailField = new JTextField();
        JTextField salaryField = new JTextField();
        JTextField departmentIdField = new JTextField();
        JTextField companyIdField = new JTextField();
        JTextField teamLeadIdField = new JTextField();
        JTextField statusField = new JTextField();

        JPanel panel = new JPanel(new GridLayout(9, 2));
        panel.add(new JLabel("Name:"));
        panel.add(nameField);
        panel.add(new JLabel("Age:"));
        panel.add(ageField);
        panel.add(new JLabel("Phone Number:"));
        panel.add(phoneField);
        panel.add(new JLabel("Email:"));
        panel.add(emailField);
        panel.add(new JLabel("Salary:"));
        panel.add(salaryField);
        panel.add(new JLabel("Employment Status:"));
        panel.add(statusField);
        panel.add(new JLabel("Company ID:"));
        panel.add(companyIdField);
        panel.add(new JLabel("Department ID:"));
        panel.add(departmentIdField);
        panel.add(new JLabel("Team Lead ID:"));
        panel.add(teamLeadIdField);



        int result = JOptionPane.showConfirmDialog(null, panel, "Add New Employee", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            DepartmentService departmentService = new DepartmentService(new CRUDdepartments(), new DepartmentValidation());
            Optional<Department> department = departmentService.getDepartmentById(conn, Integer.parseInt(departmentIdField.getText()));
            if (!department.isPresent()) {
                JOptionPane.showMessageDialog(null, "No department found with ID: " + departmentIdField.getText(), "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Employee newEmployee = new Employee(
                    nameField.getText(),
                    Integer.parseInt(ageField.getText()),
                    phoneField.getText(),
                    emailField.getText(),
                    Double.parseDouble(salaryField.getText()),
                    department.get(),
                    teamLeadIdField.getText().isEmpty() ? null : Integer.parseInt(teamLeadIdField.getText()),
                    Integer.parseInt(companyIdField.getText()),
                    statusField.getText()
            );
            System.out.println(newEmployee);
            employeeService.addEmployee(conn, newEmployee);
            loadData();
        }
    }

    @Override
    public void updateData(int rowIndex) {
        DefaultTableModel model = (DefaultTableModel) employeeTable.getModel();
        Integer id = (Integer) model.getValueAt(rowIndex, 0);
        Optional<Employee> employeeOptional = employeeService.getEmployeeById(conn, id);

        if (employeeOptional.isPresent()) {
            Employee employee = employeeOptional.get();

            JTextField nameField = new JTextField(employee.getName());
            JTextField ageField = new JTextField(String.valueOf(employee.getAge()));
            JTextField phoneField = new JTextField(employee.getPhoneNumber());
            JTextField emailField = new JTextField(employee.getEmail());
            JTextField salaryField = new JTextField(String.valueOf(employee.getSalary()));
            JTextField departmentIdField = new JTextField(String.valueOf(employee.getDepartment().getId()));
            JTextField companyIdField = new JTextField(String.valueOf(employee.getCompanyId()));
            JTextField teamLeadIdField = new JTextField(employee.getTeamLeadId() != null ? employee.getTeamLeadId().toString() : "");
            JTextField statusField = new JTextField(employee.getEmployedStatus());

            JPanel panel = new JPanel(new GridLayout(9, 2));
            panel.add(new JLabel("Name:"));
            panel.add(nameField);
            panel.add(new JLabel("Age:"));
            panel.add(ageField);
            panel.add(new JLabel("Phone Number:"));
            panel.add(phoneField);
            panel.add(new JLabel("Email:"));
            panel.add(emailField);
            panel.add(new JLabel("Salary:"));
            panel.add(salaryField);
            panel.add(new JLabel("Employment Status:"));
            panel.add(statusField);
            panel.add(new JLabel("Company ID:"));
            panel.add(companyIdField);
            panel.add(new JLabel("Department ID:"));
            panel.add(departmentIdField);
            panel.add(new JLabel("Team Lead ID:"));
            panel.add(teamLeadIdField);


            int result = JOptionPane.showConfirmDialog(null, panel, "Update Employee", JOptionPane.OK_CANCEL_OPTION);
            if (result == JOptionPane.OK_OPTION) {
                employee.setName(nameField.getText());
                employee.setAge(Integer.parseInt(ageField.getText()));
                employee.setPhoneNumber(phoneField.getText());
                employee.setEmail(emailField.getText());
                employee.setSalary(Double.parseDouble(salaryField.getText()));
                employee.setDepartment(new Department(Integer.parseInt(departmentIdField.getText()), "", 0)); // Note: Real department fetching logic might be needed here
                employee.setCompanyId(Integer.parseInt(companyIdField.getText()));
                employee.setTeamLeadId(teamLeadIdField.getText().isEmpty() ? null : Integer.parseInt(teamLeadIdField.getText()));
                employee.setEmployedStatus(statusField.getText());
                System.out.println(employee);
                System.out.println(employee.getDepartment());
                employeeService.updateEmployee(conn, employee);
                loadData();
            }
        }
    }

    @Override
    public void confirmAndDeleteData(int rowIndex) {
        DefaultTableModel model = (DefaultTableModel) employeeTable.getModel();
        Integer id = (Integer) model.getValueAt(rowIndex, 0);

        int result = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to delete this employee?",
                "Confirm Deletion",
                JOptionPane.YES_NO_OPTION
        );

        if (result == JOptionPane.YES_OPTION) {
            employeeService.deleteEmployeeById(conn, id);
            loadData();
        }
    }

    private void saveTableDataToPDF() throws DocumentException, FileNotFoundException {
        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream("EmployeeData.pdf"));
        document.open();

        PdfPTable pdfTable = new PdfPTable(employeeTable.getColumnCount());
        for (int i = 0; i < employeeTable.getColumnCount(); i++) {
            pdfTable.addCell(employeeTable.getColumnName(i));
        }

        for (int rows = 0; rows < employeeTable.getRowCount(); rows++) {
            for (int cols = 0; cols < employeeTable.getColumnCount(); cols++) {
                pdfTable.addCell(employeeTable.getModel().getValueAt(rows, cols).toString());
            }
        }

        document.add(pdfTable);
        document.close();
        JOptionPane.showMessageDialog(this, "PDF file has been created successfully!");
    }

    private void toggleSort() {
        TableRowSorter<TableModel> sorter = new TableRowSorter<>(employeeTable.getModel());
        employeeTable.setRowSorter(sorter);
        ArrayList<RowSorter.SortKey> sortKeys = new ArrayList<>();

        int columnIndexToSort = 1;
        sortKeys.add(new RowSorter.SortKey(columnIndexToSort, sortAscending ? SortOrder.ASCENDING : SortOrder.DESCENDING));

        sorter.setComparator(columnIndexToSort, new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return o1.compareTo(o2);
            }
        });

        sorter.setSortKeys(sortKeys);
        sorter.sort();

        sortAscending = !sortAscending;
    }
}
