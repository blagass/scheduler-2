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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

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
    public static Customer customerToPass;

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
    @FXML
    public void onUpdateCustomer(ActionEvent actionEvent) {
        customerToPass = customerTableView.getSelectionModel().getSelectedItem();
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
    @FXML
    public void onDeleteCustomer(ActionEvent actionEvent) {
    }
    @FXML
    public static Customer getCustomerToPass() {
        return customerToPass;
    }
    @FXML
    public void setCustomerToPass(Customer customerToPass) {
        CustomerView.customerToPass = customerToPass;
    }
}
