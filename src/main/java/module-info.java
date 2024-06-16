module com.brandonlagasse.scheduler2 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires java.desktop;


    opens com.brandonlagasse.scheduler2 to javafx.fxml;
    exports com.brandonlagasse.scheduler2;
    exports com.brandonlagasse.scheduler2.controller;
    opens com.brandonlagasse.scheduler2.controller to javafx.fxml;
    opens com.brandonlagasse.scheduler2.model to javafx.base;
    exports com.brandonlagasse.scheduler2.model;
    exports com.brandonlagasse.scheduler2.dao;
}