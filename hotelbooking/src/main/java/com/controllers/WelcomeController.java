package com.controllers;

import com.hotelbooking.App;

import java.io.IOException;
import javafx.fxml.FXML;

public class WelcomeController {

    @FXML
    private void welcomeSignIn() throws IOException {
        App.setRoot("login");
    }

    @FXML
    private void welcomeSignUp() throws IOException {
        App.setRoot("signup");
    }

    @FXML
    private void welcomeQuit() throws IOException {
        App.closeProgram();
    }
}