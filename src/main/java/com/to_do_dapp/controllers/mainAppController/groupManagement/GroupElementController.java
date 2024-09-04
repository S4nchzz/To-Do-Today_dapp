package com.to_do_dapp.controllers.mainAppController.groupManagement;

import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;

import com.to_do_dapp.api.ApiConnection;
import com.to_do_dapp.controllers.mainAppController.MainControllerApp;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

public class GroupElementController {
    private final MainControllerApp mainControllerApp;
    private Pane groupPane;
    // Pane elements
    @FXML
    private Text fxid_title;
    @FXML
    private Button fxid_joinButton;
    @FXML
    private TextField fxid_passwordPlacement;
    @FXML
    private Text fxid_description;
    @FXML
    private Text fxid_creationDate;
    @FXML
    private Text fxid_members;

    private final ApiConnection apiConnection;
    private String teamkey;
    private String title;
    private String description;
    private int administrator;
    private boolean publicgroup;
    private String password;
    private String date;
    private int members;

    public GroupElementController(JSONObject group) {
        apiConnection = ApiConnection.getInstance();

        this.teamkey = group.getString("teamkey");
        this.title = group.getString("title");
        this.description = group.getString("description");
        this.administrator = group.getInt("administrator");
        this.publicgroup = group.getBoolean("publicgroup");
        this.password = group.getString("password");
        this.date = group.getString("date");
        this.members = group.getInt("nMembers");

        try {
            this.password = group.getString("password");
        } catch (JSONException jsone) {
            this.password = "";
            //? LOG : The group doesnt have password
        }

        mainControllerApp = MainControllerApp.getInstance();

        Platform.runLater(() -> {
            this.fxid_title.setText(this.title);
            this.fxid_description.setText(this.description);
            this.fxid_creationDate.setText("Created on " + this.date);
            this.fxid_members.setText(members + "/4");

            if (!password.isEmpty()) {
                this.fxid_passwordPlacement.setVisible(true);
            }
        });

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/com/to_do_dapp/fxml/mainApp/toDoTemplates/groupViewEntries.fxml"));
        loader.setController(this);

        try {
            this.groupPane = loader.load();
        } catch (IOException e) {
            //? LOG: Failed to load pane
        }
    }

    @FXML
    private void joinActionHandler() { 
        if (this.fxid_passwordPlacement.getText().equals(this.password  ) && apiConnection.associateUserToGroup(this)) {
            mainControllerApp.openTeams();
        }
    }

    @FXML
    private void sendPassword (KeyEvent key) {
        if (key.getCode() == KeyCode.ENTER && this.fxid_passwordPlacement.getText().equals(this.password) && apiConnection.associateUserToGroup(this)) {
            mainControllerApp.openTeams();
        }
    }
    
    public Pane getPane() {
        return this.groupPane;
    }

    public String getTeamkey() {
        return teamkey;
    }

    public void setTeamkey(String teamkey) {
        this.teamkey = teamkey;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getAdministrator() {
        return administrator;
    }

    public void setAdministrator(int administrator) {
        this.administrator = administrator;
    }

    public boolean isPublicgroup() {
        return publicgroup;
    }

    public void setPublicgroup(boolean publicgroup) {
        this.publicgroup = publicgroup;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
