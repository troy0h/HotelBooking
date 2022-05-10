package com.controllers.customer;

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
import javafx.scene.control.ToggleButton;

public class SignupController {

    // Get user information from the various elements
    @FXML private TextField         custSignupUsername;
    @FXML private TextField         custSignupEmail;
    @FXML private PasswordField     custSignupPassword;
    @FXML private PasswordField     custSignupConfirm;
    @FXML private ToggleButton      custSignupCorpClient;
    @FXML private ComboBox<String>  custSignupPayment;

    boolean userExists = false;

    @FXML
    protected void initialize() {
        custSignupPayment.getItems().addAll(
            "Cash",
            "Credit Card",
            "Debit Card"
        );
    }

    @FXML
    private void custSignupSignup() {
        // Connect to the database
        Connection conn = SqlConn.Connect();
        ResultSet rs;
        ResultSet rs2;
        try {
            // Check the username in the database
            PreparedStatement st = conn.prepareStatement("SELECT * FROM customers WHERE username=?");
            st.setString(1,custSignupUsername.getText());
            rs = st.executeQuery();

            // Check the email in the database
            PreparedStatement st2 = conn.prepareStatement("SELECT * FROM customers WHERE email=?");
            st2.setString(1,custSignupEmail.getText());
            rs2 = st.executeQuery();

            // Do some checks on the username
            boolean valid = (custSignupUsername.getText() != null) && custSignupUsername.getText().matches("[A-Za-z0-9_]+");
            // Check against the checks performed above
            if (!valid) 
                DialogBox.Error("Username is invalid\nUsername must be alphanumeric");
            // Check if password and confirmation are empty
            else if (custSignupPassword.getText().equals("") || custSignupConfirm.getText().equals("")) 
                DialogBox.Error("Password or password confirmation are empty");
            // Check if password and confirmation match
            else if (!custSignupPassword.getText().equals(custSignupConfirm.getText()))
                DialogBox.Error("Password and password confirmation do not match");
            // Check against the database entry checked above
            else if (!rs.next() == false) 
                DialogBox.Error("Desired username is already in use");
            else if (!rs2.next() == false) 
                DialogBox.Error("Desired email address is already in use");
            else if (custSignupPayment.getValue() == null)
                DialogBox.Error("Please select a value in the dropdown box");
            // Every check passes
            else {
                // Get the passwords' hash
                String PassHash = App.getSha256(custSignupPassword.getText());
                try {
                    String corpClient = "";
                    if (custSignupCorpClient.isSelected())
                        corpClient = "True";
                    else if (!custSignupCorpClient.isSelected())
                        corpClient = "False";

                    // Prepare and execute an insert statement
                    PreparedStatement stmt = conn.prepareStatement("INSERT INTO customers (username, password, email, paymentMethod, corporateClient) VALUES (?, ?, ?, ?, ?)");
                    stmt.setString(1, custSignupUsername.getText());
                    stmt.setString(2, PassHash);
                    stmt.setString(3, custSignupEmail.getText());
                    stmt.setString(4, custSignupPayment.getValue());
                    stmt.setString(5, corpClient);
                    stmt.executeUpdate();
                    conn.close();
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
    private void custSignupGoBack() {
        try {
            App.setRoot("welcome");
        } 
        catch (Exception ex) {
            DialogBox.Exception(ex);
        }
    }
}
