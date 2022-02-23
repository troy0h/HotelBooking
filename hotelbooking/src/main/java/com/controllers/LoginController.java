package com.controllers;

import com.classes.User;
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
    @FXML TextField     Username;
    @FXML PasswordField Password;
    String dbPassword = "";
    int adminInt = 0;

    @FXML
    private void loginLogIn() {
        Connection conn = SqlConn.Connect();
        // Create a new password hash from the given password
        String PassHash = App.getSha256(Password.getText());
        User user = new User();
        user.username = Username.getText();
        user.password = PassHash;

        try {
            // Get the line where the username matches the username column
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM users WHERE username = ?");
            stmt.setString(1, user.username);
            ResultSet rs = stmt.executeQuery();
            while(rs.next()) {
                // Get the password hash from the database
                dbPassword = rs.getString(3);
                user.name = rs.getString(4);
                adminInt = rs.getInt(5);
            }

            if (!dbPassword.equals(user.password)){
                // If the database password does not equal the hash, the password is incorrect
                DialogBox.Error("Username or Password does not match");
            }
            else {
                if (adminInt == 1)
                    user.isAdmin = true;
                else
                    user.isAdmin = false;

                DialogBox.Info("Successfully signed in\nWelcome, " + user.name + "\nUser is admin?: " + user.isAdmin);
            }
        }
        catch (Exception ex) {
            DialogBox.Exception(ex);
        }
    }

    // Go back to the welcome window
    @FXML
    private void loginGoBack() {
        try {
            App.setRoot("welcome");
        } 
        catch (Exception ex) {
            DialogBox.Exception(ex);
        }
    }
}
