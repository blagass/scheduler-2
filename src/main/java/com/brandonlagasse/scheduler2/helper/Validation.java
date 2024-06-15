package com.brandonlagasse.scheduler2.helper;

import javafx.scene.control.Alert;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Validation is a simple helper class to return an alert based on having empty fields.
 * While this implimentation works, it was part of an expirement to see which way was more efficient.
 * In the future, I would make this class contain errors for all types in a public static class
 */

public class Validation {
    /**
     * This is the main method that is called for errors.
     */
    public static void showEmptyFieldsError() {
        ResourceBundle rb = ResourceBundle.getBundle("/com/brandonlagasse/scheduler2/Lang", Locale.getDefault());
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(rb.getString("loginError"));
        alert.setHeaderText(null);
        alert.setContentText(rb.getString("emptyFields"));
        alert.showAndWait();
    }

    public static class wrongCredentials extends Exception {
        public wrongCredentials(String message) {
            super(message);
        }
    }

}
