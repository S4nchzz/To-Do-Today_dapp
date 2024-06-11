package com.to_do_dapp.mainScene.controllers.loginAndCreation;

import java.io.IOException;

import com.to_do_dapp.api.ApiConnection;
import com.to_do_dapp.api.resources.req_AddUser.UserData;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class CreateAccSceneController {
    private final Stage stage;
    private final LoginSceneController loginSceneController;

    // Create account elements
    @FXML
    private TextField fxid_nameField;
    @FXML
    private TextField fxid_emailField;
    @FXML
    private TextField fxid_passField;
    @FXML
    private RadioButton fxid_policyAndConditions;
    @FXML
    private Button fxid_registerButton;

    public CreateAccSceneController(Stage stage, LoginSceneController loginController) {
        this.stage = stage;
        this.loginSceneController = loginController;
    }

    @FXML
    private void loginAccountLoadScene() {
        FXMLLoader createAccLoader = new FXMLLoader();
        createAccLoader.setLocation(getClass().getResource("/com/to_do_dapp/fxml/loginAndCreation/toDo_LoginScene.fxml"));
        createAccLoader.setController(loginSceneController);

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
    private void registerAction() {
        if (!fxid_policyAndConditions.isSelected()) {
            fxid_policyAndConditions.setStyle("-fx-background-color: red");
        } else {
            if (!fxid_nameField.getText().isEmpty() && !fxid_emailField.getText().isEmpty() && !fxid_passField.getText().isEmpty()) {
                final String username = fxid_nameField.getText();
                final String email = fxid_emailField.getText();
                final String password = fxid_passField.getText();
                ApiConnection api = ApiConnection.getInstance();
                api.addUser(new UserData(username, password, email));
                loginAccountLoadScene();
            }
        }
    }
}
