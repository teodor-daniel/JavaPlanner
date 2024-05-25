package org.example.SwingComponents;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.example.Crud.CRUDemployees;
import org.example.Crud.CRUDprojects;
import org.example.Crud.CRUDtasks;
import org.example.Enum.ActivityState;
import org.example.Interfaces.IScreen;
import org.example.Models.Employee;
import org.example.Models.Project;
import org.example.Models.Task;
import org.example.Services.EmployeeService;
import org.example.Services.ProjectService;
import org.example.Services.TaskService;
import org.example.Validation.EmployeeValidation;
import org.example.Validation.ProjectValidation;
import org.example.Validation.TaskValidation;
import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.*;

public class TaskScreen extends JFrame implements IScreen {
    private final EmployeeService employeeService = new EmployeeService(new CRUDemployees(), new EmployeeValidation());
    private final ProjectService projectService = new ProjectService(new CRUDprojects(), new ProjectValidation());
    private final TaskService taskService;
    private final JTable taskTable;
    private final Connection conn;
    private final JFrame mainPage;
    private boolean sortAscending = true;

    public TaskScreen(Connection conn, JFrame mainPage) {
        this.conn = conn;
        this.mainPage = mainPage;
        this.taskService = new TaskService(new CRUDtasks(), new TaskValidation());

        setTitle("Task Data");
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
            TaskScreen.this.setVisible(false);
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

        taskTable = new JTable();
        add(new JScrollPane(taskTable), BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        loadData();
    }

    @Override
    public void loadData() {
        ArrayList<Task> tasks = (ArrayList<Task>) taskService.getAllTasks(conn);
        String[] columnNames = {"ID", "Name", "Description", "Status", "Due Date", "Assigned To", "Project ID", "Update", "Delete"};
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);

        for (Task task : tasks) {
            Object[] row = {
                    task.getId(),
                    task.getName(),
                    task.getDescription(),
                    task.getStatus(),
                    task.getDueDate(),
                    task.getAssignedTo(),
                    task.getProjectId(),
                    "Update",
                    "Delete"
            };
            tableModel.addRow(row);
        }

        taskTable.setModel(tableModel);

        taskTable.getColumn("Update").setCellRenderer(new ButtonRenderer());
        taskTable.getColumn("Update").setCellEditor(new ButtonEditor(new JCheckBox(), "Update", rowIndex -> updateData(rowIndex)));
        taskTable.getColumn("Delete").setCellRenderer(new ButtonRenderer());
        taskTable.getColumn("Delete").setCellEditor(new ButtonEditor(new JCheckBox(), "Delete", rowIndex -> confirmAndDeleteData(rowIndex)));
    }

    @Override
    public void openAddDataDialog() {
        JTextField nameField = new JTextField();
        JTextField descriptionField = new JTextField();
        JComboBox<ActivityState> statusComboBox = new JComboBox<>(ActivityState.values());
        JComboBox<Integer> assignedToComboBox = new JComboBox<>();
        JComboBox<Integer> projectComboBox = new JComboBox<>();

        java.util.List<Employee> employees = employeeService.getAllEmployees(conn);
        List<Project> projects = projectService.getAllProjects(conn);

        for (Employee employee : employees) {
            assignedToComboBox.addItem(employee.getId());
        }
        for (Project project : projects) {
            projectComboBox.addItem(project.getId());
        }

        // Date picker component
        UtilDateModel model = new UtilDateModel();
        Properties p = new Properties();
        p.put("text.today", "Today");
        p.put("text.month", "Month");
        p.put("text.year", "Year");
        JDatePanelImpl datePanel = new JDatePanelImpl(model, p);
        JDatePickerImpl datePicker = new JDatePickerImpl(datePanel, new DateLabelFormatter());

        JPanel panel = new JPanel(new GridLayout(6, 2));
        panel.add(new JLabel("Name:"));
        panel.add(nameField);
        panel.add(new JLabel("Description:"));
        panel.add(descriptionField);
        panel.add(new JLabel("Status:"));
        panel.add(statusComboBox);
        panel.add(new JLabel("Due Date:"));
        panel.add(datePicker);
        panel.add(new JLabel("Assigned To:"));
        panel.add(assignedToComboBox);
        panel.add(new JLabel("Project ID:"));
        panel.add(projectComboBox);

        int result = JOptionPane.showConfirmDialog(null, panel, "Add New Task", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            Date selectedDate = new Date(model.getValue().getTime());
            Task newTask = new Task(
                    nameField.getText(),
                    descriptionField.getText(),
                    selectedDate,
                    statusComboBox.getSelectedItem().toString(),
                    (Integer) assignedToComboBox.getSelectedItem(),
                    (Integer) projectComboBox.getSelectedItem()
            );
            taskService.addTask(conn, newTask);
            loadData();
        }
    }

