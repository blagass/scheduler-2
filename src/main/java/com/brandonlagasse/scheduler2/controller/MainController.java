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

/**
 * This is the main controller that loads after login. This presents the user with navigational options, as well as information related to User Appointments.
 * Users can navigate to Customers, Appointments, Reports, or Exit from this screen.
 * I would change this to a separate controller in the future, as it's current implementation isn't practical as it's linked to the main.java file.
 */
public class MainController implements Initializable {
    public Button exitButton;
    public TextArea appointmentArea;
    @FXML
    private Label welcomeText;

    public int passedUserId;

    /**
     * This method exits the application for the user. Since this is the first scene that appears after the login, there's no where to go from here.
     */
    @FXML
    protected void onExitButton() {

        Stage stage = (Stage) exitButton.getScene().getWindow();
        stage.close();
    }

    /**
     * This sends users to the Customer View; the main hub for viewing customers and updating/adding new customers.
     * @param actionEvent This is triggered by clicking on the Exit button
     */
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

    /**
     * This method navigates uers to the Appointment View; similar to customer, this is the main hub for appointment related functionality, like sending users to add/update areas.
     * @param actionEvent Triggered by the Appointments button
     */
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

    /**
     * This sends users to the Reports View; this is where users can find three different reports about Customers and thier Appointments.
     * @param actionEvent Triggered by clicking on the Reports button.
     */
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

    /**
     * The main purpose of this initialize is to present navigation to Customers, Appointments, and Report views, as well as show the user if they have any appointments within 15 minutes of login time in the designated UI area.
     */
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