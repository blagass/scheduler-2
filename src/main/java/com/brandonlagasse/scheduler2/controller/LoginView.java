package com.brandonlagasse.scheduler2.controller;

import com.brandonlagasse.scheduler2.dao.UserDAO;
import com.brandonlagasse.scheduler2.helper.Validation;
import com.brandonlagasse.scheduler2.model.User;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;

public class LoginView {
    public TextField userField;
    public TextField passwordField;

    public void onLogin(ActionEvent actionEvent) throws SQLException, IOException {

            try {
                if (userField.getText().isEmpty() || passwordField.getText().isEmpty()) {
                    Validation.showEmptyFieldsError();
                    return;
                }

                String userName = userField.getText();
                String password = passwordField.getText();


                User user = new User(-1, userName, password);
                UserDAO userDAO = new UserDAO();

                ObservableList<User> allUsers = userDAO.getList();

                boolean loginSuccess = allUsers.stream()
                        .anyMatch(u -> u.getName().equals(userName) &&
                                u.getPassword().equals(password));

                if (loginSuccess) {
                    System.out.println("Success!");
                    Parent customerScene = FXMLLoader.load(getClass().getResource("/com/brandonlagasse/scheduler2/main-view.fxml"));
                    Scene scene = new Scene(customerScene);
                    Stage window = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
                    window.setScene(scene);
                    window.show();
                } else {
                    throw new Error("Wrong credentials");
                }

            } catch (Error e) {
                showLoginErrorAlert("Login Error", e.getMessage()); // More general errors
            } catch (SQLException e) {
                System.err.println("Database error: " + e.getMessage());
            } catch (IOException e) {
                System.err.println("Error loading FXML: " + e.getMessage());
            }
    }

    private void showLoginErrorAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}