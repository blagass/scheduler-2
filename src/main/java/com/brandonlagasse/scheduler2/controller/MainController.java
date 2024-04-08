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

import javax.xml.transform.Source;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.sql.Time;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import java.util.TimeZone;

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
//
//        passedUserId = LoginView.passUserId;
//
//        AppointmentDAO appointmentDAO = new AppointmentDAO();
//
//        ObservableList<Appointment> appointments = null;
//
//        try {
//            appointments = appointmentDAO.getList();
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        }
//
//
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
//        LocalDateTime now = LocalDateTime.now();
//
//        boolean hasUpcomingAppointment = false;
//        StringBuilder builder = new StringBuilder("You have no upcoming appointments:\n");
//
//        if (appointments != null) {
//            for (Appointment appointment : appointments) {
//                if (appointment.getUserId() == passedUserId &&
//                        appointment.getStart().isAfter(now) &&
//                        appointment.getStart().isBefore(now.plusMinutes(15))) {
//
//                    builder.append(formatter.format(appointment.getStart())).append("\n");
//                    hasUpcomingAppointment = true;
//                }
//            }
//            appointmentArea.setText(hasUpcomingAppointment ? builder.toString() : "Appointment Coming up");
//        } else{
//            appointmentArea.setText("No Appointments Coming up");
//        }
//
//
//        }
//


//        passedUserId = LoginView.passUserId;
//
//        AppointmentDAO appointmentDAO = new AppointmentDAO();
//        ObservableList<Appointment> appointments = null;
//
//        try {
//            appointments = appointmentDAO.getList();
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        }
//
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
//        LocalDateTime now = LocalDateTime.now();
//        boolean hasUpcomingAppointment = false;
//
//
//        if (appointments != null) {
//            for (Appointment appointment : appointments) {
//                LocalDateTime apptStartTime = appointment.getStart();
//
//                if (appointment.getUserId() == passedUserId &&
//                        apptStartTime.isAfter(now) &&
//                        apptStartTime.isBefore(now.plusMinutes(15))) {
//                    hasUpcomingAppointment = true;
//                }
//            }
//        }
//
//        if (hasUpcomingAppointment) {
//            appointmentArea.setText("Appointments Coming Up");
//
//        } else{appointmentArea.setText("No Appointments coming up");
//    }

//        passedUserId = LoginView.passUserId;
//        AppointmentDAO appointmentDAO = new AppointmentDAO();
//        ObservableList<Appointment> appointments = null;
//
//        try {
//            appointments = appointmentDAO.getList();
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        }
//
//        for (Appointment app: appointments){
//            if(passedUserId == app.getUserId()){
//                LocalDateTime ltd = app.getStart();
//                System.out.println("Appointment Time " + ltd);
//            }else{
//                System.out.println("No Appointment Coming up");
//            }
//        }

        passedUserId = LoginView.passUserId;
        AppointmentDAO appointmentDAO = new AppointmentDAO();
        ObservableList<Appointment> appointments = null;

        try {
            appointments = appointmentDAO.getList();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        ZoneId userTimeZone = ZoneId.systemDefault();
        LocalDateTime now = LocalDateTime.now(userTimeZone);

        for (Appointment app: appointments) {
            if (passedUserId == app.getUserId()) {
                LocalDateTime apptStartUTC = app.getStart();
                LocalDateTime apptStartTimeLocal = apptStartUTC.atZone(ZoneOffset.UTC)
                        .withZoneSameInstant(userTimeZone)
                        .toLocalDateTime();

                System.out.println("Appointment Time: " + apptStartTimeLocal);

                if (apptStartTimeLocal.isAfter(now) &&
                        apptStartTimeLocal.isBefore(now.plusMinutes(30))) {

                    System.out.println("Appointment within 15 minutes!");

                }
            } else {
                System.out.println("No Appointment Coming up");
            }

    }
}}

