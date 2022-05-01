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
        customerDashboardName.setText("Welcome, " + customer.username);
    }

    public void getObject(Customer cust) {
        customer = cust;
    }

    // Go back to the welcome window
    @FXML
    private void custLoginGoBack() {
        try {
            App.setRoot("welcome");
        } 
        catch (Exception ex) {
            DialogBox.Exception(ex);
        }
    }
}
