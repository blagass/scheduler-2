package com.brandonlagasse.scheduler2.helper;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZonedDateTime;

public class TimeZone {
    public final ZonedDateTime START_HOURS = null;
    private ObservableList<LocalTime> startHours = FXCollections.observableArrayList();
    private ObservableList<LocalTime> endHours = FXCollections.observableArrayList();

    private void loadTimes() {

    }

    public boolean checkOverlap(LocalDateTime start,LocalDateTime end, int customerId, int appointmentId){
        return true;
    }

    public ObservableList<LocalTime> getStartHours() {
        return startHours;
    }

    public void setStartHours(ObservableList<LocalTime> startHours) {
        this.startHours = startHours;
    }

    public ObservableList<LocalTime> getEndHours() {
        return endHours;
    }

    public void setEndHours(ObservableList<LocalTime> endHours) {
        this.endHours = endHours;
    }
}
