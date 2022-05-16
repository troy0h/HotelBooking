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

    @FXML
    public void receptionDashboardMakeBooking() {
        try {
            App.setRoot("staffMakeBooking");
        } 
        catch (Exception ex) {
            DialogBox.Exception(ex);
        }
    }

    @FXML
    public void receptionDashboardViewBooking() {
        try {
            App.setRoot("staffViewBooking");
        } 
        catch (Exception ex) {
            DialogBox.Exception(ex);
        }
    }

    @FXML
    public void receptionDashboardExternalService() {
        try {
            App.setRoot("staffExternalService");
        } 
        catch (Exception ex) {
            DialogBox.Exception(ex);
        }
    }

    // Go back to the welcome window
    @FXML
    private void receptionDashboardGoBack() {
        try {
            App.setRoot("welcome");
        } 
        catch (Exception ex) {
            DialogBox.Exception(ex);
        }
    }
}
