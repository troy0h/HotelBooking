package com.hotelbooking.sql;

import java.sql.Connection;
import java.sql.Statement;

import com.hotelbooking.DialogBox;

public class SqlTables {
    static void executeSQL(Connection conn) {
        createCustomersTable(conn);
        createStaffTables(conn);
    }

    static void createCustomersTable(Connection conn) {
        try {
            Statement stmt = conn.createStatement();
            // SQL Query to create "customers" table
            String query = "CREATE TABLE IF NOT EXISTS customers (" + 
                "userId integer PRIMARY KEY," + 
                "username text UNIQUE NOT NULL," + 
                "password text NOT NULL," +
                "email text NOT NULL," +
                "paymentMethod text NOT NULL," +
                "corporateClient text NOT NULL );";

            stmt.executeUpdate(query);
        }
        catch (Exception ex) {
            DialogBox.Exception(ex);
        }
    }

    static void createStaffTables(Connection conn) {
        try {
            Statement stmt = conn.createStatement();
            // SQL Query to create "staff" table
            String query = "CREATE TABLE IF NOT EXISTS staff (" + 
                "staffId integer PRIMARY KEY," + 
                "username text UNIQUE NOT NULL," + 
                "password text NOT NULL," +
                "staffType text NOT NULL );";

            stmt.executeUpdate(query);
        }
        catch (Exception ex) {
            DialogBox.Exception(ex);
        }
    }
}
