package com.controllers;

import com.hotelbooking.App;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LoginController {

    @FXML TextField     Username;
    @FXML PasswordField Password;

    @FXML
    private void loginLogIn() throws IOException {
        System.out.println("Login Button Clicked");
        System.out.println(Username.getText());
        System.out.println(Password.getText());
    }

    @FXML
    private void loginGoBack() throws IOException {
        App.setRoot("welcome");
    }
}
