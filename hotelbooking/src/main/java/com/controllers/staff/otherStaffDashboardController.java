package com.controllers.staff;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.classes.Staff;
import com.hotelbooking.App;
import com.hotelbooking.DialogBox;
import com.hotelbooking.sql.SqlConn;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class otherStaffDashboardController {

    @FXML ComboBox<String>  otherStaffViewBookingSelected;
    @FXML TextField         otherStaffViewBookingCost;
    @FXML Label             otherStaffDashboardName;

    private Staff staff = LoginController.staff;

    String      staffBooking = "";
    String      arriveDate = "";
    String      departDate = "";
    String      dropDownOption = "";
    Integer     roomNumber = 0;
    Integer     roomId = 0;
    Long        bookingPrice = (long) 0;

    // Populate the combo box with bookings made types
    @FXML
    protected void initialize() {
        otherStaffDashboardName.setText("Welcome, " + staff.username);
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
                otherStaffViewBookingSelected.getItems().add(staffBooking);
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

    @FXML
    private void otherStaffViewBookingAdd() {
        dropDownOption = otherStaffViewBookingSelected.getValue();
        // If no option is selected in the dropdown, do not proceed
        if (dropDownOption == null)
            DialogBox.Error("Please select a booking from the dropdown box");
        // If nothing is in the cost to add box, do not proceed
        else if (otherStaffViewBookingCost.equals(null))
            DialogBox.Error("Please enter a cost to add");
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
                bookingPrice = rs.getLong(7);
                conn.close();
            }
            catch (Exception ex) {
                DialogBox.Exception(ex);
            }
            bookingPrice += Long.parseLong(otherStaffViewBookingCost.getText());
            try {
                Connection conn = SqlConn.Connect();
                // Find the booking in question
                PreparedStatement stmt = conn.prepareStatement("UPDATE bookings SET bookingPrice = ? WHERE roomId = ? AND timeOfStart = ? AND timeOfExit = ?");
                stmt.setLong(1, bookingPrice);
                stmt.setInt(2, roomId);
                stmt.setString(3, arriveDate);
                stmt.setString(4, departDate);
                stmt.executeUpdate();
            }
            catch (Exception ex) {
                DialogBox.Exception(ex);
            }
            DialogBox.Info("Booking updated \nNew total is: £" + bookingPrice);
        }
    }

    // Go back to the welcome window
    @FXML
    private void otherStaffLogOut() {
        try {
            App.setRoot("welcome");
        }
        catch (Exception ex) {
            DialogBox.Exception(ex);
        }
    }
}
