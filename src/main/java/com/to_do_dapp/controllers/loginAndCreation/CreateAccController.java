package com.to_do_dapp.controllers.loginAndCreation;

import java.io.IOException;

import com.to_do_dapp.api.ApiConnection;
import com.to_do_dapp.api.requests.req_AddUser.UserData;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class CreateAccController {
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
    @FXML
    private Text fxid_usernameRegWarn;
    @FXML
    private Text fxid_emailRegWarn;
    @FXML
    private Text fxid_passwordRegWarn;

    public CreateAccController(Stage stage, LoginSceneController loginController) {
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
            this.fxid_usernameRegWarn.setText("");
            this.fxid_emailRegWarn.setText("");

            final String username = fxid_nameField.getText();
            final String email = fxid_emailField.getText();
            final String password = fxid_passField.getText();

            if (CreationAccConditions.usernameSyntax(username) != "") {
                this.fxid_usernameRegWarn.setText(CreationAccConditions.usernameSyntax(username));
                return;
            }

            if (CreationAccConditions.emailSyntax(email) != "") {
                this.fxid_emailRegWarn.setText(CreationAccConditions.emailSyntax(email));
                return;
            }

            if (CreationAccConditions.paswordSyntax(password) != "") {
                this.fxid_passwordRegWarn.setText(CreationAccConditions.paswordSyntax(password));
                return;
            }


            ApiConnection api = ApiConnection.getInstance();
            api.addUser(new UserData(username, password, email));
            loginAccountLoadScene();
        }
    }
}
