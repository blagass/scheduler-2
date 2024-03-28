package com.brandonlagasse.scheduler2.controller;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Locale;

/**
 * Main class that starts when the application is opened
 */
public class Main extends Application {
    /**
     * This is the first instance of a scene change. This pulls up the login-view so that users can login with thier name/password.
     * @param stage this is the variable that sets the stage the selected scene
     * @throws IOException exception for IO error
     */
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/com/brandonlagasse/scheduler2/login-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 800, 600);
        //stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Main method that launches the start method, taking users to the login view.
     */
    public static void main(String[] args) throws SQLException {
        //Locale.setDefault(new Locale("en"));
        launch();
    }
}