package org.example;

import junit.framework.TestCase;
import org.example.Models.Company;
import org.example.Private.Sensitive;
import org.example.Services.CRUDcompany;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Unit test for simple App.
 */
public class AppTest extends TestCase {
    private Connection conn;

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
        Company newCompany = new Company(1, "TestCompany", "123 Test St", "123456789", "123-456-7890", "test@test.com");
        CRUDcompany.saveToDataBase(conn, newCompany);

        // Check if company was added
        Company fetchedCompany = CRUDcompany.findById(conn, 1);
        assertNotNull("Company should not be null", fetchedCompany);

        // Delete the company
        CRUDcompany.deleteCompany(conn, 1);

        // Check if company was deleted
        Company deletedCompany = CRUDcompany.findById(conn, 1);
        assertNull("Company should be null", deletedCompany);
    }

}
