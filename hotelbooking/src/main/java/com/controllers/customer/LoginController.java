package com.controllers.customer;

import com.classes.Customer;
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
    @FXML TextField     custLoginUsername;
    @FXML PasswordField custLoginPassword;
    String dbPassword = "";
    String corpClient = "";

    // Needs to be public to be able to transfer data to dashboard
    public static Customer cust = new Customer();

    @FXML
    private void custLoginLogin() {
        Connection conn = SqlConn.Connect();
        // Create a new password hash from the given password
        String PassHash = App.getSha256(custLoginPassword.getText());
        cust.username = custLoginUsername.getText();
        cust.password = PassHash;

        try {
            // Get the line where the username matches the username column
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM customers WHERE username = ?");
            stmt.setString(1, cust.username);
            ResultSet rs = stmt.executeQuery();
            while(rs.next()) {
                // Get the customer information from the database
                cust.id = rs.getInt(1);
                dbPassword = rs.getString(3);
                cust.email = rs.getString(4);
                cust.paymentMethod = rs.getString(5);
                corpClient = rs.getString(6);
            }
            conn.close();

            if (!dbPassword.equals(cust.password)){
                // If the database password does not equal the hash, the password is incorrect
                DialogBox.Error("Username or Password does not match");
            }
            else {
                if (corpClient.equals("true"))
                    cust.isCorpClient = true;
                else
                    cust.isCorpClient = false;
                    
                App.setRoot("customerDashboard");
            }
        }
        catch (Exception ex) {
            DialogBox.Exception(ex);
        }
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
