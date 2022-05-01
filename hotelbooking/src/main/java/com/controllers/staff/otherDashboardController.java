package com.controllers.staff;

import com.classes.Staff;
import com.hotelbooking.App;
import com.hotelbooking.DialogBox;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class otherDashboardController {
    private Staff staff = LoginController.staff;

    @FXML private Label otherStaffDashboardName;

    @FXML
    public void initialize() {
        otherStaffDashboardName.setText("Welcome, " + staff.username);
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
