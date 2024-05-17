package org.example.SwingComponents;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.example.Crud.CRUDcompany;
import org.example.Crud.CRUDdepartments;
import org.example.Crud.CRUDemployees;
import org.example.Enum.EmployeeState;
import org.example.Interfaces.IScreen;
import org.example.Models.Company;
import org.example.Models.Department;
import org.example.Models.Employee;
import org.example.Services.CompanyService;
import org.example.Services.DepartmentService;
import org.example.Services.EmployeeService;
import org.example.Validation.CompanyValidation;
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
import java.util.List;
import java.util.Optional;

public class EmployeeScreen extends JFrame implements IScreen {
    private final EmployeeService employeeService;
    private final JTable employeeTable;
    private final Connection conn;
    private final JFrame mainPage;
    private final DepartmentService departmentService;
    private final CompanyService companyService;
    private boolean sortAscending = true;

    public EmployeeScreen(Connection conn, JFrame mainPage) {
        this.conn = conn;
        this.mainPage = mainPage;
        this.employeeService = new EmployeeService(new CRUDemployees(), new EmployeeValidation());
        this.companyService = new CompanyService(new CRUDcompany(), new CompanyValidation());
        this.departmentService = new DepartmentService(new CRUDdepartments(), new DepartmentValidation());
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
        String[] columnNames = {"ID", "Name", "Age", "Phone", "Email", "Salary", "Status", "Company ID", "Department ID", "Manager ID", "Update", "Delete"};
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
                    employee.getDepartment(),
                    employee.getManagerId(),
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
        JComboBox<Integer> companyComboBox = new JComboBox<>();
        JComboBox<Integer> departmentComboBox = new JComboBox<>();
        JComboBox<Integer> managerComboBox = new JComboBox<>();
        JComboBox<String> statusComboBox = new JComboBox<>();

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
        panel.add(statusComboBox);
        panel.add(new JLabel("Company ID:"));
        panel.add(companyComboBox);
        panel.add(new JLabel("Department ID:"));
        panel.add(departmentComboBox);
        panel.add(new JLabel("Manager ID:"));
        panel.add(managerComboBox);

        List<Company> companies = companyService.getAllCompanies(conn);
        List<Department> departments = departmentService.getAllDepartments(conn);
        List<Employee> managers = employeeService.getAllManager(conn);
        List<String> stats = new ArrayList<>();
        for (EmployeeState employeeState : EmployeeState.values()) {
            stats.add(employeeState.toString());
        }
        for (Company company : companies) {
            companyComboBox.addItem(company.getId());
        }
        for (Department department : departments) {
            departmentComboBox.addItem(department.getId());
        }
        for (Employee manager : managers) {
            managerComboBox.addItem(manager.getId());
        }

        for (String status : stats) {
            statusComboBox.addItem(status);
        }

        int result = JOptionPane.showConfirmDialog(null, panel, "Add New Employee", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            Integer selectedCompany = (Integer) companyComboBox.getSelectedItem();
            Integer selectedDepartment = (Integer) departmentComboBox.getSelectedItem();
            Integer selectedManager = (Integer) managerComboBox.getSelectedItem();
            String selectedStatus = (String) statusComboBox.getSelectedItem();
            Employee newEmployee = new Employee(
                    nameField.getText(),
                    Integer.parseInt(ageField.getText()),
                    phoneField.getText(),
                    emailField.getText(),
                    Double.parseDouble(salaryField.getText()),
                    selectedDepartment,
                    selectedCompany,
                    selectedManager,
                    selectedStatus
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
            System.out.println(employee);

            JTextField nameField = new JTextField(employee.getName());
            JTextField ageField = new JTextField(String.valueOf(employee.getAge()));
            JTextField phoneField = new JTextField(employee.getPhoneNumber());
            JTextField emailField = new JTextField(employee.getEmail());
            JTextField salaryField = new JTextField(String.valueOf(employee.getSalary()));
            JComboBox<String> statusComboBox = new JComboBox<>();

            JComboBox<Integer> companyComboBox = new JComboBox<>();
            JComboBox<Integer> departmentComboBox = new JComboBox<>();
            JComboBox<Integer> managerComboBox = new JComboBox<>();

            List<Company> companies = companyService.getAllCompanies(conn);
            List<Department> departments = departmentService.getAllDepartments(conn);
            List<Employee> managers = employeeService.getAllManager(conn);

            companies.forEach(company -> companyComboBox.addItem(company.getId()));
            departments.forEach(department -> departmentComboBox.addItem(department.getId()));

            managerComboBox.addItem(employee.getId());
            for (Employee manager : managers) {
                managerComboBox.addItem(manager.getId());
            }

            for (EmployeeState employeeState : EmployeeState.values()) {

                statusComboBox.addItem(employeeState.toString());
            }

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
            panel.add(statusComboBox);
            panel.add(new JLabel("Company ID:"));
            panel.add(companyComboBox);
            panel.add(new JLabel("Department ID:"));
            panel.add(departmentComboBox);
            panel.add(new JLabel("Manager ID:"));
            panel.add(managerComboBox);


            int result = JOptionPane.showConfirmDialog(null, panel, "Update Employee", JOptionPane.OK_CANCEL_OPTION);
            if (result == JOptionPane.OK_OPTION) {
                employee.setName(nameField.getText());
                employee.setAge(Integer.parseInt(ageField.getText()));
                employee.setPhoneNumber(phoneField.getText());
                employee.setEmail(emailField.getText());
                employee.setSalary(Double.parseDouble(salaryField.getText()));
                employee.setEmployedStatus((String) statusComboBox.getSelectedItem());
                employee.setCompanyId((Integer) companyComboBox.getSelectedItem());
                employee.setDepartment((Integer) departmentComboBox.getSelectedItem());
                employee.setManagerId(managerComboBox.getSelectedItem() == null ? null : (Integer) managerComboBox.getSelectedItem());

                employeeService.updateEmployee(conn, employee);
                loadData();
            }
        } else {
            JOptionPane.showMessageDialog(null, "Employee not found", "Error", JOptionPane.ERROR_MESSAGE);
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
        int numberOfColumns = employeeTable.getColumnCount() - 2;
        PdfPTable pdfTable = new PdfPTable(numberOfColumns);

        for (int i = 0; i < numberOfColumns; i++) {
            pdfTable.addCell(employeeTable.getColumnName(i));
        }

        for (int rows = 0; rows < employeeTable.getRowCount(); rows++) {
            for (int cols = 0; cols < numberOfColumns; cols++) {
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
