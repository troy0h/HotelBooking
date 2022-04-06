module com.hotelbooking {
    requires javafx.base;
    requires javafx.controls;
    requires javafx.fxml;
    requires transitive javafx.graphics;
    requires java.sql;

    opens com.hotelbooking to javafx.fxml;
    opens com.controllers to javafx.fxml;
    opens com.controllers.customer to javafx.fxml;
    opens com.controllers.staff to javafx.fxml;
    exports com.hotelbooking;
    exports com.controllers;
}
