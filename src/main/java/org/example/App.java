package org.example;

import org.example.Crud.*;
import org.example.Models.*;
import org.example.Private.Sensitive;
import org.example.Services.*;
import org.example.Validation.*;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
public class App 
{
    public static void main(String[] args) {
        Connection conn = null;
        try {
            //driver for jdbc
            Class.forName("org.postgresql.Driver");

            //connection string.
            String url = Sensitive.URL;
            String user = Sensitive.USER;
            String password = Sensitive.PASSWORD;

            conn = DriverManager.getConnection(url, user, password);
            System.out.println("Connected to the PostgreSQL server successfully.");
            addTestEmployee(conn);

            //Testing CRUDS:
            //addTestCompany(conn);
            //addTestDepartment(conn);
//            addTestProject(conn);
//            addTestTask(conn);
            addTestTimeLog(conn);
            conn.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } catch (ClassNotFoundException e) {
            System.out.println("PostgreSQL JDBC Driver not found.");
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        }

    }

    public static void addTestCompany(Connection conn) {
        CRUDcompany crudCompany = new CRUDcompany();
        CompanyValidation companyValidationService = new CompanyValidation();
        CompanyService companyService = new CompanyService(crudCompany, companyValidationService);

        Company newCompany = new Company("Valid Test Company", "123 Test Street", "REG-12345", "1234567890", "test@company.com");
        companyService.addCompany(conn, newCompany);
        System.out.println("Company added successfully.");

        List<Company> companies = companyService.getAllCompanies(conn);
        System.out.println("All companies: " + companies);

        // Test Find by ID (Read)
        Optional<Company> retrievedCompany = companyService.getCompanyById(conn, companies.get(0).getId());
        if (retrievedCompany.isPresent()) {
            System.out.println("Retrieved company by ID: " + retrievedCompany.get());
        } else {
            System.out.println("Company not found by ID.");
        }

        Company updateCompany = companies.get(0);
        updateCompany.setName("Updated Valid Test Company Inc.");
        companyService.updateCompany(conn, updateCompany);
        System.out.println("Company updated successfully.");

    }

    public static void addTestDepartment(Connection conn){
        CRUDdepartments crudDepartments = new CRUDdepartments();
        DepartmentValidation departmentValidationService = new DepartmentValidation();
        DepartmentService departmentService = new DepartmentService(crudDepartments, departmentValidationService);

        Department newDepartment = new Department("Engineering", 2);
        departmentService.addDepartment(conn, newDepartment);
        System.out.println("Department added successfully.");

        List<Department> departments = departmentService.getAllDepartments(conn);
        System.out.println("All departments: " + departments);

        Optional<Department> retrievedDepartment = departmentService.getDepartmentById(conn, departments.get(0).getId());
        if (retrievedDepartment.isPresent()) {
            System.out.println("Retrieved department by ID: " + retrievedDepartment.get());
        } else {
            System.out.println("Department not found by ID.");
        }

        Department updateDepartment = departments.get(0);
        updateDepartment.setName("Updated Engineering");
        departmentService.updateDepartment(conn, updateDepartment);
        System.out.println("Department updated successfully.");

//        departmentService.deleteDepartmentById(conn, updateDepartment.getId());
//        System.out.println("Department deleted successfully.");

    }

        public static void addTestEmployee(Connection conn) {
            CRUDemployees crudEmployees = new CRUDemployees();
            EmployeeValidation employeeValidationService = new EmployeeValidation();
            EmployeeService employeeService = new EmployeeService(crudEmployees, employeeValidationService);

            CRUDdepartments crudDepartments = new CRUDdepartments();
            Optional<Department> optionalDepartment = crudDepartments.findById(conn,5);
            if(optionalDepartment.isPresent()) {
                Department existingDepartment = optionalDepartment.get();

                Employee newEmployee = new Employee("Teo", 30, "1234567890", "teo@yahoo.com", 60000.00, existingDepartment,  null, existingDepartment.getCompanyId(), "Working");
                employeeService.addEmployee(conn, newEmployee);
                System.out.println("Employee added successfully.");

                List<Employee> employees = employeeService.getAllEmployees(conn);
                System.out.println("All employees: " + employees);

                 Optional<Employee> retrievedEmployee = employeeService.getEmployeeById(conn, employees.get(0).getId());
                if (retrievedEmployee.isPresent()) {
                    System.out.println("Retrieved employee by ID: " + retrievedEmployee.get());
                } else {
                    System.out.println("Employee not found by ID.");
                }

                Employee updateEmployee = employees.get(0);
                updateEmployee.setName("Teodor");
                updateEmployee.setSalary(65000.00);
                employeeService.updateEmployee(conn, updateEmployee);
                System.out.println("Employee updated successfully.");
            }
        }

