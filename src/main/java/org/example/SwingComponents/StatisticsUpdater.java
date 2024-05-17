package org.example.SwingComponents;

import org.example.Crud.*;
import org.example.Services.*;
import org.example.Validation.*;

import javax.swing.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class StatisticsUpdater {
    private Connection conn;
    private CompanyService companyService;
    private DepartmentService departmentService;
    private EmployeeService employeeService;
    private ProjectService projectService;
    private TaskService taskService;
    private TimeLogService timeLogService;

    public StatisticsUpdater(Connection conn) {
        this.conn = conn;
        companyService = new CompanyService(new CRUDcompany(), new CompanyValidation());
        departmentService = new DepartmentService(new CRUDdepartments(), new DepartmentValidation());
        employeeService = new EmployeeService(new CRUDemployees(), new EmployeeValidation());
        projectService = new ProjectService(new CRUDprojects(), new ProjectValidation());
        taskService = new TaskService(new CRUDtasks(), new TaskValidation());
        timeLogService = new TimeLogService(new CRUDtimeLogs(), new TimeLogValidation());

    }

    public void updateStatistics(JLabel companyStats, JLabel departmentStats, JLabel employeeStats, JLabel projectStats, JLabel taskStats, JLabel timelogStats) {
        try {

            companyStats.setText("Companies: " +  companyService.getAllCompanies(conn).size());
            departmentStats.setText("Departments: " + departmentService.getAllDepartments(conn).size());
            employeeStats.setText("Employees: " + employeeService.getAllEmployees(conn).size());
            projectStats.setText("Projects: " + projectService.getAllProjects(conn).size());
            taskStats.setText("Tasks: " + taskService.getAllTasks(conn).size());
            timelogStats.setText("Time Logs: " + timeLogService.getAllTimeLogs(conn).size());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
