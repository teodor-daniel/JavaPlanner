package org.example;

import junit.framework.TestCase;
import org.example.Models.Company;
import org.example.Private.Sensitive;
import org.example.Crud.CRUDcompany;
import org.example.Crud.CRUDemployees;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Unit test for simple App.
 */
public class AppTest extends TestCase {
    private Connection conn;
    CRUDemployees crudEmployees = new CRUDemployees();
    CRUDcompany crudCompany = new CRUDcompany();
    /**
     * Set up your test environment, database connection, etc.
     */
    protected void setUp() throws Exception {
        super.setUp();
        // Initialize database connection
        String url = Sensitive.URL; // Use your test database credentials
        String user = Sensitive.USER;
        String password = Sensitive.PASSWORD;
        try {
            conn = DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            fail("Error connecting to database: " + e.getMessage());
        }
    }

    /**
     * Clean up after tests
     */
    protected void tearDown() throws Exception {
        super.tearDown();
        // Close the database connection
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                fail("Error closing database connection: " + e.getMessage());
            }
        }
    }

    /**
     * Test to add and then delete a company
     */
    public void testAddAndDeleteCompany() {
        Company newCompany = new Company( "TestCompany", "123 Test St", "123456789", "123-456-7890", "test@test.com");
        crudCompany.save(conn, newCompany);

        // Check if company was added
        ArrayList<Company> companies = (ArrayList<Company>) crudCompany.findAll(conn);
        Company fetchedCompany = companies.get(0);
        assertNotNull("Company should not be null", fetchedCompany);
        Integer companyId = fetchedCompany.getId();
        // Delete the company
        crudCompany.deleteById(conn, fetchedCompany.getId());

        // Check if company was deleted
        companies = (ArrayList<Company>) crudCompany.findAll(conn);
        assertEquals("Company should be null", companies.size() == 0);
    }

    public void testAddAndDeleteEmployee() throws SQLException {

//        Employee e = new Employee("Test", 42, "123-456-7890", "test@email.com", 90000.00, );
//        crudEmployees.save(conn, e);
//        Optional<Employee> fetchedEmployee = crudEmployees.findById(conn, 1);
//        assertNotNull("Employee should not be null", fetchedEmployee);
//        crudEmployees.deleteById(conn, 1);
//        Optional<Employee> deletedEmployee = crudEmployees.findById(conn, 1);
//        assertNull("Employee should be null", deletedEmployee);
    }

}