    public static void addTestProject(Connection conn) {
        CRUDprojects crudProjects = new CRUDprojects();
        ProjectValidation projectValidationService = new ProjectValidation();
        ProjectService projectService = new ProjectService(crudProjects, projectValidationService);

        CRUDdepartments crudDepartments = new CRUDdepartments();
        CRUDemployees crudEmployees = new CRUDemployees();
        Optional<Department> optionalDepartment = crudDepartments.findById(conn, 1);
        Optional<Employee> optionalLead = crudEmployees.findById(conn, 1);

        if (optionalDepartment.isPresent() && optionalLead.isPresent()) {
            Department existingDepartment = optionalDepartment.get();
            Employee leadEmployee = optionalLead.get();

            Project newProject = new Project(
                    "New Development Project",
                    "Not Started",
                    120000.00,
                    existingDepartment.getId(),
                    leadEmployee.getId()
            );

            projectService.addProject(conn, newProject);
            System.out.println("Project added successfully.");

            List<Project> projects = projectService.getAllProjects(conn);
            System.out.println("All projects: " + projects);

            Optional<Project> retrievedProject = projectService.getProjectById(conn, projects.get(0).getId());
            if (retrievedProject.isPresent()) {
                System.out.println("Retrieved project by ID: " + retrievedProject.get());
            } else {
                System.out.println("Project not found by ID.");
            }


            Project updateProject = projects.get(0);
            updateProject.setName("Updated Project Name");
            updateProject.setStatus("In Progress");
            projectService.updateProject(conn, updateProject);
            System.out.println("Project updated successfully.");
        } else {
            System.out.println("Department or lead not found. Verify that the IDs are correct.");
        }
    }

    public static void addTestTask(Connection conn) {
        CRUDtasks crudTasks = new CRUDtasks();
        TaskValidation taskValidationService = new TaskValidation();
        TaskService taskService = new TaskService(crudTasks, taskValidationService);

        CRUDemployees crudEmployees = new CRUDemployees();
        Optional<Employee> optionalEmployee = crudEmployees.findById(conn, 1);

        CRUDprojects crudProjects = new CRUDprojects();
        Optional<Project> optionalProject = crudProjects.findById(conn, 1);
        if (optionalEmployee.isPresent() && optionalProject.isPresent()) {
            Employee assignedEmployee = optionalEmployee.get();
            Project associatedProject = optionalProject.get();

            Task newTask = new Task(
                    "Initial Development Phase",
                    "Develop the core modules for the project",
                    Date.valueOf("2024-06-30"),
                    "In Progress",
                    assignedEmployee.getId(),
                    associatedProject.getId()

            );

            taskService.addTask(conn, newTask);
            System.out.println("Task added successfully.");

            List<Task> tasks = taskService.getAllTasks(conn);
            System.out.println("All tasks: " + tasks);

            Optional<Task> retrievedTask = taskService.getTaskById(conn, tasks.get(0).getId());
            if (retrievedTask.isPresent()) {
                System.out.println("Retrieved task by ID: " + retrievedTask.get());
            } else {
                System.out.println("Task not found by ID.");
            }

            Task updateTask = tasks.get(0);
            updateTask.setDescription("Refine the core modules and start integrating other components");
            updateTask.setStatus("Completed");
            taskService.updateTask(conn, updateTask);
            System.out.println("Task updated successfully.");
        } else {
            System.out.println("Employee or project not found. Verify that the IDs are correct.");
        }
    }

    public static void addTestTimeLog(Connection conn) {
        CRUDtimeLogs crudTimeLogs = new CRUDtimeLogs();
        TimeLogValidation timeLogValidator = new TimeLogValidation();
        TimeLogService timeLogService = new TimeLogService(crudTimeLogs, timeLogValidator);


        TimeLog newTimeLog = new TimeLog(
                1,
                1,
                8.0,
                java.sql.Date.valueOf("2024-05-04"),
                "Worked on feature implementation"
        );

        timeLogService.addTimeLog(conn, newTimeLog);
        System.out.println("Time log added successfully.");

        List<TimeLog> timeLogs = timeLogService.getAllTimeLogs(conn);
        System.out.println("All time logs: " + timeLogs);

        Optional<TimeLog> retrievedTimeLog = timeLogService.getTimeLogById(conn, timeLogs.get(0).getId());
        if (retrievedTimeLog.isPresent()) {
            System.out.println("Retrieved time log by ID: " + retrievedTimeLog.get());
        } else {
            System.out.println("Time log not found by ID.");
        }

        TimeLog updateTimeLog = timeLogs.get(0);
        updateTimeLog.setHoursLogged(9.0);
        updateTimeLog.setDescription("Updated work description");
        timeLogService.updateTimeLog(conn, updateTimeLog);
        System.out.println("Time log updated successfully.");
    }


}


