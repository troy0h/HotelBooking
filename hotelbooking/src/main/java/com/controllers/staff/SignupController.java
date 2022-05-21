package com.controllers.staff;

import com.hotelbooking.App;
import com.hotelbooking.DialogBox;
import com.hotelbooking.sql.SqlConn;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class SignupController {

    // Get user information from the various elements
    @FXML private TextField         staffSignupUsername;
    @FXML private PasswordField     staffSignupPassword;
    @FXML private PasswordField     staffSignupConfirm;
    @FXML private ComboBox<String>  staffSignupType;

    boolean userExists = false;

    @FXML
    protected void initialize() {
        staffSignupType.getItems().addAll(
            "Receptionist",
            "Restaraunt Staff",
            "Bar Staff"
        );
    }

    @FXML
    private void staffSignupSignup() {
        // Connect to the database
        ResultSet rs;
        try {
            Connection conn = SqlConn.Connect();
            // Check the username in the database
            PreparedStatement st = conn.prepareStatement("SELECT * FROM staff WHERE username=?");
            st.setString(1,staffSignupUsername.getText());
            rs = st.executeQuery();
            // Do some checks on the username
            boolean valid = (staffSignupUsername.getText() != null) && staffSignupUsername.getText().matches("[A-Za-z0-9_]+");
            // Check against the checks performed above
            if (!valid) 
                DialogBox.Error("Username is invalid\nUsername must be alphanumeric");
            // Check if password and confirmation are empty
            else if (staffSignupPassword.getText().equals("") || staffSignupConfirm.getText().equals("")) 
                DialogBox.Error("Password or password confirmation are empty");
            // Check if password and confirmation match
            else if (!staffSignupPassword.getText().equals(staffSignupConfirm.getText()))
                DialogBox.Error("Password and password confirmation do not match");
            // Check against the database entry checked above
            else if (!rs.next() == false) 
                DialogBox.Error("Desired username is already in use");
            else if (staffSignupType.getValue() == null)
                DialogBox.Error("Please select a value in the dropdown box");
            // Every check passes
            else {
                // Get the passwords' hash
                String PassHash = App.getSha256(staffSignupPassword.getText());
                try {
                    conn = SqlConn.Connect();
                    // Prepare and execute an insert statement
                    PreparedStatement stmt = conn.prepareStatement("INSERT INTO staff (username, password, staffType) VALUES (?, ?, ?)");
                    stmt.setString(1, staffSignupUsername.getText());
                    stmt.setString(2, PassHash);
                    stmt.setString(3, staffSignupType.getValue());
                    stmt.executeUpdate();
                    conn.close();
                }
                catch (Exception ex) {
                    // If the database entry fails, close the connection and display an exception
                    DialogBox.Exception(ex);
                }
                finally {
                    // If the database entry succeeds, tell the user, close the conenction, and return to the welcome screen
                    DialogBox.Info("Account created successfully");
                    App.setRoot("welcome");
                    
                }
            }
        }
        catch (Exception ex) {
            // If the database entry fails, close the connection and display an exception
            DialogBox.Exception(ex);
        }
    }

    // Go back to the welcome window
    @FXML
    private void staffSignupGoBack() {
        try {
            App.setRoot("welcome");
        } 
        catch (Exception ex) {
            DialogBox.Exception(ex);
        }
    }
}
