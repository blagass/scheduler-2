package com.brandonlagasse.scheduler2.helper;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;

import java.time.*;

public class TimeHelper {
    //parameters
    @FXML
    private static ObservableList<LocalTime> startHours = FXCollections.observableArrayList();
    @FXML
    private static ObservableList<LocalTime> endHours = FXCollections.observableArrayList();

    public static ObservableList<LocalTime> getStartHours() {
        return startHours;
    }

    public static ObservableList<LocalTime> getEndHours() {
        return endHours;
    }

    public void loadTimes() {
        // Determine user's timezone
        ZoneId currentZone = ZoneId.systemDefault();


        // Business hours (8 AM - 10 PM ET in user's timezone)
        ZonedDateTime zonedStartTime = ZonedDateTime.of(LocalDate.now(), LocalTime.of(8, 0), currentZone);
        ZonedDateTime zonedEndTime = ZonedDateTime.of(LocalDate.now(), LocalTime.of(22, 0), currentZone);

        // Clear existing times
        startHours.clear();
        endHours.clear();


        while (zonedStartTime.isBefore(zonedEndTime)) {
            startHours.add(zonedStartTime.toLocalTime());
            endHours.add(zonedStartTime.toLocalTime());
            zonedStartTime = zonedStartTime.plusMinutes(30);
        }
    }

    public static void checkTimeOverlap(LocalTime startTime, LocalTime endTime){

        if (startTime.isBefore(endTime)) {
            System.out.println("That time works");
        } else {
            displayErrorMessage("Make sure your start time is before your end time.");
        }
    }

    // Helper function to display the popup
    private static void displayErrorMessage(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

}


