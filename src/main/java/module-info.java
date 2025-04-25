module com.example.hotels {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires java.desktop;


    opens com.example.hotels to javafx.fxml;
    exports com.example.hotels;
    exports com.example.hotels.controllers;
    opens com.example.hotels.controllers to javafx.fxml;
    opens com.example.hotels.models to javafx.base;

}