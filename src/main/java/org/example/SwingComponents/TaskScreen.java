    package org.example.SwingComponents;

    import org.example.Crud.CRUDtasks;
    import org.example.Interfaces.ScreenInterface;
    import org.example.Models.Task;
    import org.example.Services.TaskService;
    import org.example.Validation.TaskValidationService;

    import javax.swing.*;
    import javax.swing.table.DefaultTableModel;
    import java.awt.*;
    import java.sql.Connection;
    import java.sql.Date;
    import java.util.ArrayList;
    import java.util.Optional;

    public class TaskScreen extends JFrame implements ScreenInterface {
        private JTable taskTable;
        private Connection conn;
        private JFrame mainPage;
        private final TaskService taskService;

        public TaskScreen(Connection conn, JFrame mainPage) {
            this.conn = conn;
            this.mainPage = mainPage;
            this.taskService = new TaskService(new CRUDtasks(), new TaskValidationService());

            setTitle("Task Data");
            setSize(800, 600);
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            setLayout(new BorderLayout());

            JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            JButton addButton = new JButton("Add");
            JButton backButton = new JButton("Back");

            addButton.addActionListener(e -> openAddTaskDialog());
            backButton.addActionListener(e -> {
                TaskScreen.this.setVisible(false);
                mainPage.setVisible(true);
            });

            bottomPanel.add(addButton);
            bottomPanel.add(backButton);

            taskTable = new JTable();
            add(new JScrollPane(taskTable), BorderLayout.CENTER);
            add(bottomPanel, BorderLayout.SOUTH);

            loadData();
        }


        private void openAddTaskDialog() {

        }

        @Override
        public void loadData() {
            ArrayList<Task> tasks = (ArrayList<Task>) taskService.getAllTasks(conn);
            String[] columnNames = {"ID", "Name", "Description", "Due Date", "Status", "Assigned To", "Project ID", "Update", "Delete"};
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
            JTextField statusField = new JTextField();
            JTextField dueDateField = new JTextField();
            JTextField assignedToField = new JTextField();
            JTextField projectIdField = new JTextField();

            JPanel panel = new JPanel(new GridLayout(6, 2));
            panel.add(new JLabel("Name:"));
            panel.add(nameField);
            panel.add(new JLabel("Description:"));
            panel.add(descriptionField);
            panel.add(new JLabel("Status:"));
            panel.add(statusField);
            panel.add(new JLabel("Due Date:"));
            panel.add(dueDateField);
            panel.add(new JLabel("Assigned To:"));
            panel.add(assignedToField);
            panel.add(new JLabel("Project ID:"));
            panel.add(projectIdField);

            int result = JOptionPane.showConfirmDialog(null, panel, "Add New Task", JOptionPane.OK_CANCEL_OPTION);
            if (result == JOptionPane.OK_OPTION) {
                Task newTask = new Task(
                        nameField.getText(),
                        descriptionField.getText(),
                        Date.valueOf(dueDateField.getText()),
                        statusField.getText(),
                        Integer.parseInt(assignedToField.getText()),
                        Integer.parseInt(projectIdField.getText())
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
                JTextField statusField = new JTextField(task.getStatus());
                JTextField dueDateField = new JTextField(String.valueOf(task.getDueDate()));
                JTextField projectIdField = new JTextField(String.valueOf(task.getProjectId()));

                JPanel panel = new JPanel(new GridLayout(5, 2));
                panel.add(new JLabel("Name:"));
                panel.add(nameField);
                panel.add(new JLabel("Description:"));
                panel.add(descriptionField);
                panel.add(new JLabel("Status:"));
                panel.add(statusField);
                panel.add(new JLabel("Due Date:"));
                panel.add(dueDateField);
                panel.add(new JLabel("Project ID:"));
                panel.add(projectIdField);

                int result = JOptionPane.showConfirmDialog(null, panel, "Update Task", JOptionPane.OK_CANCEL_OPTION);
                if (result == JOptionPane.OK_OPTION) {
                    task.setName(nameField.getText());
                    task.setDescription(descriptionField.getText());
                    task.setStatus(statusField.getText());
                    task.setDueDate((Date.valueOf(dueDateField.getText())));
                    task.setProjectId(Integer.parseInt(projectIdField.getText()));
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
    }
