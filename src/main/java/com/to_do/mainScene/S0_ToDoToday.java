package com.to_do.mainScene;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;

import org.json.JSONObject;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class S0_ToDoToday {
    public S0_ToDoToday(Stage stage) {
        FXMLLoader toDoLoader = new FXMLLoader();
        toDoLoader.setLocation(getClass().getResource("/com/to_do/fxml/toDo_principalScene.fxml"));
        toDoLoader.setController(this);

        try {
            Parent toDoParent = toDoLoader.load();
            Scene toDoScene = new Scene(toDoParent);

            stage.setScene(toDoScene);
            stage.setTitle("To do Today");
            stage.setResizable(false);
            stage.centerOnScreen();
            stage.show();
        } catch (IOException e) {
            // ? LOG: FAILED TO LOAD TO DO MAIN SCENE
            e.printStackTrace();
        }
    }
}
