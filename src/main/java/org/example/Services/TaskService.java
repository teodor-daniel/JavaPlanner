package org.example.Services;

import org.example.Crud.CRUDtasks;
import org.example.Validation.TaskValidation;
import org.example.Models.Task;

import java.sql.Connection;
import java.util.List;
import java.util.Optional;

public class TaskService {
    private final CRUDtasks crudTasks;
    private final TaskValidation taskValidator;

    public TaskService(CRUDtasks crudTasks, TaskValidation taskValidator) {
        this.crudTasks = crudTasks;
        this.taskValidator = taskValidator;
    }

    public void addTask(Connection conn, Task task) {
        if (!taskValidator.validate(task)) {
            throw new IllegalArgumentException("Invalid task data");
        }

        crudTasks.save(conn, task);
    }

    public List<Task> getAllTasks(Connection conn) {
        return crudTasks.findAll(conn);
    }

    public Optional<Task> getTaskById(Connection conn, Integer id) {
        return crudTasks.findById(conn, id);
    }

    public void updateTask(Connection conn, Task task) {
        if (!taskValidator.validate(task)) {
            throw new IllegalArgumentException("Invalid task data");
        }

        crudTasks.update(conn, task);
    }

    public void deleteTaskById(Connection conn, Integer id) {
        crudTasks.deleteById(conn, id);
    }
}
