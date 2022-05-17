package com.controllers.staff;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.hotelbooking.App;
import com.hotelbooking.DialogBox;
import com.hotelbooking.sql.SqlConn;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

public class ViewBookingController {

    @FXML ComboBox<String>  staffViewBookingSelected;
    @FXML TextField         staffViewBookingArrival;
    @FXML TextField         staffViewBookingDepart;
    @FXML TextField         staffViewBookingType;
    @FXML TextField         staffViewBookingNumber;
    @FXML TextField         staffViewBookingCost;
    @FXML TextField         staffViewBookingChecked;
    @FXML TextField         staffViewBookingUser;

    String      staffBooking = "";
    String      arriveDate = "";
    String      departDate = "";
    String      dropDownOption = "";
    Integer     roomNumber = 0;
    Integer     roomId = 0;
    Integer     userId = 0;
    String      userType = "";
    String      username = "";
    Integer     checkedIn = 0;
    Integer     toCheckIn = 0;

    // Populate the combo box with bookings made types
    @FXML
    protected void initialize() {
        try {
            Connection conn = SqlConn.Connect();
            // Get all bookings
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM bookings");
            ResultSet rs = stmt.executeQuery();
            // While there is a current line:
            while (rs.next()) {
                // Get the arrival and departure date, and room number
                arriveDate = rs.getString(5);
                departDate = rs.getString(6);
                roomId = rs.getInt(2);
                roomNumber = getRoomNumber(roomId);
                // Make an entry in the format "NNN: YYYY-MM-DD - YYYY-MM-DD"
                staffBooking = roomNumber + ": " + arriveDate + " - "  + departDate;
                // Add it to the combo box
                staffViewBookingSelected.getItems().add(staffBooking);
            }
            conn.close();
        }
        catch (Exception ex) {
            DialogBox.Exception(ex);
        }
    }

    private int getRoomNumber(Integer roomId) {
        try {
            Connection conn = SqlConn.Connect();
            PreparedStatement stmt = conn.prepareStatement("SELECT roomNum FROM rooms WHERE roomId = ?");
            stmt.setInt(1, roomId);
            ResultSet rs = stmt.executeQuery();
            Integer roomNum = rs.getInt(1);
            conn.close();
            return roomNum;
        }
        catch (Exception ex) {
            DialogBox.Exception(ex);
            return 0;
        }
    }

    private int getRoomId(Integer roomNum) {
        try {
            Connection conn = SqlConn.Connect();
            PreparedStatement stmt = conn.prepareStatement("SELECT roomId FROM rooms WHERE roomNum = ?");
            stmt.setInt(1, roomNum);
            ResultSet rs = stmt.executeQuery();
            Integer roomId = rs.getInt(1);
            conn.close();
            return roomId;
        }
        catch (Exception ex) {
            DialogBox.Exception(ex);
            return 0;
        }
    }

    @FXML void staffViewBookingView() {
        dropDownOption = staffViewBookingSelected.getValue();
        // If no option is selected in the dropdown, do not proceed
        if (dropDownOption == null)
            DialogBox.Error("Please select a booking from the dropdown box");
        else {
            // Get the arrival date and ending date from the dropdown
            String roomNumStr = dropDownOption.substring(0,3);
            Integer roomNum = Integer.parseInt(roomNumStr);
            roomId = getRoomId(roomNum);
            arriveDate = dropDownOption.substring(5,15);
            departDate = dropDownOption.substring(dropDownOption.length() - 10);
            try {
                Connection conn = SqlConn.Connect();
                // Find the booking in question
                PreparedStatement stmt = conn.prepareStatement("SELECT * FROM bookings WHERE roomId = ? AND timeOfStart = ? AND timeOfExit = ?");
                stmt.setInt(1, roomId);
                stmt.setString(2, arriveDate);
                stmt.setString(3, departDate);
                ResultSet rs = stmt.executeQuery();
                // Display basic information about it
                staffViewBookingArrival.setText(arriveDate);
                staffViewBookingDepart.setText(departDate);
                staffViewBookingCost.setText("£" + rs.getLong(7));
                userType = rs.getString(4);
                roomId = rs.getInt(2);
                int checkedIn = rs.getInt(8);
                if (checkedIn == 0)
                    staffViewBookingChecked.setText("No");
                else if (checkedIn == 1)
                    staffViewBookingChecked.setText("Yes");
                // Get username
                userId = rs.getInt(3);
                conn.close();
            }
            catch (Exception ex) {
                DialogBox.Exception(ex);
            }
            try {
                Connection conn = SqlConn.Connect();
                PreparedStatement stmt2 = null;
                if (userType.equals("user")) {
                    stmt2 = conn.prepareStatement("SELECT * FROM customers WHERE customerId = ?");
                    stmt2.setInt(1, userId);
                }
                else if (userType.equals("staff")) {
                    stmt2 = conn.prepareStatement("SELECT * FROM staff WHERE staffId = ?");
                    stmt2.setInt(1, userId);
                }
                ResultSet rs2 = stmt2.executeQuery();
                username = rs2.getString(2);
                staffViewBookingUser.setText(username);
                conn.close();
            }
            catch (Exception ex) {
                DialogBox.Exception(ex);
            }
            try {
                Connection conn = SqlConn.Connect();
                // Get more complex info about the room
                PreparedStatement stmt = conn.prepareStatement("SELECT * FROM rooms WHERE roomId = ?");
                stmt.setInt(1, roomId);
                ResultSet rs = stmt.executeQuery();
                staffViewBookingType.setText(rs.getString(3));
                roomNumber = rs.getInt(2);
                staffViewBookingNumber.setText(String.valueOf(roomNumber));
                conn.close();
            }
            catch (Exception ex) {
                DialogBox.Exception(ex);
            }
        }
    }

