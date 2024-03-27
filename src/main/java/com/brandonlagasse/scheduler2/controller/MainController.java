package com.brandonlagasse.scheduler2.controller;

import com.brandonlagasse.scheduler2.dao.AppointmentDAO;
import com.brandonlagasse.scheduler2.model.Appointment;
import com.brandonlagasse.scheduler2.model.User;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class MainController implements Initializable {
    public Button exitButton;
    public TextArea appointmentArea;
    @FXML
    private Label welcomeText;

    public int passedUserId;

    @FXML
    protected void onExitButton() {

        Stage stage = (Stage) exitButton.getScene().getWindow();
        stage.close();
    }

    public void onCustomerButton(ActionEvent actionEvent) {
        try {
            Parent customerScene = FXMLLoader.load(getClass().getResource("/com/brandonlagasse/scheduler2/customer-view.fxml"));
            Scene scene = new Scene(customerScene);
            Stage window = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            window.setScene(scene);
            window.show();
        } catch (IOException e) {
            System.err.println("Error loading customer-view.fxml: " + e.getMessage());
        }
    }

    public void onAppointmentButton(ActionEvent actionEvent) {
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

    public void onReportButton(ActionEvent actionEvent) {
        try {
            Parent customerScene = FXMLLoader.load(getClass().getResource("/com/brandonlagasse/scheduler2/report-view.fxml"));
            Scene scene = new Scene(customerScene);
            Stage window = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            window.setScene(scene);
            window.show();
        } catch (IOException e) {
            System.err.println("Error loading customer-view.fxml: " + e.getMessage());
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
//     passedUserId = LoginView.passUserId;
//
//        ObservableList<User> allUsers = FXCollections.observableArrayList();
//        for(User user : allUsers){
//            if the user.getid == passedUserId, then set the text in the appointmentArea any appointment the user has within 15 minutes of the login time
//        };
//
//
//    }

//        passedUserId = LoginView.passUserId;
//        AppointmentDAO appointmentDAO = new AppointmentDAO();
//        // 1. Fetch Appointments from Database
//        ObservableList<Appointment> appointments = null;
//        try {
//            appointments = appointmentDAO.getList();
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        }
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
//        // 2. Filter for Relevant Appointments
//        LocalDateTime now = LocalDateTime.now();
//        StringBuilder appointmentText = new StringBuilder();
//
//        for (Appointment appointment : appointments) {
//            if (appointment.getUserId() == passedUserId &&
//                    appointment.getStart().toLocalTime().isAfter(LocalTime.from(now)) &&
//                    appointment.getStart().toLocalTime().isBefore(LocalTime.from(now.plusMinutes(15)))) {
//
//                        appointmentText.append(formatter).append("\n");
//            }
//        }
//
//        // 3. Set Text in appointmentArea
//        appointmentArea.setText(appointmentText.toString());
        passedUserId = LoginView.passUserId;

        AppointmentDAO appointmentDAO = new AppointmentDAO();

        ObservableList<Appointment> appointments = null;
        try {
            appointments = appointmentDAO.getList();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");


        LocalDateTime now = LocalDateTime.now();
        StringBuilder appointmentText = new StringBuilder();
        for (Appointment appointment : appointments) {
            if (appointment.getUserId() == passedUserId &&
                    appointment.getStart().toLocalTime().isAfter(LocalTime.from(now)) &&
                    appointment.getStart().toLocalTime().isBefore(LocalTime.from(now.plusMinutes(15)))) {
                appointmentText.append(formatter.format(appointment.getStart())).append("\n"); // Format appointment details
            }
        }

        if (appointmentText.isEmpty()) { // No appointments found
            appointmentArea.setText("No Appointments Coming Up");
        } else {
            appointmentArea.setText(appointmentText.toString());
        }
    }}