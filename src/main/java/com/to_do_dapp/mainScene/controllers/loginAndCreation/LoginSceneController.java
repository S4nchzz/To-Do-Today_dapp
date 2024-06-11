package com.to_do_dapp.mainScene.controllers.loginAndCreation;

import java.io.IOException;

import com.to_do_dapp.api.ApiConnection;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class LoginSceneController {
    private final Stage stage;

    // Login elements
    @FXML
    private TextField fxid_nameField;
    @FXML
    private TextField fxid_passField;
    @FXML
    private Button fxid_loginButton;
    
    public LoginSceneController(Stage stage) {
        this.stage = stage;
    }

    @FXML
    private void createAccountLoadScene() {
        FXMLLoader createAccLoader = new FXMLLoader();
        createAccLoader.setLocation(getClass().getResource("/com/to_do_dapp/fxml/loginAndCreation/toDo_AccountCreation.fxml"));
        createAccLoader.setController(new CreateAccSceneController(stage, this));

        Parent createAccParent;
        try {
            createAccParent = createAccLoader.load();
            Scene createAccScene = new Scene(createAccParent);
            
            stage.setScene(createAccScene);
            stage.setResizable(false);
            stage.centerOnScreen();
            stage.setTitle("To do Today <3");
            stage.show();
        } catch (IOException e) {
            // ? LOG: ERROR LOADING TODO_ACCOUND_CºREATION.FXML
            e.printStackTrace();
        }
    }

    @FXML
    private void loginAction() {
        ApiConnection api = ApiConnection.getInstance();
        api.login(this.fxid_nameField.getText(), this.fxid_passField.getText());
    }
}
