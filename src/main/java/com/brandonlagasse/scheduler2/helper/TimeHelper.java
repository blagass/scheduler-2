package com.brandonlagasse.scheduler2.helper;

import com.brandonlagasse.scheduler2.dao.JDBC;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.*;

/**
 * This class acts as a helper for all time functions, like checking overlap, retrieving and setting LocalDateTime and it's derivatives, and displaying error messages.
 */
public class TimeHelper {
    //parameters
    @FXML
    private static ObservableList<LocalTime> startHours = FXCollections.observableArrayList();
    @FXML
    private static ObservableList<LocalTime> endHours = FXCollections.observableArrayList();

    /**
     * Getter for getStartHours
     * @return returns the startHours ObservableList
     */
    public static ObservableList<LocalTime> getStartHours() {
        return startHours;
    }

    /**
     * Getter for getEndHours
     * @return returns endHours Observable list
     */
    public static ObservableList<LocalTime> getEndHours() {
        return endHours;
    }

    /**
     * loadTimes method sets up the start/end hours during the work hours within 30 minute increments.
     */
    public void loadTimes() {
        //  timezone
        ZoneId currentZone = ZoneId.systemDefault();


        ZonedDateTime zonedStartTime = ZonedDateTime.of(LocalDate.now(), LocalTime.of(8, 0), currentZone);
        ZonedDateTime zonedEndTime = ZonedDateTime.of(LocalDate.now(), LocalTime.of(22, 0), currentZone);

        // clear  times
        startHours.clear();
        endHours.clear();


        while (zonedStartTime.isBefore(zonedEndTime)) {
            startHours.add(zonedStartTime.toLocalTime());
            endHours.add(zonedStartTime.toLocalTime());
            zonedStartTime = zonedStartTime.plusMinutes(30);
        }
    }

}


