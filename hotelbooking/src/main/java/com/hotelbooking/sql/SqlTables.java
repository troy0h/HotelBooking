package com.hotelbooking.sql;

import java.sql.Connection;
import java.sql.Statement;

import com.hotelbooking.DialogBox;

public class SqlTables {
    static void executeSQL(Connection conn) {
        createUsersTables(conn);
    }

    static void createUsersTables(Connection conn) {
        try {
            Statement stmt = conn.createStatement();
            // SQL Query to create "users" table
            String query = "CREATE TABLE IF NOT EXISTS users (" + 
                "userId integer PRIMARY KEY," + 
                "username text UNIQUE NOT NULL," + 
                "password text NOT NULL," +
                "name text NOT NULL," +
                "isAdmin int NOT NULL );";

            stmt.executeUpdate(query);
        }
        catch (Exception ex) {
            DialogBox.Exception(ex);
        }
    }
}
