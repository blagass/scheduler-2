package com.brandonlagasse.scheduler2.controller;

import com.brandonlagasse.scheduler2.dao.CountryDAO;
import com.brandonlagasse.scheduler2.dao.CustomerDAO;
import com.brandonlagasse.scheduler2.dao.FirstLevelDivisionDAO;
import com.brandonlagasse.scheduler2.model.Country;
import com.brandonlagasse.scheduler2.model.Customer;
import com.brandonlagasse.scheduler2.model.FirstLevelDivision;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class CustomerUpdateView implements Initializable {
    @FXML
    public TextField customerIdField;
    @FXML
    public TextField nameField;
    @FXML
    public TextField addressField;
    @FXML
    public TextField postalCodeField;
    @FXML
    public TextField phoneField;

    @FXML
    public ComboBox<Country> countryCombo;
    @FXML
    public ComboBox<FirstLevelDivision> fldCombo;

    @FXML
    public CountryDAO countryDAO = new CountryDAO();
    @FXML
    public FirstLevelDivisionDAO fldDAO = new FirstLevelDivisionDAO();

    @FXML
    public static Customer passedCustomer;
    @FXML
    private ObservableList<FirstLevelDivision> usDivisions;
    @FXML
    private ObservableList<FirstLevelDivision> canadaDivisions;
    @FXML
    private ObservableList<FirstLevelDivision> ukDivisions;



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        passedCustomer = CustomerView.getCustomerToPass();


        customerIdField.setText(String.valueOf(passedCustomer.getId()));
        nameField.setText(passedCustomer.getName());
        addressField.setText(passedCustomer.getAddress());
        postalCodeField.setText(passedCustomer.getPostalCode());
        phoneField.setText(passedCustomer.getPhone());

        try {
            usDivisions = FirstLevelDivisionDAO.usStates();
            canadaDivisions = FirstLevelDivisionDAO.canadaStates();
            ukDivisions = FirstLevelDivisionDAO.ukStates();

            fldCombo.setItems(fldDAO.getList());
            countryCombo.setItems(countryDAO.getList());
            setCountryAndFldCombo();


        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }


    private void setCountryAndFldCombo() throws SQLException {
        // Find the matching First-Level Division for the customer
        FirstLevelDivision matchingFld = null;
        for (FirstLevelDivision fld : fldDAO.getList()) {
            if (fld.getId() == passedCustomer.getDivisionId()) {
                matchingFld = fld;
                break;
            }
        }

        // Set both the FLD and the Country
        if (matchingFld != null) {
            // Set the First-Level Division
            fldCombo.getSelectionModel().select(matchingFld);

            // Set the Country
            int countryId = matchingFld.getCountryId();
            for (Country country : countryCombo.getItems()) {
                if (country.getId() == countryId) {
                    countryCombo.getSelectionModel().select(country);
                    break;
                }
            }
        }
    }




    @FXML
    public void onCancel(ActionEvent actionEvent) {

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
    @FXML
    public void onSave(ActionEvent actionEvent) throws SQLException {
        Customer customer = new Customer(-1,"ted","asdfsa","14326","544-353-3333",4,"Place");
        CustomerDAO customerDAO = new CustomerDAO();

        int customerId = Integer.parseInt(customerIdField.getText());
        String customerName = nameField.getText();
        String customerAddress = addressField.getText();
        String customerPostal = postalCodeField.getText();
        String customerPhone = phoneField.getText();
        int customerDivisionId  = countryCombo.getSelectionModel().getSelectedItem().getId();
        String customerDivisionName = fldCombo.getSelectionModel().getSelectedItem().toString();

        customer.setId(customerId);
        customer.setName(customerName);
        customer.setAddress(customerAddress);
        customer.setPostalCode(customerPostal);
        customer.setPhone(customerPhone);
        customer.setDivisionId(customerDivisionId);
        customer.setDivisionName(customerDivisionName);

        customerDAO.update(customer);

        //Exit
        try {
            Parent customerScene = FXMLLoader.load(getClass().getResource("/com/brandonlagasse/scheduler2/customer-view.fxml"));
            Scene scene = new Scene(customerScene);
            Stage window = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            window.setScene(scene);
            window.show();
        } catch (IOException e) {
            System.err.println("Error loading main-view.fxml: " + e.getMessage());
        }

    }

    @FXML
    public void customerPass(Customer customer){

    }

    public void onCountryCombo(ActionEvent actionEvent) throws SQLException {
        Country country = countryCombo.getSelectionModel().getSelectedItem();

        if (country != null) {

            ObservableList<FirstLevelDivision> matchingDivisions = null;
            switch(country.getId()) {
                case 1:
                    matchingDivisions = usDivisions;
                    break;
                case 2:
                    matchingDivisions = ukDivisions;
                    break;
                case 3:
                    matchingDivisions = canadaDivisions;
                    break;
                default:
                    System.err.println("No matching Country found");
            }

            if (matchingDivisions != null) {
                fldCombo.setItems(matchingDivisions);
                fldCombo.getSelectionModel().clearSelection();
            }
        }
    }

    public void onFldCombo(ActionEvent actionEvent) {
    }


    public static Customer getPassedCustomer() {
        return passedCustomer;
    }


    public static void setPassedCustomer(Customer passedCustomer) {
        CustomerUpdateView.passedCustomer = passedCustomer;
    }
}
