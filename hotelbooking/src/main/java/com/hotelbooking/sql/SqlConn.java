package com.hotelbooking.sql;

import java.sql.Connection;
import java.sql.DriverManager;

import com.hotelbooking.DialogBox;

public class SqlConn {
    public static Connection Connect() {
        Connection conn = null;
        try {
            // db parameters
            String url = "jdbc:sqlite:database.db";
            // create a connection to the database
            conn = DriverManager.getConnection(url);           
        }
        // If theres an error when conencting to the database, output it to the user.
        catch (Exception ex) {
            DialogBox.Exception(ex);
        }

        return conn;
    }
}
