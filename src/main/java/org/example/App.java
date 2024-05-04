package org.example;

import org.example.Models.*;
import org.example.Private.Sensitive;
import org.example.Crud.CRUDcompany;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

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
            CRUDcompany crudCompany = new CRUDcompany();
            System.out.println("Connected to the PostgreSQL server successfully.");
            //Testing CRUDS:
            Company c1 = new Company("Company1", "Address1", "123456789", "123-456-7890", "email@ceva");

            crudCompany.findAll(conn).forEach(System.out::println);

            crudCompany.deleteById(conn, 1);
            if(!crudCompany.findById(conn, 1).isPresent()){
                System.out.println("Company deleted successfully");
            }
            //
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
}
