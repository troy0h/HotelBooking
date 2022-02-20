package com.controllers;

import com.hotelbooking.App;

import java.io.IOException;
import javafx.fxml.FXML;

public class WelcomeController {

    // Go to login if the Sign In button is clicked
    @FXML
    private void welcomeSignIn() throws IOException {
        App.setRoot("login");
    }

    // Go to sign up if the Sign Up button is clicked
    @FXML
    private void welcomeSignUp() throws IOException {
        App.setRoot("signup");
    }

    // Quits the program
    @FXML
    private void welcomeQuit() throws IOException {
        App.closeProgram();
    }
}