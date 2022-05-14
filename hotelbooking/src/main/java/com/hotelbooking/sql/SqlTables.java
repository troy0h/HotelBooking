package com.hotelbooking.sql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import com.hotelbooking.DialogBox;

public class SqlTables {
    static void executeSQL(Connection conn) {
        createCustomersTable(conn);
        createStaffTables(conn);
        createBookingTables(conn);
        createRoomsTables(conn);
    }

    static void createCustomersTable(Connection conn) {
        try {
            Statement stmt = conn.createStatement();
            // SQL Query to create "customers" table
            String query = "CREATE TABLE IF NOT EXISTS customers (" + 
                "customerId integer PRIMARY KEY," + 
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

    static void createBookingTables(Connection conn) {
        // Link table between rooms, customers and receptionists
        try {
            Statement stmt = conn.createStatement();
            String query = "CREATE TABLE IF NOT EXISTS bookings (" +
                "bookingId integer PRIMARY KEY," +
                "roomId integer NOT NULL," + 
                "userId integer NOT NULL," + 
                "timeOfStart text NOT NULL," + // YYYY-MM-DD
                "timeOfExit text NOT NULL," +  // YYYY-MM-DD
                "bookingPrice real NOT NULL," +
                "checkedIn integer NOT NULL );";
            stmt.executeUpdate(query);
        }
        catch (Exception ex) {
            DialogBox.Exception(ex);
        }
    }

    static void createRoomsTables(Connection conn) {
        // Check if the table exists
        boolean tExists = false;
        try (ResultSet rs = conn.getMetaData().getTables(null, null, "rooms", null)) {
            while (rs.next()) { 
                String tName = rs.getString("TABLE_NAME");
                if (tName != null && tName.equals("rooms")) {
                    tExists = true;
                }
            }
        }
        catch (Exception ex) {
            DialogBox.Exception(ex);
        }
        // If it doesnt, make the table...
        if (tExists == false) {
            try {
                Statement stmt = conn.createStatement();
                // SQL Query to create "rooms" table
                String query = "CREATE TABLE IF NOT EXISTS rooms (" + 
                    "roomId integer PRIMARY KEY," + 
                    "roomNum int UNIQUE NOT NULL," + 
                    "roomType text NOT NULL," +
                    "isBooked int NOT NULL);";
    
                stmt.executeUpdate(query);
            }
            catch (Exception ex) {
                DialogBox.Exception(ex);
            }
            
            // ... And then populate it
            try {
                int n = 100;
                PreparedStatement stmt = conn.prepareStatement("INSERT INTO rooms (roomNum, roomType, isBooked) VALUES (?, ?, ?)");
                while (n <= 109) {
                    stmt.setInt(1, n);
                    stmt.setString(2, "Single");
                    stmt.setInt(3, 0);
                    stmt.executeUpdate();
                    n += 1;
                }
                n = 200;
                while (n <= 209) {
                    stmt.setInt(1, n);
                    stmt.setString(2, "Single");
                    stmt.setInt(3, 0);
                    stmt.executeUpdate();
                    n += 1;
                }
                n = 300;
                while (n <= 309) {
                    stmt.setInt(1, n);
                    stmt.setString(2, "Single");
                    stmt.setInt(3, 0);
                    stmt.executeUpdate();
                    n += 1;
                }
                n = 400;
                while (n <= 409) {
                    stmt.setInt(1, n);
                    stmt.setString(2, "Single");
                    stmt.setInt(3, 0);
                    stmt.executeUpdate();
                    n += 1;
                }
                n = 500;
                while (n <= 509) {
                    stmt.setInt(1, n);
                    stmt.setString(2, "Double");
                    stmt.setInt(3, 0);
                    stmt.executeUpdate();
                    n += 1;
                }
                n = 600;
                while (n <= 609) {
                    stmt.setInt(1, n);
                    stmt.setString(2, "Double");
                    stmt.setInt(3, 0);
                    stmt.executeUpdate();
                    n += 1;
                }
                n = 700;
                while (n <= 709) {
                    stmt.setInt(1, n);
                    stmt.setString(2, "Twin");
                    stmt.setInt(3, 0);
                    stmt.executeUpdate();
                    n += 1;
                }
                n += 1;
                n = 800;
                while (n <= 809) {
                    stmt.setInt(1, n);
                    stmt.setString(2, "Twin");
                    stmt.setInt(3, 0);
                    stmt.executeUpdate();
                    n += 1;
                }
                
            }
            catch (Exception ex) {
                DialogBox.Exception(ex);
            }

        }
    }
}