    @Override
    public void updateData(int rowIndex) {
        DefaultTableModel model = (DefaultTableModel) taskTable.getModel();
        Integer id = (Integer) model.getValueAt(rowIndex, 0);
        Optional<Task> taskOptional = taskService.getTaskById(conn, id);

        if (taskOptional.isPresent()) {
            Task task = taskOptional.get();

            JTextField nameField = new JTextField(task.getName());
            JTextField descriptionField = new JTextField(task.getDescription());
            JComboBox<ActivityState> statusComboBox = new JComboBox<>(ActivityState.values());
            statusComboBox.setSelectedItem(ActivityState.valueOf(task.getStatus().toUpperCase()));

            JComboBox<Integer> assignedToComboBox = new JComboBox<>();
            JComboBox<Integer> projectComboBox = new JComboBox<>();

            List<Employee> employees = employeeService.getAllEmployees(conn);
            List<Project> projects = projectService.getAllProjects(conn);

            for (Employee employee : employees) {
                assignedToComboBox.addItem(employee.getId());
            }
            for (Project project : projects) {
                projectComboBox.addItem(project.getId());
            }

            assignedToComboBox.setSelectedItem(task.getAssignedTo());
            projectComboBox.setSelectedItem(task.getProjectId());

            // Date picker component with initial value
            UtilDateModel dateModel = new UtilDateModel(task.getDueDate());
            Properties p = new Properties();
            p.put("text.today", "Today");
            p.put("text.month", "Month");
            p.put("text.year", "Year");
            JDatePanelImpl datePanel = new JDatePanelImpl(dateModel, p);
            JDatePickerImpl datePicker = new JDatePickerImpl(datePanel, new DateLabelFormatter());

            JPanel panel = new JPanel(new GridLayout(6, 2));
            panel.add(new JLabel("Name:"));
            panel.add(nameField);
            panel.add(new JLabel("Description:"));
            panel.add(descriptionField);
            panel.add(new JLabel("Status:"));
            panel.add(statusComboBox);
            panel.add(new JLabel("Due Date:"));
            panel.add(datePicker);
            panel.add(new JLabel("Assigned To:"));
            panel.add(assignedToComboBox);
            panel.add(new JLabel("Project ID:"));
            panel.add(projectComboBox);

            int result = JOptionPane.showConfirmDialog(null, panel, "Update Task", JOptionPane.OK_CANCEL_OPTION);
            if (result == JOptionPane.OK_OPTION) {
                Date selectedDate = new Date(dateModel.getValue().getTime());
                task.setName(nameField.getText());
                task.setDescription(descriptionField.getText());
                task.setStatus(statusComboBox.getSelectedItem().toString());
                task.setDueDate(selectedDate);
                task.setAssignedTo((Integer) assignedToComboBox.getSelectedItem());
                task.setProjectId((Integer) projectComboBox.getSelectedItem());

                taskService.updateTask(conn, task);
                loadData();
            }
        }
    }

    @Override
    public void confirmAndDeleteData(int rowIndex) {
        DefaultTableModel model = (DefaultTableModel) taskTable.getModel();
        Integer id = (Integer) model.getValueAt(rowIndex, 0);

        int result = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to delete this task?",
                "Confirm Deletion",
                JOptionPane.YES_NO_OPTION
        );

        if (result == JOptionPane.YES_OPTION) {
            taskService.deleteTaskById(conn, id);
            loadData();
        }
    }

    private void saveTableDataToPDF() throws DocumentException, FileNotFoundException {
        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream("TaskData.pdf"));
        document.open();

        int numberOfColumns = taskTable.getColumnCount() - 2;
        PdfPTable pdfTable = new PdfPTable(numberOfColumns);

        for (int i = 0; i < numberOfColumns; i++) {
            pdfTable.addCell(taskTable.getColumnName(i));
        }

        for (int rows = 0; rows < taskTable.getRowCount(); rows++) {
            for (int cols = 0; cols < numberOfColumns; cols++) {
                pdfTable.addCell(taskTable.getModel().getValueAt(rows, cols).toString());
            }
        }

        document.add(pdfTable);
        document.close();
        JOptionPane.showMessageDialog(this, "PDF file has been created successfully!");
    }

    private void toggleSort() {
        TableRowSorter<TableModel> sorter = new TableRowSorter<>(taskTable.getModel());
        taskTable.setRowSorter(sorter);
        ArrayList<RowSorter.SortKey> sortKeys = new ArrayList<>();

        int columnIndexToSort = 1;
        sortKeys.add(new RowSorter.SortKey(columnIndexToSort, sortAscending ? SortOrder.ASCENDING : SortOrder.DESCENDING));

        sorter.setComparator(columnIndexToSort, Comparator.naturalOrder());

        sorter.setSortKeys(sortKeys);
        sorter.sort();

        sortAscending = !sortAscending;
    }

    private class DateLabelFormatter extends JFormattedTextField.AbstractFormatter {
        private final String datePattern = "yyyy-MM-dd";
        private final SimpleDateFormat dateFormatter = new SimpleDateFormat(datePattern);

        @Override
        public Object stringToValue(String text) throws java.text.ParseException {
            return dateFormatter.parseObject(text);
        }

        @Override
        public String valueToString(Object value) throws java.text.ParseException {
            if (value != null) {
                Calendar cal = (Calendar) value;
                return dateFormatter.format(cal.getTime());
            }
            return "";
        }
    }
}
