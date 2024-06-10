package com.to_do;

import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;

import com.to_do.mainScene.S0_ToDoToday;

/**
 * JavaFX App
 */
public class ToDoStarter extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        new S0_ToDoToday(stage);
    }

    public static void main(String[] args) {
        launch();
    }
}