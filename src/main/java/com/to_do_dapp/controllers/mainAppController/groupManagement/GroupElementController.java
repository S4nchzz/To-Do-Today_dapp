package com.to_do_dapp.controllers.mainAppController.groupManagement;

import java.io.IOException;

import org.json.JSONObject;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

public class GroupElementController {
    private Pane groupPane;
    // Pane elements
    @FXML
    private Text fxid_title;
    @FXML
    private Text fxid_description;
    @FXML
    private Text fxid_creationDate;
    @FXML
    private Text fxid_members;

    private String teamkey;
    private String title;
    private String description;
    private int administrator;
    private boolean publicgroup;
    private String password;
    private String date;


    public GroupElementController(JSONObject group) {
        this.teamkey = group.getString("teamkey");
        this.title = group.getString("title");
        this.description = group.getString("description");
        this.administrator = group.getInt("administrator");
        this.publicgroup = group.getBoolean("publicgroup");
        this.password = group.getString("password");
        this.date = group.getString("date");

        Platform.runLater(() -> {
            this.fxid_title.setText(this.title);
            this.fxid_description.setText(this.description);
            this.fxid_creationDate.setText(this.date);
            this.fxid_members.setText("Not defined!!!");
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
