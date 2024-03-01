module com.brandonlagasse.scheduler2 {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.brandonlagasse.scheduler2 to javafx.fxml;
    exports com.brandonlagasse.scheduler2;
}