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

    boolean     x = false;
    String      staffBooking = "";
    String      arriveDate = "";
    String      departDate = "";
    String      dropDownOption = "";
    Integer     roomNumber = 0;
    Integer     roomId = 0;
    String      roomIdStr = "";
    Integer     userId = 0;
    String      username = "";
    Integer     checkedIn = 0;
    Integer     toCheckIn = 0;

    // Populate the combo box with bookings made types
    @FXML
    protected void initialize() {
        Connection conn = SqlConn.Connect();
        try {
            // Get all bookings
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM bookings");
            ResultSet rs = stmt.executeQuery();
            // While there is a current line:
            while (rs.next()) {
                // Get the arrival and departure date
                roomId = rs.getInt(2);
                roomIdStr = String.format("%02d", roomId);
                arriveDate = rs.getString(4);
                departDate = rs.getString(5);
                // Make an entry in the format "NN: YYYY-MM-DD - YYYY-MM-DD"
                staffBooking = roomIdStr + ": " + arriveDate + " - "  + departDate;
                // Add it to the combo box
                staffViewBookingSelected.getItems().add(staffBooking);
            }
            conn.close();
        }
        catch (Exception ex) {
            DialogBox.Exception(ex);
        }
    }

    @FXML void staffViewBookingView() {
        dropDownOption = staffViewBookingSelected.getValue();
        // If no option is selected in the dropdown, do not proceed
        if (dropDownOption == null)
            DialogBox.Error("Please select a booking from the dropdown box");
        else {
            // Get the arrival date and ending date from the dropdown
            roomIdStr = dropDownOption.substring(0,2);
            roomId = Integer.parseInt(roomIdStr);
            arriveDate = dropDownOption.substring(4,14);
            departDate = dropDownOption.substring(dropDownOption.length() - 10);
            try {
                Connection conn = SqlConn.Connect();
                // Find the booking in question
                PreparedStatement stmt = conn.prepareStatement("SELECT * FROM bookings WHERE roomId = ? AND timeOfStart = ? AND timeOfExit = ?");
                stmt.setInt(1, roomId);
                stmt.setString(2, arriveDate);
                stmt.setString(3, departDate);
                ResultSet rs = stmt.executeQuery();
                rs.next();
                // Display basic information about it
                staffViewBookingArrival.setText(arriveDate);
                staffViewBookingDepart.setText(departDate);
                staffViewBookingCost.setText("£" + rs.getLong(6));
                roomId = rs.getInt(2);
                int checkedIn = rs.getInt(7);
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
                PreparedStatement stmt2 = conn.prepareStatement("SELECT * FROM customers WHERE customerId = ?");
                stmt2.setInt(1, userId);
                ResultSet rs2 = stmt2.executeQuery();
                if (rs2.next() == false) {
                    stmt2 = conn.prepareStatement("SELECT * FROM staff WHERE staffId = ?");
                    stmt2.setInt(1, userId);
                    rs2 = stmt2.executeQuery();
                    username = rs2.getString(2);
                }
                else {
                    username = rs2.getString(2);
                }
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
                staffViewBookingNumber.setText(String.valueOf(rs.getInt(2)));
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
                Connection conn = SqlConn.Connect();
                try {
                    // Get the arrival date and ending date from the dropdown
                    roomIdStr = dropDownOption.substring(0,2);
                    roomId = Integer.parseInt(roomIdStr);
                    arriveDate = dropDownOption.substring(4,14);
                    departDate = dropDownOption.substring(dropDownOption.length() - 10);
                    // Delete the booking from the database
                    PreparedStatement stmt = conn.prepareStatement("DELETE FROM bookings WHERE roomId AND timeOfStart = ? AND timeOfExit = ?");
                    stmt.setInt(1, roomId);
                    stmt.setString(2, arriveDate);
                    stmt.setString(3, departDate);
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
            roomIdStr = dropDownOption.substring(0,2);
            roomId = Integer.parseInt(roomIdStr);
            arriveDate = dropDownOption.substring(4,14);
            departDate = dropDownOption.substring(dropDownOption.length() - 10);
            try {
                Connection conn = SqlConn.Connect();
                // Find the booking in question
                PreparedStatement stmt = conn.prepareStatement("SELECT * FROM bookings WHERE roomId = ? AND timeOfStart = ? AND timeOfExit = ?");
                stmt.setInt(1, roomId);
                stmt.setString(2, arriveDate);
                stmt.setString(3, departDate);
                ResultSet rs = stmt.executeQuery();
                // Check if they are currently checked in
                checkedIn = rs.getInt(7);
                conn.close();
            }
            catch (Exception ex) {
                DialogBox.Exception(ex);
            }
            try {
                if (checkedIn == 0)
                    toCheckIn = 1;
                else if (checkedIn == 1)
                    toCheckIn = 0;
                Connection conn = SqlConn.Connect();
                PreparedStatement stmt = conn.prepareStatement("UPDATE bookings SET checkedIn = ? WHERE roomId = ? AND timeOfStart = ? AND timeOfExit = ?");
                stmt.setInt(1, toCheckIn);
                stmt.setInt(2, roomId);
                stmt.setString(3, arriveDate);
                stmt.setString(4, departDate);
                stmt.executeUpdate();
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
