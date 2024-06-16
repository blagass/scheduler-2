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


    /**
     * This acts as a helper function to display alerts
     * @param message this will output the message to the alert, depending on how it's utilized from another method
     */
    public static void displayErrorMessage(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

//    /**
//     * This method performs a check for overlaping dates when selecting saving a new appointment
//     * @param startDate this parameter holds the start date in LocalDate
//     * @param endDate this parameter holds the end date in LocalDate
//     * @return Boolean returns true if the overlap checks pass
//     */
//    public static boolean checkDateOverlap(LocalDate startDate, LocalDate endDate){
//
//        if (startDate.isBefore(LocalDate.now())) {
//            System.out.println("Start date cannot be in the past.");
//            return false;
//        }
//
//        if (startDate.isAfter(endDate)) {
//            System.out.println("Start date must be before the end date.");
//            return false;
//        }
//
//        return true;
//    }
//
//    /**
//     * This method checks for time overlaps, similar to the date overlap checker.
//     * @param startTime - this is a LocalTime variable to check that the start times and end times aren't inverted
//     * @param endTime -  this parameter is used to end the mark of the opening hours.
//     * @return boolean returns true if both overlap checks pass.
//     */
//    public static boolean  checkTimeOverlap(LocalTime startTime, LocalTime endTime){
//
//        if (startTime.isBefore(LocalTime.now())) {
//            System.out.println("Start time cannot be in the past.");
//            return false;
//        }
//
//        // check to see if the start time is after the end time
//        if (startTime.isAfter(endTime) || startTime.equals(endTime)) {
//            System.out.println("Start date must be before the end date.");
//            return false;
//        }
//
//        return true;
//    }
//



}