    @FXML
    private void staffViewBookingDelete() {
        if (dropDownOption == null)
            DialogBox.Error("Please select a booking from the dropdown box");
        else {
            boolean confirm = DialogBox.Confirmation("Are you sure you want to delete this booking?");
            if (confirm == true) {
                try {
                    Connection conn = SqlConn.Connect();
                    // Get the arrival date and ending date from the dropdown
                    String roomNumStr = dropDownOption.substring(0,3);
                    Integer roomNum = Integer.parseInt(roomNumStr);
                    roomId = getRoomId(roomNum);
                    arriveDate = dropDownOption.substring(5,15);
                    departDate = dropDownOption.substring(dropDownOption.length() - 10);
                    // Delete the booking from the database
                    PreparedStatement stmt = conn.prepareStatement("DELETE FROM bookings WHERE roomId = ? AND timeOfStart = ? AND timeOfExit = ? AND userType = ?");
                    stmt.setInt(1, roomId);
                    stmt.setString(2, arriveDate);
                    stmt.setString(3, departDate);
                    stmt.setString(4, userType);
                    stmt.executeUpdate();
                    // Update the room to not be booked anymore
                    stmt = conn.prepareStatement("UPDATE rooms SET isBooked = 0 WHERE roomNum = ?");
                    stmt.setInt(1, roomNumber);
                    stmt.executeUpdate();
                    conn.close();
                    // Inform the user that the booking has been deleted
                    DialogBox.Info("Booking has been deleted");
                    // Reload the page to update all of the values
                    App.setRoot("staffViewBooking");
                }
                catch (Exception ex) {
                    DialogBox.Exception(ex);
                }
            }
            else if (confirm == false) {
            }
            else {
                DialogBox.Error("Something went wrong!");
            }
        }
    }

    @FXML
    private void staffViewBookingCheckin() {
        dropDownOption = staffViewBookingSelected.getValue();
        // If no option is selected in the dropdown, do not proceed
        if (dropDownOption == null)
            DialogBox.Error("Please select a booking from the dropdown box");
        else {
            // Get the arrival date and ending date from the dropdown
            String roomNumStr = dropDownOption.substring(0,3);
            Integer roomNum = Integer.parseInt(roomNumStr);
            roomId = getRoomId(roomNum);
            arriveDate = dropDownOption.substring(5,15);
            departDate = dropDownOption.substring(dropDownOption.length() - 10);
            try {
                Connection conn = SqlConn.Connect();
                // Find the booking in question
                PreparedStatement stmt = conn.prepareStatement("SELECT * FROM bookings WHERE roomId = ? AND timeOfStart = ? AND timeOfExit = ? AND userType = ?");
                stmt.setInt(1, roomId);
                stmt.setString(2, arriveDate);
                stmt.setString(3, departDate);
                stmt.setString(4, userType);
                ResultSet rs = stmt.executeQuery();
                // Check if they are currently checked in
                checkedIn = rs.getInt(8);
                conn.close();
            }
            catch (Exception ex) {
                DialogBox.Exception(ex);
            }
            if (checkedIn == 0)
                toCheckIn = 1;
            else if (checkedIn == 1)
                toCheckIn = 0;
            try {
                Connection conn = SqlConn.Connect();
                PreparedStatement stmt = conn.prepareStatement("UPDATE bookings SET checkedIn = ? WHERE roomId = ? AND timeOfStart = ? AND timeOfExit = ?");
                stmt.setInt(1, toCheckIn);
                stmt.setInt(2, roomId);
                stmt.setString(3, arriveDate);
                stmt.setString(4, departDate);
                stmt.executeUpdate();
                conn.close();
                staffViewBookingView();
            }
            catch (Exception ex) {
                DialogBox.Exception(ex);
            }
        }
    }

    @FXML
    private void staffViewBookingGoBack() {
        try {
            App.setRoot("receptionDashboard");
        } 
        catch (Exception ex) {
            DialogBox.Exception(ex);
        }
    }
}
