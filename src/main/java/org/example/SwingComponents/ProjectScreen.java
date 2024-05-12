package org.example.SwingComponents;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.example.Crud.CRUDprojects;
import org.example.Interfaces.IScreen;
import org.example.Models.Project;
import org.example.Services.ProjectService;
import org.example.Validation.ProjectValidation;

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

public class ProjectScreen extends JFrame implements IScreen {
    private JTable projectTable;
    private Connection conn;
    private JFrame mainPage;
    private final ProjectService projectService;
    private boolean sortAscending = true;

    public ProjectScreen(Connection conn, JFrame mainPage) {
        this.conn = conn;
        this.mainPage = mainPage;
        this.projectService = new ProjectService(new CRUDprojects(), new ProjectValidation());

        setTitle("Project Data");
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
            ProjectScreen.this.setVisible(false);
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

        projectTable = new JTable();
        add(new JScrollPane(projectTable), BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        loadData();
    }


    @Override
    public void loadData() {
        ArrayList<Project> projects = (ArrayList<Project>) projectService.getAllProjects(conn);
        String[] columnNames = {"ID", "Name", "Status", "Budget", "Department ID", "Team Lead ID", "Update", "Delete"};
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);

        for (Project project : projects) {
            Object[] row = {
                    project.getId(),
                    project.getName(),
                    project.getStatus(),
                    project.getBudget(),
                    project.getDepartmentId(),
                    project.getTeamLead(),
                    "Update",
                    "Delete"
            };
            tableModel.addRow(row);
        }

        projectTable.setModel(tableModel);

        projectTable.getColumn("Update").setCellRenderer(new ButtonRenderer());
        projectTable.getColumn("Update").setCellEditor(new ButtonEditor(new JCheckBox(), "Update", rowIndex -> updateData(rowIndex)));
        projectTable.getColumn("Delete").setCellRenderer(new ButtonRenderer());
        projectTable.getColumn("Delete").setCellEditor(new ButtonEditor(new JCheckBox(), "Delete", rowIndex -> confirmAndDeleteData(rowIndex)));

    }

    @Override
    public void openAddDataDialog() {
        JTextField nameField = new JTextField();
        JTextField statusField = new JTextField();
        JTextField budgetField = new JTextField();
        JTextField departmentIdField = new JTextField();
        JTextField teamLeadIdField = new JTextField();

        JPanel panel = new JPanel(new GridLayout(5, 2));
        panel.add(new JLabel("Name:"));
        panel.add(nameField);
        panel.add(new JLabel("Status:"));
        panel.add(statusField);
        panel.add(new JLabel("Budget:"));
        panel.add(budgetField);
        panel.add(new JLabel("Department ID:"));
        panel.add(departmentIdField);
        panel.add(new JLabel("Team Lead ID:"));
        panel.add(teamLeadIdField);

        int result = JOptionPane.showConfirmDialog(null, panel, "Add New Project", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            Project newProject = new Project(
                    nameField.getText(),
                    statusField.getText(),
                    Double.parseDouble(budgetField.getText()),
                    Integer.parseInt(departmentIdField.getText()),
                    Integer.parseInt(teamLeadIdField.getText())
            );
            projectService.addProject(conn, newProject);
            loadData();
        }
    }

    @Override
    public void updateData(int rowIndex) {
        DefaultTableModel model = (DefaultTableModel) projectTable.getModel();
        Integer id = (Integer) model.getValueAt(rowIndex, 0);
        Optional<Project> projectOptional = projectService.getProjectById(conn, id);

        if (projectOptional.isPresent()) {
            Project project = projectOptional.get();

            JTextField nameField = new JTextField(project.getName());
            JTextField statusField = new JTextField(project.getStatus());
            JTextField budgetField = new JTextField(String.valueOf(project.getBudget()));
            JTextField departmentIdField = new JTextField(String.valueOf(project.getDepartmentId()));
            JTextField teamLeadIdField = new JTextField(String.valueOf(project.getTeamLead()));

            JPanel panel = new JPanel(new GridLayout(5, 2));
            panel.add(new JLabel("Name:"));
            panel.add(nameField);
            panel.add(new JLabel("Status:"));
            panel.add(statusField);
            panel.add(new JLabel("Budget:"));
            panel.add(budgetField);
            panel.add(new JLabel("Department ID:"));
            panel.add(departmentIdField);
            panel.add(new JLabel("Team Lead ID:"));
            panel.add(teamLeadIdField);

            int result = JOptionPane.showConfirmDialog(null, panel, "Update Project", JOptionPane.OK_CANCEL_OPTION);
            if (result == JOptionPane.OK_OPTION) {
                project.setName(nameField.getText());
                project.setStatus(statusField.getText());
                project.setBudget(Double.parseDouble(budgetField.getText()));
                project.setDepartmentId(Integer.parseInt(departmentIdField.getText()));
                project.setTeamLead(Integer.parseInt(teamLeadIdField.getText()));
                projectService.updateProject(conn, project);
                loadData();
            }
        }
    }

    @Override
    public void confirmAndDeleteData(int rowIndex) {
        DefaultTableModel model = (DefaultTableModel) projectTable.getModel();
        Integer id = (Integer) model.getValueAt(rowIndex, 0);

        int result = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to delete this project?",
                "Confirm Deletion",
                JOptionPane.YES_NO_OPTION
        );

        if (result == JOptionPane.YES_OPTION) {
            projectService.deleteProjectById(conn, id);
            loadData();
        }
    }
    private void saveTableDataToPDF() throws DocumentException, FileNotFoundException {
        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream("ProjectData.pdf"));
        document.open();

        PdfPTable pdfTable = new PdfPTable(projectTable.getColumnCount());
        for (int i = 0; i < projectTable.getColumnCount(); i++) {
            pdfTable.addCell(projectTable.getColumnName(i));
        }

        for (int rows = 0; rows < projectTable.getRowCount(); rows++) {
            for (int cols = 0; cols < projectTable.getColumnCount(); cols++) {
                pdfTable.addCell(projectTable.getModel().getValueAt(rows, cols).toString());
            }
        }

        document.add(pdfTable);
        document.close();
        JOptionPane.showMessageDialog(this, "PDF file has been created successfully!");
    }

    private void toggleSort() {
        TableRowSorter<TableModel> sorter = new TableRowSorter<>(projectTable.getModel());
        projectTable.setRowSorter(sorter);
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
