package com.to_do_dapp.mainScene;

import java.io.IOException;

import com.to_do_dapp.mainScene.controllers.loginAndCreation.LoginSceneController;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class S0_ToDoToday {
    public S0_ToDoToday(Stage stage) {
        FXMLLoader toDoLoader = new FXMLLoader();
        toDoLoader.setLocation(getClass().getResource("/com/to_do_dapp/fxml/loginAndCreation/toDo_LoginScene.fxml"));
        toDoLoader.setController(new LoginSceneController(stage));

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
