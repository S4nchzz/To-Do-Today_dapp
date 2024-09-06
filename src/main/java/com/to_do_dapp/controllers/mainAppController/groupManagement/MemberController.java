package com.to_do_dapp.controllers.mainAppController.groupManagement;

import java.io.IOException;

import org.json.JSONObject;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;

public class MemberController {
    private Pane paneMember;

    @FXML
    private Text fxid_usernameField;
    @FXML
    private Pane fxid_statusColorPane;
    @FXML
    private Text fxid_userType;
    @FXML
    private Circle fxid_loggedInInfo;

    public MemberController(JSONObject user) {
        FXMLLoader loader = new FXMLLoader();
        loader.setController(this);
        loader.setLocation(getClass().getResource("/com/to_do_dapp/fxml/mainApp/toDoTemplates/groupUserMember.fxml"));

        try {
            paneMember = loader.load();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

        Platform.runLater(() -> {
        if (user.getBoolean("groupAdmin")) {
            fxid_userType.setText("Admin");
            fxid_statusColorPane.setStyle("-fx-background-color: green");
        } else {
            fxid_userType.setText("User");
            fxid_statusColorPane.setStyle("-fx-background-color: blue");
        }

        if (user.getBoolean("online")) {
            fxid_loggedInInfo.setFill(Color.GREEN);
        } else {
            fxid_loggedInInfo.setFill(Color.RED);
        }
        });

        fxid_usernameField.setText(user.getString("username"));
    }

    public Pane getPane() {
        return this.paneMember;
    }
}