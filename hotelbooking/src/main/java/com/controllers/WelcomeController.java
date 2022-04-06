package com.controllers;

import com.hotelbooking.App;
import com.hotelbooking.DialogBox;

import javafx.fxml.FXML;

public class WelcomeController {

    // Go to login if the Sign In button is clicked
    @FXML
    private void welcomeCustSignIn() {
        try {
            App.setRoot("customerLogin");
        } 
        catch (Exception ex) {
            DialogBox.Exception(ex);
        }
    }

    // Go to sign up if the Sign Up button is clicked
    @FXML
    private void welcomeCustSignUp() {
        try {
            App.setRoot("customerSignup");
        } 
        catch (Exception ex) {
            DialogBox.Exception(ex);
        }
    }

    // Go to sign up if the Sign Up button is clicked
    @FXML
    private void welcomeStaffSignIn() {
        try {
            App.setRoot("staffLogin");
        } 
        catch (Exception ex) {
            DialogBox.Exception(ex);
        }
    }

    // Go to sign up if the Sign Up button is clicked
    @FXML
    private void welcomeStaffSignUp() {
        try {
            App.setRoot("staffSignup");
        } 
        catch (Exception ex) {
            DialogBox.Exception(ex);
        }
    }

    // Quits the program
    @FXML
    private void welcomeQuit() {
        App.closeProgram();
    }
}
