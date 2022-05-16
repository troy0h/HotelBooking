package com.controllers.staff;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import com.classes.Staff;
import com.hotelbooking.App;
import com.hotelbooking.DialogBox;
import com.hotelbooking.sql.SqlConn;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;

public class MakeBookingController {

    @FXML ComboBox<String>  staffMakeBookingType;
    @FXML DatePicker        staffMakeBookingArrival;
    @FXML DatePicker        staffMakeBookingDepart;
    @FXML TextField         staffMakeBookingCost;
    @FXML ToggleButton      staffMakeBookingCheck;

    private String      comboBoxValue   = "";
    private int         roomCost        = 0;
    private Staff       staff           = LoginController.staff;
    private int         toCheckIn       = 0;

    // Populate the combo box with room types
    @FXML
    protected void initialize() {
        staffMakeBookingType.getItems().addAll(
            "Single",
            "Twin",
            "Double"
        );
    }

    // Calculate the room's cost
    @FXML
    private int staffMakeBookingEstimate() {
        try {
            // Get dates from date pickers
            LocalDate arrivalDate = staffMakeBookingArrival.getValue();
            LocalDate departDate  = staffMakeBookingDepart.getValue();
            // Get combo box value, and work out the room cost per day
            comboBoxValue = staffMakeBookingType.getValue();
            if (comboBoxValue.equals("Single"))
                roomCost = 40;
            else if (comboBoxValue.equals("Twin"))
                roomCost = 50;
            else if (comboBoxValue.equals("Double"))
                roomCost = 60;
            else if (comboBoxValue.equals(""))
                DialogBox.Error("Please select a room type from the dropdown box");
            // Multiply the room cost per day by the number of days
            long daysBetween = ChronoUnit.DAYS.between(arrivalDate, departDate);
            // If days is 0 (same day arrival and departure), make days 1
            if (daysBetween == 0)
                daysBetween = 1;
            else if (daysBetween < 0)
                DialogBox.Error("The departure date cannot be before the arrival date");
            // Calculate the total cost
            else if (daysBetween > 0) {
                int totalCost = (int) (daysBetween * roomCost);
                staffMakeBookingCost.setText("£" + totalCost);
                return totalCost;
            }
            return 0;
        }
        catch (Exception ex) {
            DialogBox.Exception(ex);
            return 0;
        }
    }

    @FXML
    private void staffMakeBookingConfirm() {
        int totalCost = staffMakeBookingEstimate();
        String arrivalDate = staffMakeBookingArrival.getValue().toString();
        String departDate  = staffMakeBookingDepart.getValue().toString();
        comboBoxValue = staffMakeBookingType.getValue();
        int roomId = 0;
        if (staffMakeBookingCheck.isSelected() == false) {
            toCheckIn = 0;
        }
        else if (staffMakeBookingCheck.isSelected() == true) {
            toCheckIn = 1;
        }
        if (totalCost == 0)
            DialogBox.Error("Please enter all appropriate values before making a booking");
        else if (totalCost != 0){
            Connection conn = SqlConn.Connect();
            try {
                // Get lowest numbered room that is free and is of given room type
                PreparedStatement stmt = conn.prepareStatement("SELECT * FROM rooms WHERE roomType = ? AND isBooked = 0");
                stmt.setString(1, comboBoxValue);
                ResultSet rs = stmt.executeQuery();
                roomId = rs.getInt(1);
            }
            catch (Exception ex) {
                DialogBox.Exception(ex);
            }
            if (roomId == 0)
                DialogBox.Error("Something has gone wrong! \nTry a different room type");
            try {
                // Update the room to show that it is booked
                PreparedStatement stmt = conn.prepareStatement("UPDATE rooms SET isBooked = 1 WHERE roomId = ?");
                stmt.setInt(1, roomId);
                stmt.executeUpdate();
            }
            catch (Exception ex) {
                DialogBox.Exception(ex);
            }
            try {
                // Add the booking
                PreparedStatement stmt = conn.prepareStatement("INSERT INTO bookings (roomId, userId, userType, timeOfStart, timeOfExit, bookingPrice, checkedIn) VALUES (?, ?, ?, ?, ?, ?, ?)");
                stmt.setInt(1, roomId);
                stmt.setInt(2, staff.id);
                stmt.setString(3, "staff");
                stmt.setString(4, arrivalDate);
                stmt.setString(5, departDate);
                stmt.setInt(6, totalCost);
                stmt.setInt(7, toCheckIn);
                stmt.executeUpdate();
                conn.close();
            }
            catch (Exception ex) {
                DialogBox.Exception(ex);
            }
                DialogBox.Info("Booking was created successfully");
        }
    }

    @FXML
    private void staffMakeBookingGoBack() {
        try {
            App.setRoot("receptionDashboard");
        } 
        catch (Exception ex) {
            DialogBox.Exception(ex);
        }
    }
}
