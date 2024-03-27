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


    // Helper function to display the popup
    public static void displayErrorMessage(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static boolean checkDateOverlap(LocalDate startDate, LocalDate endDate){
//        if (startDate.isBefore(endDate) && startDate.isAfter(LocalDate.now()) || startDate.isEqual(LocalDate.now())) {
//            System.out.println("That date works");
////        } else {
//            System.out.println("That Date doesn't work");
//        }
//
//        return true;
        if (startDate.isBefore(LocalDate.now())) {
            System.out.println("Start date cannot be in the past.");
            return false; // Fail if the date is in the past
        }

        // Check 2: Start Date must be before End Date
        if (startDate.isAfter(endDate)) {
            System.out.println("Start date must be before the end date.");
            return false; // Fail if the start date is not before the end date
        }

        // If both checks pass:
        return true;
    }
    public static boolean  checkTimeOverlap(LocalTime startTime, LocalTime endTime){
//        if (startTime.isBefore(endTime)) {
//            System.out.println("That time works");
////        } else {
////            displayErrorMessage("Make sure your start time is before your end time.");
////           // timeBox.getSelectionModel().clearSelection();
////        }
//       // return false;
//    }
//        return false;
        if (startTime.isBefore(LocalTime.now())) {
            System.out.println("Start date cannot be in the past.");
            return false; // Fail if the date is in the past
        }

        // Check 2: Start time must be before End Date
        if (startTime.isAfter(endTime)) {
            System.out.println("Start date must be before the end date.");
            return false; // Fail if the start date is not before the end date
        }

        // If both checks pass:
        return true;
    }

    public static boolean checkOverlap(LocalDateTime start, LocalDateTime end) throws SQLException {
        String sql = "SELECT * FROM appointments WHERE Start < ? AND End > ?";


        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setTimestamp(1, Timestamp.valueOf(start));
        ps.setTimestamp(2, Timestamp.valueOf(end));

        ResultSet rs = ps.executeQuery();
        rs.next();
        return false;
    }

}


