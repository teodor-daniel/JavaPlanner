package org.example.SwingComponents;

import org.example.Interfaces.ScreenInterface;
import org.example.Models.Company;
import org.example.Services.CompanyService;
import org.example.Crud.CRUDcompany;
import org.example.Validation.CompanyValidationService;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableCellEditor;
import java.awt.*;
import java.util.ArrayList;
import java.util.Optional;
import java.sql.Connection;

public class CompanyScreen extends JFrame implements ScreenInterface {
    private JTable companyTable;
    private Connection conn;
    private JFrame mainPage;
    private final CompanyService companyService;

    public CompanyScreen(Connection conn, JFrame mainPage) {
        this.conn = conn;
        this.mainPage = mainPage;
        this.companyService = new CompanyService(new CRUDcompany(), new CompanyValidationService());

        setTitle("Company Data");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton addButton = new JButton("Add");
        JButton backButton = new JButton("Back");

        addButton.addActionListener(e -> openAddDataDialog());
        backButton.addActionListener(e -> {
            CompanyScreen.this.setVisible(false);
            mainPage.setVisible(true);
        });

        bottomPanel.add(addButton);
        bottomPanel.add(backButton);

        companyTable = new JTable();
        add(new JScrollPane(companyTable), BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        loadData();
    }

    //Load company data
@Override
    public void loadData() {
        ArrayList<Company> companies = (ArrayList<Company>) companyService.getAllCompanies(conn);

        String[] columnNames = {"ID", "Name", "Address", "Registration Number", "Phone Number", "Email", "Update", "Delete"};
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);

        for (Company company : companies) {
            Object[] row = {
                    company.getId(),
                    company.getName(),
                    company.getAddress(),
                    company.getRegistrationNumber(),
                    company.getPhoneNumber(),
                    company.getEmail(),
                    "Update",
                    "Delete"
            };
            tableModel.addRow(row);
        }

        companyTable.setModel(tableModel);

        companyTable.getColumn("Update").setCellRenderer(new ButtonRenderer());
        companyTable.getColumn("Update").setCellEditor(new ButtonEditor(new JCheckBox(), "Update", rowIndex -> updateData(rowIndex)));
        companyTable.getColumn("Delete").setCellRenderer(new ButtonRenderer());
        companyTable.getColumn("Delete").setCellEditor(new ButtonEditor(new JCheckBox(), "Delete", rowIndex -> confirmAndDeleteData(rowIndex)));
    }



    @Override
    public void openAddDataDialog() {
        JTextField nameField = new JTextField();
        JTextField addressField = new JTextField();
        JTextField regNumberField = new JTextField();
        JTextField phoneNumberField = new JTextField();
        JTextField emailField = new JTextField();

        JPanel panel = new JPanel(new GridLayout(5, 2));
        panel.add(new JLabel("Name:"));
        panel.add(nameField);
        panel.add(new JLabel("Address:"));
        panel.add(addressField);
        panel.add(new JLabel("Registration Number:"));
        panel.add(regNumberField);
        panel.add(new JLabel("Phone Number:"));
        panel.add(phoneNumberField);
        panel.add(new JLabel("Email:"));
        panel.add(emailField);

        int result = JOptionPane.showConfirmDialog(null, panel, "Add New Company", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            Company newCompany = new Company(nameField.getText(), addressField.getText(), regNumberField.getText(),
                    phoneNumberField.getText(), emailField.getText());
            companyService.addCompany(conn, newCompany);
            loadData();
        }
    }

@Override
    public void updateData(int rowIndex) {
        DefaultTableModel model = (DefaultTableModel) companyTable.getModel();
        Integer id = (Integer) model.getValueAt(rowIndex, 0);
        Optional<Company> companyOptional = companyService.getCompanyById(conn, id);

        if (companyOptional.isPresent()) {
            Company company = companyOptional.get();
            JTextField nameField = new JTextField(company.getName());
            JTextField addressField = new JTextField(company.getAddress());
            JTextField regNumberField = new JTextField(company.getRegistrationNumber());
            JTextField phoneNumberField = new JTextField(company.getPhoneNumber());
            JTextField emailField = new JTextField(company.getEmail());

            JPanel panel = new JPanel(new GridLayout(5, 2));
            panel.add(new JLabel("Name:"));
            panel.add(nameField);
            panel.add(new JLabel("Address:"));
            panel.add(addressField);
            panel.add(new JLabel("Registration Number:"));
            panel.add(regNumberField);
            panel.add(new JLabel("Phone Number:"));
            panel.add(phoneNumberField);
            panel.add(new JLabel("Email:"));
            panel.add(emailField);

            int result = JOptionPane.showConfirmDialog(null, panel, "Update Company", JOptionPane.OK_CANCEL_OPTION);
            if (result == JOptionPane.OK_OPTION) {
                company.setName(nameField.getText());
                company.setAddress(addressField.getText());
                company.setRegistrationNumber(regNumberField.getText());
                company.setPhoneNumber(phoneNumberField.getText());
                company.setEmail(emailField.getText());
                companyService.updateCompany(conn, company);
                loadData();
            }
        }
    }



    @Override
    public void confirmAndDeleteData(int rowIndex) {
        DefaultTableModel model = (DefaultTableModel) companyTable.getModel();
        Integer id = (Integer) model.getValueAt(rowIndex, 0);

        int result = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to delete this company?",
                "Confirm Deletion",
                JOptionPane.YES_NO_OPTION
        );

        if (result == JOptionPane.YES_OPTION) {
            companyService.deleteCompanyById(conn, id);
            loadData();
        }
    }
}

