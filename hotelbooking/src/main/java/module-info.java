module com.hotelbooking {
    requires javafx.controls;
    requires javafx.fxml;
    requires transitive javafx.graphics;

    opens com.hotelbooking to javafx.fxml;
    opens com.controllers to javafx.fxml;
    exports com.hotelbooking;
    exports com.controllers;
}
