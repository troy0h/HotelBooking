package com.controllers;

import com.hotelbooking.App;
import com.hotelbooking.DialogBox;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class SignupController {

    @FXML TextField     Username;
    @FXML PasswordField Password;
    @FXML PasswordField PasswordConfirm;

    @FXML
    private void signupSignUp() throws IOException {
        if (!Password.getText().equals(PasswordConfirm.getText()))
            DialogBox.Error("Passwords do not match");
    }

    @FXML
    private void signupGoBack() throws IOException {
        App.setRoot("welcome");
    }
}
