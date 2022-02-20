package com.hotelbooking.sql;

import java.sql.*;

public class SqlTables {
    static void executeSQL(Connection conn) throws SQLException {
        createUsersTables(conn);
    }

    static void createUsersTables(Connection conn) throws SQLException {
        Statement stmt = conn.createStatement();
        // SQL Query to create "users" table
        String query = "CREATE TABLE IF NOT EXISTS users (" + 
            "userId integer PRIMARY KEY," + 
            "username text UNIQUE NOT NULL," + 
            "password text NOT NULL );";
        stmt.executeUpdate(query);
    }
}
