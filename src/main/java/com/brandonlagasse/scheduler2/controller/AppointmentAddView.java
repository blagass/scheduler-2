package com.brandonlagasse.scheduler2.controller;

import com.brandonlagasse.scheduler2.dao.AppointmentDAO;
import com.brandonlagasse.scheduler2.dao.ContactDAO;
import com.brandonlagasse.scheduler2.dao.CustomerDAO;
import com.brandonlagasse.scheduler2.dao.UserDAO;
import com.brandonlagasse.scheduler2.helper.TimeHelper;
import com.brandonlagasse.scheduler2.model.Appointment;
import com.brandonlagasse.scheduler2.model.Contact;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ResourceBundle;

/**
 * This Apppointment controller responsible for collecting data to create a new Appointment object to add to the database.
 */
public class AppointmentAddView implements Initializable {
    public TextField titleField;
    //public TextField LocationField;
    public TextField descriptionField;
    public TextField typeField;
    //public TextField startField;
    //public TextField endField;
    public TextField customerIdField;
    public TextField userIdField;
    public ComboBox<Contact> contactCombo;
    public ComboBox<LocalTime> startTimeCombo;
    public ComboBox<LocalTime> endTimeCombo;
    public DatePicker startDatePicker;
    //public DatePicker onEndDatePicker;
    public TextField locationField;
    public DatePicker endDatePicker;

    /**
     *The initialization provides population of the start time, end time, and contact combo boxes
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ContactDAO contactDAO = new ContactDAO();

        ObservableList<Contact> allContacts = null;
        try {
            allContacts = contactDAO.getList();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        TimeHelper timeHelper = new TimeHelper();
        timeHelper.loadTimes();

        startTimeCombo.setItems(TimeHelper.getStartHours());

        endTimeCombo.setItems(TimeHelper.getEndHours());

        contactCombo.setItems(allContacts);
    }

    /**
     * onSave uses the Save button to initiate the collection of data from combo boxes and text fields, and combines them into an Appointment object. This is then called by the AppointmentDAO to add to the database.
     *
     * @param actionEvent Triggered by the Save button
     * @throws SQLException Collects errors that occur when adding to the database
     */
    public void onSave(ActionEvent actionEvent) throws SQLException {
if(!titleField.getText().isEmpty()) {
    //int id = Integer.parseInt(appointmentIdField.getText());
    String title = titleField.getText();
    String description = descriptionField.getText();
    String location = locationField.getText();
    String type = typeField.getText();


    LocalTime startTime = startTimeCombo.getSelectionModel().getSelectedItem();
    LocalDate startDate = startDatePicker.getValue();


    LocalDateTime startLdt = LocalDateTime.of(startDate, startTime);


    LocalTime endTime = endTimeCombo.getSelectionModel().getSelectedItem();
    LocalDate endDate = endDatePicker.getValue();

    System.out.println("Start Date: " + startDate);
    System.out.println("End Date" + endDate);
//        if (startDate.isAfter(endDate)) {
//            Alert alert = new Alert(Alert.AlertType.ERROR);alert.setHeaderText(null);alert.setContentText("Start date must be before end date");alert.showAndWait();
//            return;
//        }
    if (startDate.isAfter(endDate) ||
            startDate.getDayOfWeek() == DayOfWeek.SATURDAY ||
            startDate.getDayOfWeek() == DayOfWeek.SUNDAY) {

        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText(null);
        alert.setContentText("The start date must be before end date, and it cannot land on a weekend.");
        alert.showAndWait();
        return;
    }


    if (!TimeHelper.checkTimeOverlap(startTime, endTime)) {

        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText(null);
        alert.setContentText("The start time must be before end time");
        alert.showAndWait();
        return;
    }


    LocalDateTime endLdt = LocalDateTime.of(endDate, endTime);


    int customerId = Integer.parseInt(customerIdField.getText());
    CustomerDAO customerDAO = new CustomerDAO();


    if (!customerDAO.customerExists(customerId)) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText("Invalid customer ID. Please enter a valid customer ID.");
        alert.showAndWait();
        return;
    }

    UserDAO userDAO = new UserDAO();
    int userId = Integer.parseInt(userIdField.getText());
    if (!userDAO.userExists(userId)) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText("Invalid user ID. Please enter a valid user ID.");
        alert.showAndWait();
        return;
    }

    int contactId = contactCombo.getSelectionModel().getSelectedItem().getId();

    Appointment appointment = new Appointment(-1, title, description, location, type, startLdt, endLdt, customerId, userId, contactId);
    AppointmentDAO appointmentDAO = new AppointmentDAO();
    appointmentDAO.insert(appointment);

    try {
        Parent customerScene = FXMLLoader.load(getClass().getResource("/com/brandonlagasse/scheduler2/appointment-view.fxml"));
        Scene scene = new Scene(customerScene);
        Stage window = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    } catch (IOException e) {
        System.err.println("Error loading appointment-view.fxml: " + e.getMessage());
    }
} else {     Alert alert = new Alert(Alert.AlertType.ERROR);
    alert.setTitle("Error");
    alert.setHeaderText(null);
    alert.setContentText("Please fill in all fields.");
    alert.showAndWait();
    return; }
    }

    /**
     * onExit uses the Go Back button to navigate back to the AppointmentView screen
     * @param actionEvent Triggered by the Go Back button
     */
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


//    public void onStartTime(ActionEvent actionEvent) {
//
////    }
//
//    public void onEndCombo(ActionEvent actionEvent) {
//    }
//
//    public void onStartDate(ActionEvent actionEvent) {
//    }
//
//    public void onEndDate(ActionEvent actionEvent) {
//    }


}
