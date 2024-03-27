package com.brandonlagasse.scheduler2.controller;

import com.brandonlagasse.scheduler2.dao.AppointmentDAO;
import com.brandonlagasse.scheduler2.dao.ContactDAO;
import com.brandonlagasse.scheduler2.dao.CustomerDAO;
import com.brandonlagasse.scheduler2.dao.UserDAO;
import com.brandonlagasse.scheduler2.helper.TimeHelper;
import com.brandonlagasse.scheduler2.model.Appointment;
import com.brandonlagasse.scheduler2.model.Contact;
import com.brandonlagasse.scheduler2.model.Customer;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
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

    public void onSave(ActionEvent actionEvent) throws SQLException {

         int id = Integer.parseInt(appointmentIdField.getText());
         String title = titleField.getText();
         String description = descriptionField.getText();
         String location = locationField.getText();
         String type = typeField.getText();

         //Create start LocalDateTime object
         LocalTime startTime = startCombo.getSelectionModel().getSelectedItem();
         LocalDate startDate = startDatePicker.getValue();

         LocalDateTime startLdt = LocalDateTime.of(startDate, startTime);

         //Create end LocalDateTime object
         LocalTime endTime = endCombo.getSelectionModel().getSelectedItem();
         LocalDate endDate = endDatePicker.getValue();

//         TimeHelper.checkDateOverlap(startDate,endDate);
//        TimeHelper.checkTimeOverlap(startTime,endTime);

        System.out.println("Start Date: " + startDate);
        System.out.println("End Date" + endDate);
        if (startDate.isAfter(endDate)) {
            Alert alert = new Alert(Alert.AlertType.ERROR);alert.setHeaderText(null);alert.setContentText("Start date must be before end date");alert.showAndWait();
return;
        }


        if (!TimeHelper.checkTimeOverlap(startTime, endTime)) {

            Alert alert = new Alert(Alert.AlertType.ERROR);alert.setHeaderText(null);alert.setContentText("Start time must be before end time");alert.showAndWait();
return;
        }

        LocalDateTime endLdt = LocalDateTime.of(endDate,endTime);

         //Check customerId against DAO for valid id
         int customerId = Integer.parseInt(customerIdField.getText());
         CustomerDAO customerDAO = new CustomerDAO();
         if (!customerDAO.customerExists(customerId)) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Invalid customer ID. Please enter a valid customer ID.");
            alert.showAndWait();
            return;  // Exit if the customer ID is invalid
         }
         //Check userId against DAO for valid id
        UserDAO userDAO = new UserDAO();
        int userId = Integer.parseInt(userIdField.getText());
        if (!userDAO.userExists(userId)) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Invalid user ID. Please enter a valid user ID.");
            alert.showAndWait();
            return;  // Exit if the user ID is invalid
        }

         int contactId = contactCombo.getSelectionModel().getSelectedItem().getId();

         Appointment appointment = new Appointment(id,title,description,location,type,startLdt,endLdt,customerId,userId,contactId);
         AppointmentDAO appointmentDAO = new AppointmentDAO();
         appointmentDAO.update(appointment);

        //Exit
        try {
            Parent customerScene = FXMLLoader.load(getClass().getResource("/com/brandonlagasse/scheduler2/appointment-view.fxml"));
            Scene scene = new Scene(customerScene);
            Stage window = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            window.setScene(scene);
            window.show();
        } catch (IOException e) {
            System.err.println("Error loading main-view.fxml: " + e.getMessage());
        }

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

        //LocalDate appointmentDate = appointment.getStart().toLocalDate();

        TimeHelper timeHelper = new TimeHelper();
        timeHelper.loadTimes();

        //Set up LTDs
        LocalDateTime ltdStart = appointment.getStart();
        LocalDateTime ltdEnd = appointment.getEnd();

        LocalTime startTime = ltdStart.toLocalTime();
        LocalDate startDate = ltdStart.toLocalDate();

        LocalTime endTime = ltdEnd.toLocalTime();
        LocalDate endDate = ltdEnd.toLocalDate();


        startCombo.setItems(TimeHelper.getStartHours());

        endCombo.setItems(TimeHelper.getEndHours());

        contactCombo.setItems(allContacts);

        //Set transfer appointments start/end times
        startCombo.setValue(startTime);
        endCombo.setValue(endTime);

        //Set Contact combo
        int contactId = appointment.getContactId();
        Contact contact = null;
        try {
            contact = contactDAO.getById(contactId);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        contactCombo.setValue(contact);

        //Set up date picker
        startDatePicker.setValue(startDate);
        endDatePicker.setValue(endDate);

    }

    public void onStartCombo(ActionEvent actionEvent) {
//        if(endCombo != null) {
//            TimeHelper.checkTimeOverlap(startCombo, startCombo.getValue(), endCombo.getValue());
//        }else {
//
//        }
    }

    public void onEndCombo(ActionEvent actionEvent) {
//        if(startCombo != null) {
//            TimeHelper.checkTimeOverlap(endCombo, startCombo.getValue(), endCombo.getValue());
//        }else {
//            System.out.println("Waiting for start selection");
//        }
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
//        if (endDatePicker != null) {
//            if (startDatePicker.getValue().isAfter(LocalDate.now())) {
//                TimeHelper.checkDateOverlap(startDatePicker, startDatePicker.getValue(), endDatePicker.getValue());
//            } else {
//                TimeHelper.displayErrorMessage("Start date cannot be before the current date.");
//               // startDatePicker.setValue(null);
//            }
//        } else {
//            System.out.println("Waiting for end selection");
//        }
    }

    public void onEndDate(ActionEvent actionEvent) {
//        if (startDatePicker != null) {
//            TimeHelper.checkDateOverlap(endDatePicker, startDatePicker.getValue(), endDatePicker.getValue());
//        } else {
//            System.out.println("Waiting for start selection");
//        }
    }

    public void onContactCombo(ActionEvent actionEvent) {
    }
}
