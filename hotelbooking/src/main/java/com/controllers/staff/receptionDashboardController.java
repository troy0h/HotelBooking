package com.controllers.staff;

import com.classes.Staff;
import com.hotelbooking.App;
import com.hotelbooking.DialogBox;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class receptionDashboardController {
    private Staff staff = LoginController.staff;

    @FXML private Label receptionStaffDashboardName;

    @FXML
    public void initialize() {
        receptionStaffDashboardName.setText("Welcome, " + staff.username);
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
