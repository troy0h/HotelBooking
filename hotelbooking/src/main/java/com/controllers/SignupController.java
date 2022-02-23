package com.controllers;

import com.hotelbooking.App;
import com.hotelbooking.DialogBox;
import com.hotelbooking.sql.SqlConn;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class SignupController {

    // Get the username, password and password confirmation from their fields
    @FXML TextField     Username;
    @FXML PasswordField Password;
    @FXML PasswordField PasswordConfirm;

    boolean userExists = false;

    @FXML
    private void signupSignUp() throws IOException {
        // Connect to the database
        Connection conn = SqlConn.Connect();
        ResultSet rs;
        try {
            // Check the username in the database
            PreparedStatement st = conn.prepareStatement("SELECT * FROM users WHERE username=?");
            st.setString(1,Username.getText());
            rs = st.executeQuery();

            // Do some checks on the username
            boolean valid = (Username.getText() != null) && Username.getText().matches("[A-Za-z0-9_]+");
            // Check against the checks performed above
            if (!valid) 
                DialogBox.Error("Username is invalid\nUsername must be alphanumeric");
            // Check if password and confirmation are empty
            else if (Password.getText().equals("") || PasswordConfirm.getText().equals("")) 
                DialogBox.Error("Password or password confirmation are empty");
            // Check if password and confirmation match
            else if (!Password.getText().equals(PasswordConfirm.getText()))
                DialogBox.Error("Password and password confirmation do not match");
            // Check against the database entry checked above
            else if (!rs.next() == false) 
                DialogBox.Error("Desired username is already in use");
            // Every check passes
            else {
                // Get the passwords' hash
                String PassHash = App.getSha256(Password.getText());
                try {
                    // Prepare and execute an insert statement
                    PreparedStatement stmt = conn.prepareStatement("INSERT INTO users (username, password) VALUES (?, ?)");
                    stmt.setString(1, Username.getText());
                    stmt.setString(2, PassHash);
                    stmt.executeUpdate();
                }
                catch (Exception ex) {
                    // If the database entry fails, close the connection and display an exception
                    conn.close();
                    DialogBox.Exception(ex);
                }
                finally {
                    // If the database entry succeeds, tell the user, close the conenction, and return to the welcome screen
                    DialogBox.Info("Account created successfully");
                    conn.close();
                    App.setRoot("welcome");
                }
            }
        }
        catch (Exception ex) {
            DialogBox.Exception(ex);
        }
    }

    // Go back to the welcome window
    @FXML
    private void signupGoBack() throws IOException {
        App.setRoot("welcome");
    }
}
