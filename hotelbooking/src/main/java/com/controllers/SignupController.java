package com.controllers;

import com.hotelbooking.App;
import com.hotelbooking.DialogBox;
import com.hotelbooking.sql.SqlConn;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;

public class SignupController {

    // Get the username, password and password confirmation from their fields
    @FXML private TextField     Username;
    @FXML private TextField     Name;
    @FXML private PasswordField Password;
    @FXML private PasswordField PasswordConfirm;
    @FXML private ToggleButton  IsAdmin;

    boolean userExists = false;

    @FXML
    private void signupSignUp() {
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
            boolean valid2 = (Name.getText() != null) && Name.getText().matches("[A-Za-z0-9_]+");
            // Check against the checks performed above
            if (!valid) 
                DialogBox.Error("Username is invalid\nUsername must be alphanumeric");
            else if (!valid2)
                DialogBox.Error("Name is invalid\nName must be alphanumeric");
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
                    int userAdmin = 0;
                    if (IsAdmin.isSelected())
                        userAdmin = 1;
                    else if (!IsAdmin.isSelected())
                        userAdmin = 0;

                    // Prepare and execute an insert statement
                    PreparedStatement stmt = conn.prepareStatement("INSERT INTO users (username, password, name, isAdmin) VALUES (?, ?, ?, ?)");
                    stmt.setString(1, Username.getText());
                    stmt.setString(2, PassHash);
                    stmt.setString(3, Name.getText());
                    stmt.setInt(4, userAdmin);
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
    private void signupGoBack() {
        try {
            App.setRoot("welcome");
        } 
        catch (Exception ex) {
            DialogBox.Exception(ex);
        }
    }
}
