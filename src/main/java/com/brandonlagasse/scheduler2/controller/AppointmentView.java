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
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

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
    private ObservableList<Appointment> allAppointments;

    private static Appointment transferAppointment;

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

    public void onDeleteAppointment(ActionEvent actionEvent) throws SQLException {

        Appointment selectedAppointment = appointmentTable.getSelectionModel().getSelectedItem();
        AppointmentDAO appointmentDAO = null;
        if (selectedAppointment == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Please select an appointment to delete.");
            alert.showAndWait();
            appointmentDAO = new AppointmentDAO();
        } else {

            System.out.println("Number of customers in the database: " + appointmentDAO.getList().size());
            appointmentDAO.delete(selectedAppointment.getId());
            System.out.println("Number of customers in the database: " + appointmentDAO.getList().size());

            ObservableList<Appointment> allAppointments = FXCollections.observableArrayList();
            allAppointments.setAll(appointmentDAO.getList());
            appointmentTable.setItems(allAppointments);
        }
    }

    public void byWeek(ActionEvent actionEvent) {

    }

    public void byMonth(ActionEvent actionEvent) {
    }

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

    public static Appointment getTransferAppointment() {
        return transferAppointment;
    }

    public void setTransferAppointment(Appointment transferAppointment) {
        this.transferAppointment = transferAppointment;
    }
}
