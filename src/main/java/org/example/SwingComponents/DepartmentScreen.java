package org.example.SwingComponents;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.example.Crud.CRUDcompany;
import org.example.Crud.CRUDdepartments;
import org.example.Crud.CRUDemployees;
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

class DepartmentScreen extends JFrame implements IScreen {
    private final DepartmentService departmentService;
    private final CompanyService companyService;
    private final EmployeeService employeeService;
    private final JTable departmentTable;
    private final Connection conn;
    private final JFrame mainPage;
    private boolean sortAscending = true;

    public DepartmentScreen(Connection conn, JFrame mainPage) {
        this.conn = conn;
        this.mainPage = mainPage;
        this.departmentService = new DepartmentService(new CRUDdepartments(), new DepartmentValidation());
        companyService = new CompanyService(new CRUDcompany(), new CompanyValidation());
        employeeService = new EmployeeService(new CRUDemployees(), new EmployeeValidation());
        setTitle("Department Data");
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
            DepartmentScreen.this.setVisible(false);
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

        departmentTable = new JTable();
        add(new JScrollPane(departmentTable), BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        loadData();
    }


    @Override
    public void loadData() {
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
        departmentTable.getColumn("Update").setCellEditor(new ButtonEditor(new JCheckBox(), "Update", rowIndex -> updateData(rowIndex)));
        departmentTable.getColumn("Delete").setCellRenderer(new ButtonRenderer());
        departmentTable.getColumn("Delete").setCellEditor(new ButtonEditor(new JCheckBox(), "Delete", rowIndex -> confirmAndDeleteData(rowIndex)));
    }


    @Override
    public void openAddDataDialog() {
        JTextField nameField = new JTextField();

        List<Company> companies = companyService.getAllCompanies(conn);
        List<Employee> employees = employeeService.getAllEmployees(conn);

        JComboBox<Integer> companyComboBox = new JComboBox<>();
        JComboBox<Integer> managerComboBox = new JComboBox<>();

        for (Company company : companies) {
            companyComboBox.addItem(company.getId());
        }
        for (Employee employee : employees) {
            managerComboBox.addItem(employee.getId());
        }

        JPanel panel = new JPanel(new GridLayout(3, 2));
        panel.add(new JLabel("Name:"));
        panel.add(nameField);
        panel.add(new JLabel("Company:"));
        panel.add(companyComboBox);
        panel.add(new JLabel("Manager:"));
        panel.add(managerComboBox);

        int result = JOptionPane.showConfirmDialog(null, panel, "Add New Department", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            Integer selectedCompanyId = (Integer) companyComboBox.getSelectedItem();
            Integer selectedManagerId = (Integer) managerComboBox.getSelectedItem();
            System.out.println(selectedCompanyId + " " + selectedManagerId + " " + nameField.getText());
            Department newDepartment = new Department(nameField.getText(), selectedCompanyId, selectedManagerId);
            System.out.println(newDepartment);
            departmentService.addDepartment(conn, newDepartment);
            loadData();
        }
    }


    @Override
    public void updateData(int rowIndex) {
        DefaultTableModel model = (DefaultTableModel) departmentTable.getModel();
        Integer id = (Integer) model.getValueAt(rowIndex, 0);

        Optional<Department> departmentOptional = departmentService.getDepartmentById(conn, id);
        if (departmentOptional.isPresent()) {
            Department department = departmentOptional.get();
            JTextField nameField = new JTextField(department.getName());

            List<Company> companies = companyService.getAllCompanies(conn);
            List<Employee> employees = employeeService.getAllEmployees(conn);

            JComboBox<Integer> companyComboBox = new JComboBox<>();
            JComboBox<Integer> managerComboBox = new JComboBox<>();

            for (Company company : companies) {
                companyComboBox.addItem(company.getId());
            }
            for (Employee employee : employees) {
                managerComboBox.addItem(employee.getId());
            }


            JPanel panel = new JPanel(new GridLayout(3, 2));
            panel.add(new JLabel("Name:"));
            panel.add(nameField);
            panel.add(new JLabel("Company ID:"));
            panel.add(companyComboBox);
            panel.add(new JLabel("Manager ID:"));
            panel.add(managerComboBox);

            int result = JOptionPane.showConfirmDialog(null, panel, "Update Department", JOptionPane.OK_CANCEL_OPTION);
            if (result == JOptionPane.OK_OPTION) {
                department.setName(nameField.getText());
                department.setCompanyId((Integer) companyComboBox.getSelectedItem());
                department.setManagerId((Integer) managerComboBox.getSelectedItem());
                System.out.println(department);
                departmentService.updateDepartment(conn, department);
                loadData();
            }
        }
    }

    @Override
    public void confirmAndDeleteData(int rowIndex) {
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
            loadData();
        }

    }

    private void saveTableDataToPDF() throws DocumentException, FileNotFoundException {
        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream("DepartmentData.pdf"));
        document.open();

        int numberOfColumns = departmentTable.getColumnCount() - 2;
        PdfPTable pdfTable = new PdfPTable(numberOfColumns);

        for (int i = 0; i < numberOfColumns; i++) {
            pdfTable.addCell(departmentTable.getColumnName(i));
        }

        for (int rows = 0; rows < departmentTable.getRowCount(); rows++) {
            for (int cols = 0; cols < numberOfColumns; cols++) {
                pdfTable.addCell(departmentTable.getModel().getValueAt(rows, cols).toString());
            }
        }

        document.add(pdfTable);
        document.close();
        JOptionPane.showMessageDialog(this, "PDF file has been created successfully!");
    }


    private void toggleSort() {
        TableRowSorter<TableModel> sorter = new TableRowSorter<>(departmentTable.getModel());
        departmentTable.setRowSorter(sorter);
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
