package com.hotelbooking;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class DialogBox {
    public static void Error(String error) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(error);
        alert.showAndWait();
    }

    public static void Warning(String warn) {
        Alert alert = new Alert(AlertType.WARNING);
        alert.setTitle("Warning");
        alert.setHeaderText(null);
        alert.setContentText(warn);
        alert.showAndWait();
    }

    public static void Info(String info) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText(null);
        alert.setContentText(info);
        alert.showAndWait();
    }
}
