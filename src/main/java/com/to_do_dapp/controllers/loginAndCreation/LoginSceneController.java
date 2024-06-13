package com.to_do_dapp.controllers.loginAndCreation;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.json.JSONObject;

import com.to_do_dapp.api.ApiConnection;
import com.to_do_dapp.controllers.FilesCreation;
import com.to_do_dapp.controllers.mainAppController.MainControllerApp;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
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
    @FXML
    private Text fxid_usernameLoginWarning;
    @FXML
    private Text fxid_passwordLoginWarning;
    
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
        String username = this.fxid_nameField.getText();
        String password = this.fxid_passField.getText();

        if (username.isBlank()) {
            this.fxid_usernameLoginWarning.setText("Rellena este campo");
            return;
        }

        if (password.isBlank()) {
            this.fxid_passwordLoginWarning.setText("Rellena este campo");
            return;
        }

        ApiConnection api = ApiConnection.getInstance();
        Object authUser = api.login(this.fxid_nameField.getText(), this.fxid_passField.getText());

        if (authUser instanceof String) {
            try {
                OutputStream out = new FileOutputStream(new File(FilesCreation.toDoTodayAbsolutePath + FilesCreation.authTempUserFile));
                JSONObject json = new JSONObject((String)authUser);
                
                out.write(json.getString("tempUserAuthTkn").getBytes());
                out.close();
            } catch (IOException e) {
                // ? LOG: File authUser.tkn not found at C:/User/user/appdata/Local/ToDoToday/ check absolute path
                e.printStackTrace();
            }
        
            FXMLLoader toDoMainScene = new FXMLLoader();
            toDoMainScene.setController(new MainControllerApp());
            toDoMainScene.setLocation(getClass().getResource("/com/to_do_dapp/fxml/mainApp/toDo_principalScene.fxml"));
            
            try {
                Parent mainAppParent = toDoMainScene.load();
                Scene mainAppScene = new Scene(mainAppParent);
                Stage mainAppStage = new Stage();

                this.stage.close();
                mainAppStage.setScene(mainAppScene);
                mainAppStage.centerOnScreen();
                mainAppStage.setResizable(false);
                mainAppStage.show();
                
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (authUser instanceof Boolean) {
            // ?: Show message invalid credentials   
        }
    }
}
