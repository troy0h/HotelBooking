package com.controllers.customer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.classes.Customer;
import com.hotelbooking.App;
import com.hotelbooking.DialogBox;
import com.hotelbooking.sql.SqlConn;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

public class ViewBookingController {

    @FXML ComboBox<String>  custViewBookingSelected;
    @FXML TextField         custViewBookingArrival;
    @FXML TextField         custViewBookingDepart;
    @FXML TextField         custViewBookingType;
    @FXML TextField         custViewBookingNumber;
    @FXML TextField         custViewBookingCost;
    @FXML TextField         custViewBookingChecked;

    private Customer customer = LoginController.cust;

    boolean     x = false;
    String      custBooking = "";
    String      arriveDate = "";
    String      departDate = "";
    String      dropDownOption = "";
    Integer     roomNum = 0;

    // Populate the combo box with bookings made types
    @FXML
    protected void initialize() {
        Connection conn = SqlConn.Connect();
        try {
            // Get all bookings made by the user
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM bookings WHERE userId = ? AND userType = ?");
            stmt.setInt(1, customer.id);
            stmt.setString(2, "user");
            ResultSet rs = stmt.executeQuery();
            // While there is a current line:
            while (rs.next()) {
                // Get the arrival and departure date
                arriveDate = rs.getString(5);
                departDate = rs.getString(6);
                // Make an entry in the format "YYYY-MM-DD - YYYY-MM-DD"
                custBooking = arriveDate + " - "  + departDate;
                // Add it to the combo box
                custViewBookingSelected.getItems().add(custBooking);
            }
            conn.close();
        }
        catch (Exception ex) {
            DialogBox.Exception(ex);
        }
    }

    @FXML void custViewBookingView() {
        dropDownOption = custViewBookingSelected.getValue();
        Connection conn = SqlConn.Connect();
        // If no option is selected in the dropdown, do not proceed
        if (dropDownOption == null)
            DialogBox.Error("Please select a booking from the dropdown box");
        else {
            // Get the arrival date and ending date from the dropdown
            arriveDate = dropDownOption.substring(0,10);
            departDate = dropDownOption.substring(dropDownOption.length() - 10);
            try {
                // Find the booking in question
                PreparedStatement stmt = conn.prepareStatement("SELECT * FROM bookings WHERE userId = ? AND timeOfStart = ? AND timeOfExit = ? AND userType = ?");
                stmt.setInt(1, customer.id);
                stmt.setString(2, arriveDate);
                stmt.setString(3, departDate);
                stmt.setString(4, "user");
                ResultSet rs = stmt.executeQuery();
                // Display basic information about it
                custViewBookingArrival.setText(arriveDate);
                custViewBookingDepart.setText(departDate);
                custViewBookingCost.setText("£" + rs.getLong(7));
                int checkedIn = rs.getInt(8);
                if (checkedIn == 0)
                    custViewBookingChecked.setText("No");
                else if (checkedIn == 1)
                    custViewBookingChecked.setText("Yes");
                // Get more complex info about the room
                stmt = conn.prepareStatement("SELECT * FROM rooms WHERE roomId = ?");
                stmt.setInt(1, rs.getInt(2));
                rs = stmt.executeQuery();
                custViewBookingType.setText(rs.getString(3));
                roomNum = rs.getInt(2);
                custViewBookingNumber.setText(String.valueOf(roomNum));
                conn.close();
            }
            catch (Exception ex) {
                DialogBox.Exception(ex);
            }
        }
    }

    @FXML
    private void custViewBookingDelete() {
        if (dropDownOption == null)
            DialogBox.Error("Please select a booking from the dropdown box");
        else {
            boolean confirm = DialogBox.Confirmation("Are you sure you want to delete this booking?");
            if (confirm == true) {
                Connection conn = SqlConn.Connect();
                try {
                    // Get the arrival date and ending date from the dropdown
                    arriveDate = dropDownOption.substring(0,10);
                    departDate = dropDownOption.substring(dropDownOption.length() - 10);
                    // Delete the booking from the database
                    PreparedStatement stmt = conn.prepareStatement("DELETE FROM bookings WHERE userId = ? AND timeOfStart = ? AND timeOfExit = ? AND userType = ?");
                    stmt.setInt(1, customer.id);
                    stmt.setString(2, arriveDate);
                    stmt.setString(3, departDate);
                    stmt.setString(4, "user");
                    stmt.executeUpdate();
                    // Update the room to not be booked anymore
                    stmt = conn.prepareStatement("UPDATE rooms SET isBooked = 0 WHERE roomNum = ?");
                    stmt.setInt(1, roomNum);
                    stmt.executeUpdate();
                    conn.close();
                    // Inform the user that the booking has been deleted
                    DialogBox.Info("Booking has been deleted");
                    // Reload the page to update all of the values
                    App.setRoot("customerViewBooking");
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
    private void custViewBookingGoBack() {
        try {
            App.setRoot("customerDashboard");
        } 
        catch (Exception ex) {
            DialogBox.Exception(ex);
        }
    }
}
