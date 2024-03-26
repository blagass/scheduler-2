package com.brandonlagasse.scheduler2.helper;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

import java.util.Locale;
import java.util.ResourceBundle;

public class Validation {

    public static void showEmptyFieldsError() {
        ResourceBundle rb = ResourceBundle.getBundle("/com/brandonlagasse/scheduler2/Lang", Locale.getDefault());
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(rb.getString("loginError"));
        alert.setHeaderText(null);
        alert.setContentText(rb.getString("emptyFields"));
        alert.showAndWait();
    }
}