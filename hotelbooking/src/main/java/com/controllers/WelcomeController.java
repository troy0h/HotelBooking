package com.controllers;

import com.hotelbooking.App;
import com.hotelbooking.DialogBox;

import javafx.fxml.FXML;

public class WelcomeController {

    // Go to login if the Sign In button is clicked
    @FXML
    private void welcomeSignIn() {
        try {
            App.setRoot("login");
        } 
        catch (Exception ex) {
            DialogBox.Exception(ex);
        }
    }

    // Go to sign up if the Sign Up button is clicked
    @FXML
    private void welcomeSignUp() {
        try {
            App.setRoot("signup");
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
