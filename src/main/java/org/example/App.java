package org.example;

import org.example.Models.Employee;
import org.example.Models.Project;
import org.example.Models.Task;
import org.example.Models.TeamLead;
import org.example.Private.Sensitive;
import org.example.Services.CRUDemployees;
import org.example.Services.SalaryComparator;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;

/**
 * Hello world!
 *
 */

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


//            Employee e = new Employee("Teo", 23, "123", "teo@a", 2000, "hr");
            CRUDemployees cruDemployees =  new CRUDemployees();
            ArrayList<Employee> employeeArrayList = new ArrayList<>();
            employeeArrayList.addAll(CRUDemployees.loadAllEmployees(conn));
            System.out.println(employeeArrayList);
            //close.
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
//
//        Employee e = new Employee("Teo", 23, "123", "teo@a", 2000, "hr");
//        TeamLead t = new TeamLead("Teoo", 24, "234", "teoo@a",  2500,"hr", 2000000);
//        t.hireEmployee(e);
//        t.hireEmployee(new Employee("Alice", 30, "123-456-7890", "alice@example.com", 50000, "IT"));
//        Collections.sort(t.getSubordinates(), new SalaryComparator());
//        System.out.println(t.getSubordinates());
//        Calendar futureDate = Calendar.getInstance();
//        futureDate.add(Calendar.DAY_OF_MONTH, 10); // 10 days from now
//        Task futureTask = new Task("Future Task", futureDate.getTime(), "hr");
//        System.out.println("ID: " + futureTask.getId() + ", Is Urgent: " + futureTask.isUrgent());
//
//        Calendar pastDate = Calendar.getInstance();
//        pastDate.add(Calendar.DAY_OF_MONTH, -10);
//        Task pastTask = new Task("Past Task", pastDate.getTime(), "it");
//        System.out.println("Test Case 2 - Task ID: " + pastTask.getId() + ", Is Urgent: " + pastTask.isUrgent());
//
//        Date startDate = new Date();
//        Date endDate = new Date(startDate.getTime() + 7 * 24 * 60 * 60 * 1000);
//        Date deadline = new Date(startDate.getTime() + 14 * 24 * 60 * 60 * 1000);
//
//        Project project = new Project("New Project", startDate, endDate, deadline, "IT", "IN_PROGRESS", 100000, t);
//        project.addTask(pastTask);
//        project.addTask(futureTask);
//        System.out.println(project);

    }
}
