package com.brandonlagasse.scheduler2.helper;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;

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

        // Populate startHours
        while (zonedStartTime.isBefore(zonedEndTime)) {
            startHours.add(zonedStartTime.toLocalTime());
            endHours.add(zonedStartTime.toLocalTime());
            zonedStartTime = zonedStartTime.plusMinutes(30);
        }


    }
}

