package com.controllers.customer;

import com.classes.Customer;
import com.hotelbooking.App;
import com.hotelbooking.DialogBox;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class DashboardController {
    private Customer customer = LoginController.cust;

    @FXML private Label customerDashboardName;

    @FXML
    public void initialize() {
        // Get user's username and add it to the label
        customerDashboardName.setText("Welcome, " + customer.username);
    }

    // Go to the Make Booking window
    @FXML
    private void custDashMakeBooking() {
        try {
            App.setRoot("customerMakeBooking");
        } 
        catch (Exception ex) {
            DialogBox.Exception(ex);
        }
    }

    // Go to the View Booking window
    @FXML
    private void custDashViewBooking() {
        try {
            App.setRoot("customerViewBooking");
        } 
        catch (Exception ex) {
            DialogBox.Exception(ex);
        }
    }

    // Go to the Delete Booking window
    @FXML
    private void custDashDeleteBooking() {
        try {
            App.setRoot("customerDeleteBooking");
        } 
        catch (Exception ex) {
            DialogBox.Exception(ex);
        }
    }

    // Go back to the welcome window
    @FXML
    private void custDashGoBack() {
        try {
            App.setRoot("welcome");
        } 
        catch (Exception ex) {
            DialogBox.Exception(ex);
        }
    }
}
