package com.example.servidor;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class ServidorApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(ServidorApplication.class.getResource("servidor-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 582, 374);
        stage.setResizable(false);
        stage.setTitle("Server Manager");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}