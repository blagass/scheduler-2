package com.brandonlagasse.scheduler2.helper;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class Validation {

    public static void showEmptyFieldsError() throws Error {
        throw new Error("Fields Cannot be Empty");
    }
}