package com.brandonlagasse.scheduler2.controller;

import com.brandonlagasse.scheduler2.dao.AppointmentDAO;
import com.brandonlagasse.scheduler2.model.Appointment;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

/**
 * This is the main view to see all the appointments available. It's also the way to navigate to add and update appointment views.This is connected directly to the main view.
 */
public class AppointmentView implements Initializable {
    public TableView<Appointment> appointmentTable;
    public TableColumn idCol;
    public TableColumn titleCol;
    public TableColumn descriptionCol;
    public TableColumn locationCol;
    public TableColumn typeCol;
    public TableColumn startCol;
    public TableColumn endCol;
    public TableColumn customerIdCol;
    public TableColumn userIdCol;
    public TableColumn contactIdCol;
    public RadioButton byWeekCombo;
    public RadioButton byMonthCombo;
    public RadioButton viewAllCombo;
    private ObservableList<Appointment> allAppointments;
    private static Appointment transferAppointment;


    /**
     * OnExit takes the user back to the main view screen.
     * @param actionEvent Triggered by the Go Back button
     */
    public void onExit(ActionEvent actionEvent) {
        try {
            Parent customerScene = FXMLLoader.load(getClass().getResource("/com/brandonlagasse/scheduler2/main-view.fxml"));
            Scene scene = new Scene(customerScene);
            Stage window = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            window.setScene(scene);
            window.show();
        } catch (IOException e) {
            System.err.println("Error loading main-view.fxml: " + e.getMessage());
        }
    }

    /**
     * This method takes the user to the Add Appointment screen where they can add a new user to the database.
     * @param actionEvent Triggered kby the Add Appointment button
     */
    public void onAddAppointment(ActionEvent actionEvent) {
        try {
            Parent customerScene = FXMLLoader.load(getClass().getResource("/com/brandonlagasse/scheduler2/appointment-add-view.fxml"));
            Scene scene = new Scene(customerScene);
            Stage window = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            window.setScene(scene);
            window.show();
        } catch (IOException e) {
            System.err.println("Error loading appointment-add-view.fxml: " + e.getMessage());
        }
    }

    /**
     * This takes the user to the screen where they can update the properties of the appointment they selected in the table view.
     * @param actionEvent Triggered by the Update button
     */
    public void onUpdateAppointment(ActionEvent actionEvent) {

        transferAppointment = appointmentTable.getSelectionModel().getSelectedItem();
        if (transferAppointment == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Please select an appointment to update.");
            alert.showAndWait();} else{
        try {
            Parent customerScene = FXMLLoader.load(getClass().getResource("/com/brandonlagasse/scheduler2/appointment-update-view.fxml"));
            Scene scene = new Scene(customerScene);
            Stage window = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            window.setScene(scene);
            window.show();
        } catch (IOException e) {
            System.err.println("Error loading appointment-update-view.fxml: " + e.getMessage());
        }}
    }

    /**
     * Deletes the currently selected appointment from the database
     * @param actionEvent Triggered by selected an appointment, then clicking the Delete button
     * @throws SQLException Simple catch to make sure appointment can be deleted from database
     */
    public void onDeleteAppointment(ActionEvent actionEvent) throws SQLException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete?");
        alert.setHeaderText("Are you sure you want to delete?");
        alert.setContentText("There's no going back!");

        Optional<ButtonType> confirmation = alert.showAndWait();
        if (confirmation.isPresent() && confirmation.get() == ButtonType.OK)
        {

            Appointment selectedAppointment = appointmentTable.getSelectionModel().getSelectedItem();
            AppointmentDAO appointmentDAO = null;
            if (selectedAppointment == null) {
                Alert a = new Alert(Alert.AlertType.ERROR);
                a.setTitle("Error");
                a.setHeaderText(null);
                a.setContentText("Please select an appointment to delete.");
                a.showAndWait();
            } else {
                appointmentDAO = new AppointmentDAO();
                System.out.println("Number of customers in the database: " + appointmentDAO.getList().size());
                appointmentDAO.delete(selectedAppointment.getId());
                System.out.println("Number of customers in the database: " + appointmentDAO.getList().size());

                ObservableList<Appointment> allAppointments = FXCollections.observableArrayList();
                allAppointments.setAll(appointmentDAO.getList());
                appointmentTable.setItems(allAppointments);
            }
        }
    }

    /**
     * Filters the current appointment tableview to show only appointments by week.
     * LAMBDA - This method also uses a Lambda to filter through the new all appointments and add the collection to an ObservableList
     * @param actionEvent Triggered by selected th By Weeek radio button
     */
    public void byWeek(ActionEvent actionEvent) {
        byMonthCombo.setSelected(false);
        viewAllCombo.setSelected(false);
        LocalDate today = LocalDate.now();
        ObservableList<Appointment> appointmentsByWeek = allAppointments.stream()
                .filter(app -> app.getStart().toLocalDate().isAfter(today.minusDays(7)) &&
                        app.getStart().toLocalDate().isBefore(today.plusDays(1)))
                .collect(Collectors.toCollection(FXCollections::observableArrayList));
        appointmentTable.setItems(appointmentsByWeek);
    }

    /**
     *  Filters the current appointment tableview to show only appointments by month.
     *  LAMBDA - This method also uses a Lambda to filter through the new all appointments and add the collection to an ObservableList
     * @param actionEvent Triggered by selecting the By Month radio button
     */
    public void byMonth(ActionEvent actionEvent) {
        byWeekCombo.setSelected(false);
        viewAllCombo.setSelected(false);
        LocalDate today = LocalDate.now();
        ObservableList<Appointment> appointmentsByMonth = allAppointments.stream()
                .filter(app -> app.getStart().toLocalDate().getMonth() == today.getMonth())
                .collect(Collectors.toCollection(FXCollections::observableArrayList));
        appointmentTable.setItems(appointmentsByMonth);
    }

    /**
     * Sets the current Appointments tableview to show all appointments in the database
     * @param actionEvent This is triggered by selecting the View All radio button
     */
    public void onViewAll(ActionEvent actionEvent) {
        byWeekCombo.setSelected(false);
        byMonthCombo.setSelected(false);
        appointmentTable.setItems(allAppointments);
    }

    /**
     * This initialize populates the Appointments tableview with all the appointments in the database
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        AppointmentDAO dao = new AppointmentDAO();

        try {
            allAppointments = dao.getList();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        descriptionCol.setCellValueFactory(new PropertyValueFactory<>("description"));
        locationCol.setCellValueFactory(new PropertyValueFactory<>("location"));
        typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
        startCol.setCellValueFactory(new PropertyValueFactory<>("start"));
        endCol.setCellValueFactory(new PropertyValueFactory<>("end"));
        customerIdCol.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        userIdCol.setCellValueFactory(new PropertyValueFactory<>("userId"));
        contactIdCol.setCellValueFactory(new PropertyValueFactory<>("contactId"));

        appointmentTable.setItems(allAppointments);
    }

    /**
     * This method returns the transfered appointment to throw to other screens
     * @return This returns appointment used for appointment update
     */
    public static Appointment getTransferAppointment() {
        return transferAppointment;
    }

//    public void setTransferAppointment(Appointment transferAppointment) {
//        this.transferAppointment = transferAppointment;
//    }


}
