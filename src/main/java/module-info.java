module com.brandonlagasse.scheduler2 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens com.brandonlagasse.scheduler2 to javafx.fxml;
    exports com.brandonlagasse.scheduler2;
    exports com.brandonlagasse.scheduler2.controller;
    opens com.brandonlagasse.scheduler2.controller to javafx.fxml;
}