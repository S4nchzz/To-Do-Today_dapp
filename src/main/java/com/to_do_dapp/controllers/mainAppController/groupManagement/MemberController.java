package com.to_do_dapp.controllers.mainAppController.groupManagement;

import java.io.IOException;

import org.json.JSONObject;

import com.to_do_dapp.api.ApiConnection;
import com.to_do_dapp.controllers.mainAppController.MainControllerApp;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;

public class MemberController {
    private Pane paneMember;

    @FXML
    private Text fxid_usernameField;
    private final String localUsername;
    @FXML
    private Pane fxid_statusColorPane;
    @FXML
    private Text fxid_userType;
    @FXML
    private Circle fxid_loggedInInfo;
    @FXML
    private ImageView fxid_kickUserImageView;

    private final ApiConnection apiConnection;
    private GroupData groupData;

    public MemberController(JSONObject user) {
        FXMLLoader loader = new FXMLLoader();
        loader.setController(this);
        loader.setLocation(getClass().getResource("/com/to_do_dapp/fxml/mainApp/toDoTemplates/groupUserMember.fxml"));

        try {
            paneMember = loader.load();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

        this.apiConnection = ApiConnection.getInstance();
        this.groupData = GroupData.getInstance();

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

        this.localUsername = user.getString("username");
        fxid_usernameField.setText(localUsername);

        if (apiConnection.amIAdminFromGroup() && !localUsername.equals(apiConnection.getUserName())) {
            this.fxid_kickUserImageView.setVisible(true);
        }
    }

    public Pane getPane() {
        return this.paneMember;
    }

    @FXML
    private void kickUser() {
        if (apiConnection.kickUser(localUsername, groupData.getTeamKey())) {
            MainControllerApp.getInstance().openTeams();
        }
    }
}