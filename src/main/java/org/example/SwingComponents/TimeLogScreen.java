package org.example.SwingComponents;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.example.Crud.CRUDemployees;
import org.example.Crud.CRUDtasks;
import org.example.Crud.CRUDtimeLogs;
import org.example.Interfaces.IScreen;
import org.example.Models.Employee;
import org.example.Models.Task;
import org.example.Models.TimeLog;
import org.example.Services.EmployeeService;
import org.example.Services.TaskService;
import org.example.Services.TimeLogService;
import org.example.Validation.EmployeeValidation;
import org.example.Validation.TaskValidation;
import org.example.Validation.TimeLogValidation;
import org.jdatepicker.impl.*;

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
import java.util.*;
import java.util.List;

public class TimeLogScreen extends JFrame implements IScreen {
    private JTable timeLogTable;
    private Connection conn;
    private JFrame mainPage;
    private final TaskService taskService = new TaskService(new CRUDtasks(), new TaskValidation());
    private final EmployeeService employeeService = new EmployeeService(new CRUDemployees(), new EmployeeValidation());
    private final TimeLogService timeLogService;
    private boolean sortAscending = true;

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
        JButton pdfButton = new JButton("Get PDF");
        JButton sortButton = new JButton("Sort by Name");

        addButton.addActionListener(e -> openAddDataDialog());
        backButton.addActionListener(e -> {
            TimeLogScreen.this.setVisible(false);
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
        JTextField hoursLoggedField = new JTextField();
        JTextField descriptionField = new JTextField();

        JComboBox<Integer> taskComboBox = new JComboBox<>();
        JComboBox<Integer> employeeComboBox = new JComboBox<>();

        List<Task> tasks = taskService.getAllTasks(conn);
        List<Employee> employees = employeeService.getAllEmployees(conn);

        for (Task task : tasks) {
            taskComboBox.addItem(task.getId());
        }
        for (Employee employee : employees) {
            employeeComboBox.addItem(employee.getId());
        }

        // Date picker component
        UtilDateModel model = new UtilDateModel();
        Properties p = new Properties();
        p.put("text.today", "Today");
        p.put("text.month", "Month");
        p.put("text.year", "Year");
        JDatePanelImpl datePanel = new JDatePanelImpl(model, p);
        JDatePickerImpl datePicker = new JDatePickerImpl(datePanel, new DateLabelFormatter());

        JPanel panel = new JPanel(new GridLayout(5, 2));
        panel.add(new JLabel("Task ID:"));
        panel.add(taskComboBox);
        panel.add(new JLabel("Employee ID:"));
        panel.add(employeeComboBox);
        panel.add(new JLabel("Hours Logged:"));
        panel.add(hoursLoggedField);
        panel.add(new JLabel("Log Date:"));
        panel.add(datePicker);
        panel.add(new JLabel("Description:"));
        panel.add(descriptionField);

        int result = JOptionPane.showConfirmDialog(null, panel, "Add New Time Log", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            Date selectedDate = new Date(model.getValue().getTime());
            TimeLog newTimeLog = new TimeLog(
                    (Integer) taskComboBox.getSelectedItem(),
                    (Integer) employeeComboBox.getSelectedItem(),
                    Double.parseDouble(hoursLoggedField.getText()),
                    selectedDate,
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

            JTextField hoursLoggedField = new JTextField(String.valueOf(timeLog.getHoursLogged()));
            JTextField descriptionField = new JTextField(timeLog.getDescription());

            JComboBox<Integer> taskComboBox = new JComboBox<>();
            JComboBox<Integer> employeeComboBox = new JComboBox<>();

            List<Task> tasks = taskService.getAllTasks(conn);
            List<Employee> employees = employeeService.getAllEmployees(conn);

            for (Task task : tasks) {
                taskComboBox.addItem(task.getId());
            }

            for (Employee employee : employees) {
                employeeComboBox.addItem(employee.getId());
            }

            taskComboBox.setSelectedItem(timeLog.getTaskId());
            employeeComboBox.setSelectedItem(timeLog.getEmployeeId());

            // Date picker component with initial value
            UtilDateModel dateModel = new UtilDateModel(timeLog.getLogDate());
            Properties p = new Properties();
            p.put("text.today", "Today");
            p.put("text.month", "Month");
            p.put("text.year", "Year");
            JDatePanelImpl datePanel = new JDatePanelImpl(dateModel, p);
            JDatePickerImpl datePicker = new JDatePickerImpl(datePanel, new DateLabelFormatter());

            JPanel panel = new JPanel(new GridLayout(5, 2));
            panel.add(new JLabel("Hours Logged:"));
            panel.add(hoursLoggedField);
            panel.add(new JLabel("Log Date:"));
            panel.add(datePicker);
            panel.add(new JLabel("Description:"));
            panel.add(descriptionField);
            panel.add(new JLabel("Task ID:"));
            panel.add(taskComboBox);
            panel.add(new JLabel("Employee ID:"));
            panel.add(employeeComboBox);

            int result = JOptionPane.showConfirmDialog(null, panel, "Update Time Log", JOptionPane.OK_CANCEL_OPTION);
            if (result == JOptionPane.OK_OPTION) {
                Date selectedDate = new Date(dateModel.getValue().getTime());
                timeLog.setTaskId((Integer) taskComboBox.getSelectedItem());
                timeLog.setEmployeeId((Integer) employeeComboBox.getSelectedItem());
                timeLog.setHoursLogged(Double.parseDouble(hoursLoggedField.getText()));
                timeLog.setLogDate(selectedDate);
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
            timeLogService.deleteTimeLogById(conn, id);
            loadData();
        }
    }

    private void saveTableDataToPDF() throws DocumentException, FileNotFoundException {
        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream("LogData.pdf"));
        document.open();

        int numberOfColumns = timeLogTable.getColumnCount() - 2;
        PdfPTable pdfTable = new PdfPTable(numberOfColumns);

        for (int i = 0; i < numberOfColumns; i++) {
            pdfTable.addCell(timeLogTable.getColumnName(i));
        }

        for (int rows = 0; rows < timeLogTable.getRowCount(); rows++) {
            for (int cols = 0; cols < numberOfColumns; cols++) {
                pdfTable.addCell(timeLogTable.getModel().getValueAt(rows, cols).toString());
            }
        }

        document.add(pdfTable);
        document.close();
        JOptionPane.showMessageDialog(this, "PDF file has been created successfully!");
    }


    private void toggleSort() {
        TableRowSorter<TableModel> sorter = new TableRowSorter<>(timeLogTable.getModel());
        timeLogTable.setRowSorter(sorter);
        ArrayList<RowSorter.SortKey> sortKeys = new ArrayList<>();

        int columnIndexToSort = 6;
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
