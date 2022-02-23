package com.hotelbooking.sql;

import java.sql.Connection;
import java.sql.DriverManager;

import com.hotelbooking.DialogBox;

public class SqlStart {
    //Connect to a sample database, and make database

    public static void firstConnect() {
        Connection conn = null;
        try {
            // db parameters
            String url = "jdbc:sqlite:database.db";
            // create a connection to the database
            conn = DriverManager.getConnection(url);           
        } 
        catch (Exception ex) {
            DialogBox.Exception(ex);
        }
        finally {
            try {
                // Execute things needed in sqlStatements file
                SqlTables.executeSQL(conn);

                if (conn != null)
                    conn.close();
            }
            catch (Exception ex) {
                DialogBox.Exception(ex);
            }
        }
    }
}
