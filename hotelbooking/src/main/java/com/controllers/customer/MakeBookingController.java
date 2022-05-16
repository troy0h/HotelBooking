package com.controllers.customer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import com.classes.Customer;
import com.hotelbooking.App;
import com.hotelbooking.DialogBox;
import com.hotelbooking.sql.SqlConn;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

public class MakeBookingController {

    @FXML ComboBox<String>  custMakeBookingType;
    @FXML DatePicker        custMakeBookingArrival;
    @FXML DatePicker        custMakeBookingDepart;
    @FXML TextField         custMakeBookingCost;

    private String   comboBoxValue   = "";
    private int      roomCost        = 0;
    private Customer customer        = LoginController.cust;

    // Populate the combo box with room types
    @FXML
    protected void initialize() {
        custMakeBookingType.getItems().addAll(
            "Single",
            "Twin",
            "Double"
        );
    }

    // Calculate the room's cost
    @FXML
    private int custMakeBookingEstimate() {
        // Get dates from date pickers
        LocalDate arrivalDate = custMakeBookingArrival.getValue();
        LocalDate departDate  = custMakeBookingDepart.getValue();
        // Get combo box value, and work out the room cost per day
        comboBoxValue = custMakeBookingType.getValue();
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
            custMakeBookingCost.setText("£" + totalCost);
            return totalCost;
        }
        return 0;
    }

    @FXML
    private void custMakeBookingConfirm() {
        int totalCost = custMakeBookingEstimate();
        String arrivalDate = custMakeBookingArrival.getValue().toString();
        String departDate  = custMakeBookingDepart.getValue().toString();
        comboBoxValue = custMakeBookingType.getValue();
        int roomId = 0;
        
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
                stmt.setInt(2, customer.id);
                stmt.setString(3, "user");
                stmt.setString(4, arrivalDate);
                stmt.setString(5, departDate);
                stmt.setInt(6, totalCost);
                stmt.setInt(7, 0);
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
    private void custMakeBookingGoBack() {
        try {
            App.setRoot("customerDashboard");
        } 
        catch (Exception ex) {
            DialogBox.Exception(ex);
        }
    }
}
