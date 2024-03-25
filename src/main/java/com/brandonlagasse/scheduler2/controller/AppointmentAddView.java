package com.brandonlagasse.scheduler2.controller;

import com.brandonlagasse.scheduler2.dao.AppointmentDAO;
import com.brandonlagasse.scheduler2.dao.ContactDAO;
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
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalTime;
import java.util.ResourceBundle;

public class AppointmentAddView implements Initializable {
    public TextField titleField;
    public TextField LocationField;
    public TextField descriptionField;
    public TextField typeField;
    public TextField startField;
    public TextField endField;
    public TextField customerIdField;
    public TextField userIdField;
    public ComboBox<Contact> contactCombo;
    public ComboBox<LocalTime> startTimeCombo;
    public ComboBox<LocalTime> endTimeCombo;
    public DatePicker startDatePicker;
    public DatePicker onEndDatePicker;

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

    public void onStartTime(ActionEvent actionEvent) {
    }

    public void onEndCombo(ActionEvent actionEvent) {
    }

    public void onStartDate(ActionEvent actionEvent) {
    }

    public void onEndDate(ActionEvent actionEvent) {
    }

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
}
