package com.brandonlagasse.scheduler2.controller;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Locale;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/com/brandonlagasse/scheduler2/login-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 800, 600);
        //stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) throws SQLException {
        Locale.setDefault(new Locale("en"));
        launch();
    }
}