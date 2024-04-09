package com.brandonlagasse.scheduler2.controller;

import com.brandonlagasse.scheduler2.dao.AppointmentDAO;
import com.brandonlagasse.scheduler2.dao.UserDAO;
import com.brandonlagasse.scheduler2.helper.LoginHelper;
import com.brandonlagasse.scheduler2.helper.Validation;
import com.brandonlagasse.scheduler2.model.Appointment;
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
import java.sql.Time;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * This controller is associated with the first login screen users see when they open the application. This scene provides functionality to login using a username and password, as well as view text on the screen in French or English.
 */
public class LoginView implements Initializable {
    public TextField userField;
    public TextField passwordField;
    public Label loginLabel;
    public Label userLabel;
    public Label zoneLabel;
    public Label passwordLabel;
    public Button loginButton;
    public Label yourZone;
    public static int passUserId;

    /**
     * This is the main entry into the program, using username and password as authentication. This is retrieved from the UserDAO.
     * The language is set up to translate the text on screen, and on alerts, to be in French or English, depending on the users location.
     * @param actionEvent this is triggered by clicking the Login button after the User/Password fields have been filled out.
     * @throws SQLException this exception is for the UserDAO authentication checks
     * @throws IOException this is not explicitly used
     */
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

//
//            boolean loginSuccess = allUsers.stream()
//                    .anyMatch(u -> u.getName().equals(finalUserName) &&
//                            u.getPassword().equals(password));
//
            boolean loginSuccess = false;
            int userId = -1;


            for (User u : allUsers) {
                if (u.getName().equals(finalUserName) && u.getPassword().equals(password)) {
                    userId = u.getId();
                    loginSuccess = true;
                    break;
                }
            }

            if (loginSuccess) {
                //System.out.println("Success!");
                passUserId = userId;
                MainController.passedUserId = passUserId;


                AppointmentDAO appointmentDAO = new AppointmentDAO();
                ObservableList<Appointment> appointments = null;

                System.out.println("User ID is:" + passUserId); // TEST LOG - Doesn't seem to be working

                try {
                    appointments = appointmentDAO.getList();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }

                ZoneId userTimeZone = ZoneId.systemDefault();
                LocalDateTime now = LocalDateTime.now();
                LocalDateTime now15 = now.plusMinutes(15);
                boolean appFound = false;

                for (Appointment app: appointments) {
                    if (passUserId == app.getUserId()) {
                        System.out.println("Found User");

//                        LocalDateTime utcStart = app.getStart();
//                        LocalDateTime localStart = utcStart.atZone(ZoneOffset.UTC).withZoneSameInstant(userTimeZone).toLocalDateTime();

//                        System.out.println("Local Time is: " + localStart);// TEST LOG
//
//                        System.out.println("UTC start is:" + utcStart); // TEST LOG


                        if ((app.getStart().isAfter(now) || app.getStart().equals(now)) && (app.getStart().isBefore(now15) || app.getStart().equals(now15))) {
                            System.out.println("Appointment starting soon!");
                            //appointmentArea.setText("Appointment starting soon!");
                            Alert alert = new Alert(Alert.AlertType.INFORMATION);
                            alert.setTitle("Heads up!");
                            alert.setHeaderText("You have an appointment coming up!");
                            alert.setContentText("Appointment ID " + app.getId() + " Time: " + app.getStart());
                            alert.showAndWait();
                            appFound = true;
                            break;
                        }

                    }

                }

                if (!appFound){
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setContentText("No Appointments coming up");
                    alert.showAndWait();
                }


                //SCENE CHANGE
                Parent customerScene = FXMLLoader.load(getClass().getResource("/com/brandonlagasse/scheduler2/main-view.fxml"));
                Scene scene = new Scene(customerScene);
                Stage window = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
                window.setScene(scene);
                window.show();

                //log
                LoginHelper.logLoginAttempt(userName, true);





            } else {
                throw new Error("Wrong credentials");
            }

        } catch (Error e) {
            showLoginErrorAlert("Login Error", e.getMessage());
            LoginHelper.logLoginAttempt(userName, false);
        }
    }

    /**
     * This alert is specificly for showing wrong credential authentication failure
     * @param title this is the title of the error window
     * @param message this is the error message to be displayed
     */
    private void showLoginErrorAlert(String title, String message) {
        ResourceBundle rb = ResourceBundle.getBundle("/com/brandonlagasse/scheduler2/Lang", Locale.getDefault());
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(rb.getString("loginError"));
        alert.setHeaderText(null);
        alert.setContentText(rb.getString("wrongCredentials"));
        alert.showAndWait();
    }

    /**
     * The initialize here retrieves a zone and associated bundle, and the appropriate labels, buttons, etc., to use the Lang resource bundle for French or English translation
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        String zone = String.valueOf(ZoneId.systemDefault());

        ResourceBundle rb = ResourceBundle.getBundle("/com/brandonlagasse/scheduler2/Lang", Locale.getDefault());

        if(Locale.getDefault().getLanguage().equals("fr"))

        if(Locale.getDefault().getLanguage().equals("fr"))

        loginButton.setText(rb.getString("log"));
        userLabel.setText(rb.getString("user"));
        passwordLabel.setText(rb.getString("password"));
        loginLabel.setText(rb.getString("welcome"));
        yourZone.setText(rb.getString("yourZone"));
        zoneLabel.setText(zone);

    }
}