package org.example.SwingComponents;


import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.example.Crud.CRUDcompany;
import org.example.Interfaces.IScreen;
import org.example.Models.Company;
import org.example.Services.CompanyService;
import org.example.Validation.CompanyValidation;

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

public class CompanyScreen extends JFrame implements IScreen {
    private final CompanyService companyService;
    private final JTable companyTable;
    private final Connection conn;
    private final JFrame mainPage;
    private boolean sortAscending = true;

    public CompanyScreen(Connection conn, JFrame mainPage) {
        this.conn = conn;
        this.mainPage = mainPage;
        this.companyService = new CompanyService(new CRUDcompany(), new CompanyValidation());

        setTitle("Company Data");
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
            CompanyScreen.this.setVisible(false);
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
        companyTable.setRowSorter(new TableRowSorter<>(tableModel));

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

    //Save to pdf
    private void saveTableDataToPDF() throws DocumentException, FileNotFoundException {
        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream("CompanyData.pdf"));
        document.open();

        int numberOfColumns = companyTable.getColumnCount() - 2;
        PdfPTable pdfTable = new PdfPTable(numberOfColumns);

        for (int i = 0; i < numberOfColumns; i++) {
            pdfTable.addCell(companyTable.getColumnName(i));
        }

        for (int rows = 0; rows < companyTable.getRowCount(); rows++) {
            for (int cols = 0; cols < numberOfColumns; cols++) {
                pdfTable.addCell(companyTable.getModel().getValueAt(rows, cols).toString());
            }
        }

        document.add(pdfTable);
        document.close();
        JOptionPane.showMessageDialog(this, "PDF file has been created successfully!");
    }


    private void toggleSort() {
        TableRowSorter<TableModel> sorter = new TableRowSorter<>(companyTable.getModel());
        companyTable.setRowSorter(sorter);
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

