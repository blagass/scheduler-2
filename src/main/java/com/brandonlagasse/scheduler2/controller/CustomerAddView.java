package com.brandonlagasse.scheduler2.controller;

import com.brandonlagasse.scheduler2.dao.CountryDAO;
import com.brandonlagasse.scheduler2.dao.CustomerDAO;
import com.brandonlagasse.scheduler2.dao.FirstLevelDivisionDAO;
import com.brandonlagasse.scheduler2.model.Country;
import com.brandonlagasse.scheduler2.model.Customer;
import com.brandonlagasse.scheduler2.model.FirstLevelDivision;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class CustomerAddView implements Initializable {
    public TextField nameField;
    public TextField addressField;
    public TextField postalCodeField;
    public TextField phoneField;
    public ComboBox<Country> countryCombo;
    public ComboBox<FirstLevelDivision> fldCombo;
    @FXML
    public static Customer passedCustomer;
    @FXML
    private ObservableList<FirstLevelDivision> usDivisions = FXCollections.observableArrayList();
    @FXML
    private ObservableList<FirstLevelDivision> canadaDivisions = FXCollections.observableArrayList();
    @FXML
    private ObservableList<FirstLevelDivision> ukDivisions = FXCollections.observableArrayList();
    public CountryDAO countryDAO = new CountryDAO();
    @FXML
    public FirstLevelDivisionDAO fldDAO = new FirstLevelDivisionDAO();


    public void saveButton(ActionEvent actionEvent) throws SQLException {

        Customer customer = new Customer(-1,"ted","asdfsa","14326","544-353-3333",4,"Place");

        CustomerDAO customerDAO = new CustomerDAO();

        //Transfer filled out info to
        String name  = nameField.getText();
        String address = addressField.getText();
        String postalCode = postalCodeField.getText();
        String phone = phoneField.getText();
        int divisionId  = fldCombo.getSelectionModel().getSelectedItem().getId();
        String divisionName = fldCombo.getSelectionModel().getSelectedItem().toString();

        if (name.isEmpty() || address.isEmpty() ||
                postalCode.isEmpty() || phone.isEmpty() ||
                fldCombo.getValue() == null) {

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Please fill in all fields.");
            alert.showAndWait();
            return;
        }

        ///customer.setId(customerId);
        customer.setName(name);
        customer.setAddress(address);
        customer.setPostalCode(postalCode);
        customer.setPhone(phone);
        customer.setDivisionId(divisionId);
        customer.setDivisionName(divisionName);

        customerDAO.insert(customer);

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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ObservableList<Country> allCountries = FXCollections.observableArrayList();
        ObservableList<FirstLevelDivision> allFirstLevelDivisions = FXCollections.observableArrayList();

        CountryDAO countryDao = new CountryDAO();
        FirstLevelDivisionDAO fldDao = new FirstLevelDivisionDAO();

        try {
            usDivisions = FirstLevelDivisionDAO.usStates();
            canadaDivisions = FirstLevelDivisionDAO.canadaStates();
            ukDivisions = FirstLevelDivisionDAO.ukStates();
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
//
        try {
            fldCombo.setItems(fldDao.getList());
            fldCombo.getSelectionModel().selectFirst();

            countryCombo.setItems(countryDao.getList());
            countryCombo.getSelectionModel().selectFirst();
            //setCountryAndFldCombo();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void onCountryCombo(ActionEvent actionEvent) {
        //Get selected country
        Country country = countryCombo.getSelectionModel().getSelectedItem();

        if (country != null) {
            ObservableList<FirstLevelDivision> divisions = null;
            switch(country.getId()) {
                case 1:
                    divisions = usDivisions;
                    break;
                case 2:
                    divisions = ukDivisions;
                    break;
                case 3:
                    divisions = canadaDivisions;

                    break;
                default:
                    System.err.println("No matching Country found");
            }

            fldCombo.setItems(divisions);

        }
    }

//
//    private void setCountryAndFldCombo() throws SQLException {
//        // Find matching FLD and set the selection
//        FirstLevelDivision matchingFld = null;
//        for (FirstLevelDivision fld : fldDAO.getList()) {
//            if (fld.getId() == passedCustomer.getDivisionId()) {
//                matchingFld = fld;
//                fldCombo.getSelectionModel().select(matchingFld);
//                break;
//            }
//        }
//
//        // Find matching Country and set selection
//        if (matchingFld != null) {
//            int countryId = matchingFld.getCountryId();
//            for (Country country : countryCombo.getItems()) {
//                if (country.getId() == countryId) {
//                    countryCombo.getSelectionModel().select(country);
//                    break;
//                }
//            }
//        }
//
//        // Load matching divisions (only if a country is selected)
//        if (countryCombo.getSelectionModel().getSelectedItem() != null) {
//            loadMatchingDivisions();
//        }
//    }
//
//    private void loadMatchingDivisions() throws SQLException {
//
//        int countryId = countryCombo.getSelectionModel().getSelectedItem().getId();
//        ObservableList<FirstLevelDivision> matchingDivisions = fldDAO.getDivisionsByCountryId(countryId);
//
//        if (matchingDivisions != null) {
//            fldCombo.setItems(matchingDivisions);
//        }
//    }
    public void onFldCombo(ActionEvent actionEvent) {
    }
}
