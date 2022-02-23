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

public class LoginController {

    // Get the username and password from their fields
    @FXML TextField     Username;
    @FXML PasswordField Password;
    String dbPassword = "";

    @FXML
    private void loginLogIn() {
        Connection conn = SqlConn.Connect();
        // Create a new password hash from the given password
        String PassHash = App.getSha256(Password.getText());

        try {
            // Get the line where the username matches the username column
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM users WHERE username = ?");
            stmt.setString(1, Username.getText());
            ResultSet rs = stmt.executeQuery();
            while(rs.next()) {
                // Get the password hash from the database
                dbPassword = rs.getString(3);
            }
            if (!dbPassword.equals(PassHash)){
                // If the database password does not equal the hash, the password is incorrect
                DialogBox.Error("Username or Password does not match");
            }
            else {
                DialogBox.Info("Successfully signed in");
            }
        }
        catch (Exception ex) {
            DialogBox.Exception(ex);
        }
    }

    // Go back to the welcome window
    @FXML
    private void loginGoBack() throws IOException {
        App.setRoot("welcome");
    }
}
