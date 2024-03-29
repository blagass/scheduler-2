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

/**
 * THis class provides a UI for the user to take a previously selected customer, and update their information before it's pushed to the database and the user returns the Customer View.
 */
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
    private ObservableList<FirstLevelDivision> usDivisions = FXCollections.observableArrayList();
    @FXML
    private ObservableList<FirstLevelDivision> canadaDivisions = FXCollections.observableArrayList();
    @FXML
    private ObservableList<FirstLevelDivision> ukDivisions = FXCollections.observableArrayList();

    /**
     * This intialize sets up the text fields and combo boxes with the previously selected customers information
     */
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
        }catch (SQLException e){
            throw new RuntimeException(e);
        }

        try {
            fldCombo.setItems(fldDAO.getList());
            countryCombo.setItems(countryDAO.getList());
            setCountryAndFldCombo();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     *This method helps match the FLD with the country
     * @throws SQLException This exception is for errors that occur while retrieving all FirstLevelDivsions
     */
    private void setCountryAndFldCombo() throws SQLException {

        FirstLevelDivision matchingFld = null;
        for (FirstLevelDivision fld : fldDAO.getList()) {
            if (fld.getId() == passedCustomer.getDivisionId()) {
                matchingFld = fld;
                fldCombo.getSelectionModel().select(matchingFld);
                break;
            }
        }

        // find country
        if (matchingFld != null) {
            int countryId = matchingFld.getCountryId();
            for (Country country : countryCombo.getItems()) {
                if (country.getId() == countryId) {
                    countryCombo.getSelectionModel().select(country);
                    break;
                }
            }
        }


        if (countryCombo.getSelectionModel().getSelectedItem() != null) {
            loadMatchingDivisions();
        }
    }

    /**
     * This is the helper to seCountryAndFldCombo that sets the combo boxes for the divisions based on the matching countryh
     * @throws SQLException Required to catch errors when retrieving the FLD divisions by countryId.
     */
    private void loadMatchingDivisions() throws SQLException {

        int countryId = countryCombo.getSelectionModel().getSelectedItem().getId();
        ObservableList<FirstLevelDivision> matchingDivisions = fldDAO.getDivisionsByCountryId(countryId);

        if (matchingDivisions != null) {
            fldCombo.setItems(matchingDivisions);
        }
    }

//
//    @Override
//    public void initialize(URL url, ResourceBundle resourceBundle) {
//        passedCustomer = CustomerView.getCustomerToPass();
//
//
//        customerIdField.setText(String.valueOf(passedCustomer.getId()));
//        nameField.setText(passedCustomer.getName());
//        addressField.setText(passedCustomer.getAddress());
//        postalCodeField.setText(passedCustomer.getPostalCode());
//        phoneField.setText(passedCustomer.getPhone());
//
//        try {
//            usDivisions = FirstLevelDivisionDAO.usStates();
//            canadaDivisions = FirstLevelDivisionDAO.canadaStates();
//            ukDivisions = FirstLevelDivisionDAO.ukStates();
//
//
//
//            countryCombo.setItems(countryDAO.getList());
//            fldCombo.setItems(fldDAO.getList());
//
//            setCountryAndFldCombo();
//
//
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        }
//
//    }
//
//
//    private void setCountryAndFldCombo() throws SQLException {
//
//        // Find the matching fld for the customer
//        FirstLevelDivision matchingFld = null;
//        for (FirstLevelDivision fld : fldDAO.getList()) {
//            if (fld.getId() == passedCustomer.getDivisionId()) {
//                matchingFld = fld;
//                break;
//            }
//        }
//
//        // Set the FLD and the Country
//        if (matchingFld != null) {
//
//            fldCombo.getSelectionModel().select(matchingFld);
//
//            int countryId = matchingFld.getCountryId();
//            for (Country country : countryCombo.getItems()) {
//                if (country.getId() == countryId) {
//                    countryCombo.getSelectionModel().select(country);
//                    break;
//                }
//            }
//        }
//    }
//

    /**
     * onCancel navigates the user back to the previous screen, and does not execute any further code
     * @param actionEvent This is triggered from the Cancel button
     */
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

    /**
     * onSave takes all the date from the text fields and country/fld combo boxes and adds them to a new customer to add to the database
     * @param actionEvent This is triggered from the Save button
     * @throws SQLException This will catch errors trying to add the customer to the database using the CustomerDAO.
     */
    @FXML
    public void onSave(ActionEvent actionEvent) throws SQLException {
        Customer customer = new Customer(-1,"ted","asdfsa","14326","544-353-3333",4,"Place");
        CustomerDAO customerDAO = new CustomerDAO();

        int customerId = Integer.parseInt(customerIdField.getText());
        String customerName = nameField.getText();
        String customerAddress = addressField.getText();
        String customerPostal = postalCodeField.getText();
        String customerPhone = phoneField.getText();
        int customerDivisionId  = fldCombo.getSelectionModel().getSelectedItem().getId();
        String customerDivisionName = fldCombo.getSelectionModel().getSelectedItem().toString();

        if (customerName.isEmpty() || customerAddress.isEmpty() ||
                customerPostal.isEmpty() || customerPhone.isEmpty() ||
                fldCombo.getValue() == null) {

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Please fill in all fields.");
            alert.showAndWait();
            return;
        }

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


//    public void customerPass(Customer customer){
//
//    }

    /**
     * This method is required for filtering FIrstLevelDivisons by the slected Country.
     * @param actionEvent Triggered by selecting a Country, which then retrieve the correct FLD and set it in the fldCombo.
     * @throws SQLException This will catch errors trying to retrieve fld's from the database
     */
    public void onCountryCombo(ActionEvent actionEvent) throws SQLException {
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

    public void onFldCombo(ActionEvent actionEvent) {
    }


    public static Customer getPassedCustomer() {
        return passedCustomer;
    }

    /**
     * Method to retrieve the passed customer from the customer tableview in the previous screen
     * @param passedCustomer This is the parameter that is set once the customer is retrieved
     */
    public static void setPassedCustomer(Customer passedCustomer) {
        CustomerUpdateView.passedCustomer = passedCustomer;
    }
}
