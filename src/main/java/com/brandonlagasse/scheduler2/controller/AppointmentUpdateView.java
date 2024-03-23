package com.brandonlagasse.scheduler2.controller;

import com.brandonlagasse.scheduler2.dao.AppointmentDAO;
import com.brandonlagasse.scheduler2.dao.ContactDAO;
import com.brandonlagasse.scheduler2.helper.TimeHelper;
import com.brandonlagasse.scheduler2.model.Appointment;
import com.brandonlagasse.scheduler2.model.Contact;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListCell;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class AppointmentUpdateView implements Initializable {
    public TextField titleField;
    public TextField descriptionField;
    public TextField locationField;
    public TextField startField;
    public TextField endField;
    public TextField customerIdField;
    public TextField userIdField;
    public ComboBox<Contact> contactCombo;
    public TextField appointmentIdField;
    public TextField typeField;
    public ComboBox<LocalTime> startCombo;
    public ComboBox<LocalTime> endCombo;
    public DatePicker startDatePicker;
    public DatePicker endDatePicker;

    public void onExit(ActionEvent actionEvent) {
        try {
            Parent customerScene = FXMLLoader.load(getClass().getResource("/com/brandonlagasse/scheduler2/appointment-view.fxml"));
            Scene scene = new Scene(customerScene);
            Stage window = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            window.setScene(scene);
            window.show();
        } catch (IOException e) {
            System.err.println("Error loading appointment-view.fxml: " + e.getMessage());
        }
    }

    public void onSave(ActionEvent actionEvent) {
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ContactDAO contactDAO = new ContactDAO();
        AppointmentDAO appointmentDAO = new AppointmentDAO();

        ObservableList<Contact> allContacts = null;
        try {
            allContacts = contactDAO.getList();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        Appointment appointment = AppointmentView.getTransferAppointment();

        appointmentIdField.setText(String.valueOf(appointment.getId()));
        titleField.setText(appointment.getTitle());
        descriptionField.setText(appointment.getDescription());
        locationField.setText(appointment.getLocation());
        typeField.setText(appointment.getType());
        customerIdField.setText(String.valueOf(appointment.getCustomerId()));
        userIdField.setText(String.valueOf(appointment.getUserId()));

        LocalDate appointmentDate = appointment.getStart().toLocalDate();

        TimeHelper timeHelper = new TimeHelper();
        timeHelper.loadTimes();


        startCombo.setItems(TimeHelper.getStartHours());

        endCombo.setItems(TimeHelper.getEndHours());

        contactCombo.setItems(allContacts);


    }

    public void onStartCombo(ActionEvent actionEvent) {
        if(endCombo != null) {
            TimeHelper.checkTimeOverlap(startCombo, startCombo.getValue(), endCombo.getValue());
        }else {
            System.out.println("Waiting for end selection");
        }
    }

    public void onEndCombo(ActionEvent actionEvent) {
        if(startCombo != null) {
            TimeHelper.checkTimeOverlap(endCombo, startCombo.getValue(), endCombo.getValue());
        }else {

            System.out.println("Waiting for start selection");
        }
    }

//    public void onStartDate(ActionEvent actionEvent) {
//        if (endDatePicker != null && startDatePicker.getValue().isAfter(LocalDate.now())) {
//            TimeHelper.checkDateOverlap(startDatePicker, startDatePicker.getValue(), endDatePicker.getValue());
//        } else if (endDatePicker!=null) {
//            TimeHelper.checkDateOverlap(startDatePicker, startDatePicker.getValue(), endDatePicker.getValue());
//        } else {
//            System.out.println("Waiting for end selection");
//        }
//    }
//
//    public void onEndDate(ActionEvent actionEvent) {
//        if (startDatePicker != null) {
//            TimeHelper.checkDateOverlap(endDatePicker, startDatePicker.getValue(), endDatePicker.getValue());
//        } else {
//            System.out.println("Waiting for start selection");
//        }
//    }

    public void onStartDate(ActionEvent actionEvent) {
        if (endDatePicker != null) {
            if (startDatePicker.getValue().isAfter(LocalDate.now())) {
                TimeHelper.checkDateOverlap(startDatePicker, startDatePicker.getValue(), endDatePicker.getValue());
            } else {
                TimeHelper.displayErrorMessage("Start date cannot be before the current date.");
                startDatePicker.setValue(null);
            }
        } else {
            System.out.println("Waiting for end selection");
        }
    }

    public void onEndDate(ActionEvent actionEvent) {
        if (startDatePicker != null) {
            TimeHelper.checkDateOverlap(endDatePicker, startDatePicker.getValue(), endDatePicker.getValue());
        } else {
            System.out.println("Waiting for start selection");
        }
    }

    public void onContactCombo(ActionEvent actionEvent) {
    }
}
