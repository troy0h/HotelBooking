package com.controllers.staff;

import com.classes.Staff;
import com.hotelbooking.App;
import com.hotelbooking.DialogBox;
import com.hotelbooking.sql.SqlConn;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LoginController {

    // Get the username and password from their fields
    @FXML TextField     staffLoginUsername;
    @FXML PasswordField staffLoginPassword;
    String dbPassword = "";

    public static Staff staff = new Staff();

    @FXML
    private void staffLoginLogin() {
        Connection conn = SqlConn.Connect();
        // Create a new password hash from the given password
        String PassHash = App.getSha256(staffLoginPassword.getText());
        staff.username = staffLoginUsername.getText();
        staff.password = PassHash;

        try {
            // Get the line where the username matches the username column
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM staff WHERE username = ?");
            stmt.setString(1, staff.username);
            ResultSet rs = stmt.executeQuery();
            while(rs.next()) {
                // Get the password hash from the database
                staff.id = rs.getInt(1);
                dbPassword = rs.getString(3);
                staff.staffType = rs.getString(4);
            }
            conn.close();

            if (!dbPassword.equals(staff.password)){
                // If the database password does not equal the hash, the password is incorrect
                DialogBox.Error("Username or Password does not match");
            }
            else {
                if (staff.staffType.equals("Receptionist"))
                    App.setRoot("receptionDashboard");
                else if (staff.staffType.equals("Bar Staff"))
                    App.setRoot("otherStaffDashboard");
                else
                    App.setRoot("otherStaffDashboard");
            }
        }
        catch (Exception ex) {
            DialogBox.Exception(ex);
        }
    }

    // Go back to the welcome window
    @FXML
    private void staffLoginGoBack() {
        try {
            App.setRoot("welcome");
        } 
        catch (Exception ex) {
            DialogBox.Exception(ex);
        }
    }
}
