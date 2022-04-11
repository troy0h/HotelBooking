package com.hotelbooking;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.security.MessageDigest;

import com.hotelbooking.sql.SqlStart;


//JavaFX App

public class App extends Application {

    private static Scene scene;

    @Override
    public void start(Stage stage) {

        /* 
         * Do a first connection to the database; 
         * Connect to the database,
         * Create tables if they dont exist,
         * Create database files if they dont exist,
         * Make sure database is accessible
         */

        SqlStart.firstConnect();
        // Configure the window used for the program
        // Set the window title
        stage.setTitle("Welcome!");
        // set the icon for the window
        stage.getIcons().add(new Image("HotelBooking-master\hotelbooking\src\main\resources\com\hotelbooking\icon.png"));
        // Set the scene (window file) and size
        scene = new Scene(loadFXML("welcome"), 1024, 768);
        // Make window not resizable
        stage.setResizable(false);
        // Add programming for when the window is closed via the X button
        stage.setOnCloseRequest(e -> closeProgram());
        // Set and show the window
        stage.setScene(scene);
        stage.show();
    }

    // Change the objects on the window
    public static void setRoot(String fxml) {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        try {
            return fxmlLoader.load();
        } 
        catch (Exception ex) {
            DialogBox.Exception(ex);
        }
        return null;
    }

    // Ran first on launch, runs the program with given arguments
    public static void main(String[] args) {
        launch(args);
    }

    // Happens when the program is closed, either via the X button or a quit button
    public static void closeProgram() {
        System.exit(0);
    }

    // Get the SHA256 hash of something, used for passwords
    // This is for added security in the storing of passwords
    public static String getSha256(String value) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(value.getBytes());
            return bytesToHex(md.digest());
        } 
        catch(Exception ex) {
            throw new RuntimeException(ex);
        }
     }
     private static String bytesToHex(byte[] bytes) {
        StringBuffer result = new StringBuffer();
        for (byte b : bytes) result.append(Integer.toString((b & 0xff) + 0x100, 16).substring(1));
        return result.toString();
     }
}