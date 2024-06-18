package com.brandonlagasse.scheduler2.controller;

import com.brandonlagasse.scheduler2.dao.CustomerDAO;
import com.brandonlagasse.scheduler2.model.Customer;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
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
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * This is the controller for the main customer view, which add/update stem from. This view provides a Customer tableview that shows all customers, as well as filtering.
 */
public class CustomerView implements Initializable {
    @FXML
    public TableView<Customer> customerTableView;
    @FXML
    public TableColumn<?,?> idCol;
    @FXML
    public TableColumn<?,?> nameCol;
    @FXML
    public TableColumn<?,?> addressCol;
    @FXML
    public TableColumn<?,?> postalCodeCol;
    @FXML
    public TableColumn<?,?> phoneCol;
    @FXML
    public TableColumn<?,?> divisionIdCol;
    @FXML
    public TableColumn<?,?> divisionNameCol;
    @FXML
    private ObservableList<Customer> transferCustomers;

    @FXML
    public Customer customer = null;

    @FXML
    public static Customer customerToPass;
    @FXML
    public TextField searchField;

    /**
     * Ths populates the customerTableView with all customers, retrieved through the CustomerDAO.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)  {
        CustomerDAO customerDAO = new CustomerDAO();

        try {
            transferCustomers = customerDAO.getList();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

            idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
            nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
            addressCol.setCellValueFactory(new PropertyValueFactory<>("address"));
            postalCodeCol.setCellValueFactory(new PropertyValueFactory<>("postalCode"));
            phoneCol.setCellValueFactory(new PropertyValueFactory<>("phone"));
            divisionIdCol.setCellValueFactory(new PropertyValueFactory<>("divisionId"));
            divisionNameCol.setCellValueFactory(new PropertyValueFactory<>("divisionName"));

            customerTableView.setItems(transferCustomers);
    }

    /**
     * Navigates the user back to the Main View
     * @param actionEvent triggered by clicking on the Go Back button
     */
    @FXML
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
     * This navigates the user to the CustomerUpdateView to add a new customer to the database
     * @param actionEvent
     */
    @FXML
    public void onAddNewCustomer(ActionEvent actionEvent) {
        try {
            Parent customerScene = FXMLLoader.load(getClass().getResource("/com/brandonlagasse/scheduler2/customer-add-view.fxml"));
            Scene scene = new Scene(customerScene);
            Stage window = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            window.setScene(scene);
            window.show();
        } catch (IOException e) {
            System.err.println("Error loading customer-add-view.fxml: " + e.getMessage());
        }
    }

    /**
     * This navigates users to the customerUpdateView, transferring the selected customer data to populate fields and combo boxes.
     * @param actionEvent
     */
    @FXML
    public void onUpdateCustomer(ActionEvent actionEvent) {


        customerToPass = customerTableView.getSelectionModel().getSelectedItem();
        if (customerToPass == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Please select a customer to update.");
            alert.showAndWait();
        } else {

            CustomerUpdateView.setPassedCustomer(customerToPass);
            System.out.println(customerToPass);

            try {
                Parent customerScene = FXMLLoader.load(getClass().getResource("/com/brandonlagasse/scheduler2/customer-update-view.fxml"));
                Scene scene = new Scene(customerScene);
                Stage window = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
                window.setScene(scene);
                window.show();
            } catch (IOException e) {
                System.err.println("Error loading customer-update-view.fxml: " + e.getMessage());
            }

        }
    }

    /**
     * This removes a customer from the database using the CustomerDAO. It also checks for any related appointments before deleting.
     * @param actionEvent triggered by the Delete button
     * @throws SQLException this will catch an error if the CustomerDAO has issues
     */
    @FXML
    public void onDeleteCustomer(ActionEvent actionEvent) throws SQLException {

        Alert a = new Alert(Alert.AlertType.CONFIRMATION);
        a.setTitle("Delete?");
        a.setHeaderText("Are you sure you want to delete?");
        a.setContentText("There's no going back!");

        Optional<ButtonType> confirmation = a.showAndWait();
        if (confirmation.isPresent() && confirmation.get() == ButtonType.OK) {

            Customer customerToDelete = customerTableView.getSelectionModel().getSelectedItem();

            if (customerToDelete == null) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("Please select a customer to delete.");
                alert.showAndWait();
                return;
            }

            CustomerDAO customerDAO = new CustomerDAO();
            if (customerDAO.delete(customerToDelete.getId())) {
                customerTableView.setItems(customerDAO.getList());
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Success");
                alert.setHeaderText(null);
                alert.setContentText("Customer deleted successfully.");
                alert.showAndWait();
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("Failed to delete customer.");
                alert.showAndWait();
            }
        }
    }

    /**
     * Getter for retrieving the customer that is being passed to other screens
     * @return this returns the customerToPass public static parameter
     */
    @FXML
    public static Customer getCustomerToPass() {
        return customerToPass;
    }

    /**
     * This method retrieves input text for the Search functionality, makes sure it's not empty.
     */
    @FXML
    public void onSearchCustomer() {

        String searchText = searchField.getText();
        CustomerDAO customerDAO = new CustomerDAO();

        try {
            if (searchText.isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Warning");
                alert.setHeaderText(null);
                alert.setContentText("Search field cannot be empty.");
                alert.showAndWait();
            } else {
                transferCustomers = customerDAO.searchByName(searchText);
                customerTableView.setItems(transferCustomers);
            }
        } catch (SQLException e) {

            e.printStackTrace();
        }
    }
}
