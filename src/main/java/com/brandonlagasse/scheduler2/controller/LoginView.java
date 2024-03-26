package com.brandonlagasse.scheduler2.controller;

import com.brandonlagasse.scheduler2.dao.UserDAO;
import com.brandonlagasse.scheduler2.helper.LoginHelper;
import com.brandonlagasse.scheduler2.helper.Validation;
import com.brandonlagasse.scheduler2.model.User;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.ZoneId;
import java.util.Locale;
import java.util.ResourceBundle;

public class LoginView implements Initializable {
    public TextField userField;
    public TextField passwordField;
    public Label loginLabel;
    public Label userLabel;
    public Label zoneLabel;
    public Label passwordLabel;
    public Button loginButton;
    public Label yourZone;

    public void onLogin(ActionEvent actionEvent) throws SQLException, IOException {
        //LoginHelper.logLoginAttempt();
        String userName = null;
        try {
            if (userField.getText().isEmpty() || passwordField.getText().isEmpty()) {
                Validation.showEmptyFieldsError();
                return;
            }

            userName = userField.getText();
            String password = passwordField.getText();


            User user = new User(-1, userName, password);
            UserDAO userDAO = new UserDAO();

            ObservableList<User> allUsers = userDAO.getList();

            String finalUserName = userName;
            boolean loginSuccess = allUsers.stream()
                    .anyMatch(u -> u.getName().equals(finalUserName) &&
                            u.getPassword().equals(password));

            if (loginSuccess) {
                System.out.println("Success!");
                Parent customerScene = FXMLLoader.load(getClass().getResource("/com/brandonlagasse/scheduler2/main-view.fxml"));
                Scene scene = new Scene(customerScene);
                Stage window = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
                window.setScene(scene);
                window.show();

                // Log
                LoginHelper.logLoginAttempt(userName, true);

            } else {
                throw new Error("Wrong credentials");
            }

        } catch (Error e) {
            showLoginErrorAlert("Login Error", e.getMessage());

            LoginHelper.logLoginAttempt(userName, false);
        }
    }

    private void showLoginErrorAlert(String title, String message) {
        ResourceBundle rb = ResourceBundle.getBundle("/com/brandonlagasse/scheduler2/Lang", Locale.getDefault());
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(rb.getString("loginError")); // Use translated title
        alert.setHeaderText(null);
        alert.setContentText(rb.getString("wrongCredentials")); // Use translated message
        alert.showAndWait();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        String zone = String.valueOf(ZoneId.systemDefault());

        ResourceBundle rb = ResourceBundle.getBundle("/com/brandonlagasse/scheduler2/Lang", Locale.getDefault());

        if(Locale.getDefault().getLanguage().equals("fr"))
            System.out.println("hello");
        if(Locale.getDefault().getLanguage().equals("fr"))
            System.out.println(rb.getString("hello"));
        loginButton.setText(rb.getString("log"));
        userLabel.setText(rb.getString("user"));
        passwordLabel.setText(rb.getString("password"));
        loginLabel.setText(rb.getString("welcome"));
        yourZone.setText(rb.getString("yourZone"));
        zoneLabel.setText(zone);
    }
}