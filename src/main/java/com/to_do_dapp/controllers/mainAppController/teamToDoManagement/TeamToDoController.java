package com.to_do_dapp.controllers.mainAppController.teamToDoManagement;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.json.JSONObject;

import com.to_do_dapp.api.ApiConnection;
import com.to_do_dapp.controllers.mainAppController.MainControllerApp;
import com.to_do_dapp.controllers.mainAppController.toDoManagement.ToDoDateFormat;
import com.to_do_dapp.controllers.notification_system.NotificationController;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.CheckBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

public class TeamToDoController {
    private JSONObject jsonToDoData;
    private final ApiConnection apiConnection;
    private NotificationController notificationController;

    // ? ToDo values
    private final int id;
    private final String teamkey;
    private final ToDoDateFormat toDoDateFormat;
    private final boolean ended;

    // To Do elements
    @FXML
    private Text fxid_toDoHeader;
    @FXML
    private Text fxid_toDoContent;
    @FXML
    private Text fxid_teamToDoId;
    @FXML
    private ImageView fxid_favElement;
    @FXML
    private Text fxid_toDoDate;
    @FXML
    private Text fxid_toDoDueData;
    @FXML
    private Pane fxid_endedColorPane;
    @FXML
    private Text fxid_hasBeenEnded;
    @FXML
    private CheckBox fxid_checkBoxEntry;

    private boolean isFavSelected;
    private boolean completed;

    private final MainControllerApp main;

    @FXML
    private void initialize() {
        this.fxid_teamToDoId.setText(this.teamkey);
    }

    public TeamToDoController(JSONObject json) {
        this.main = MainControllerApp.getInstance();

        this.apiConnection = ApiConnection.getInstance();
        this.jsonToDoData = json;

        this.id = json.getInt("id");
        this.teamkey = json.getString("teamkey");
        this.toDoDateFormat = new ToDoDateFormat(jsonToDoData);
        this.ended = jsonToDoData.getBoolean("ended");

        this.completed = this.ended;

        this.notificationController = NotificationController.getInstance();
    }

    public Pane createPane() {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/com/to_do_dapp/fxml/mainApp/toDoTemplates/teamToDoEntryTemplate.fxml"));
        loader.setController(this);

        try {
            Parent pane = loader.load();
            this.fxid_toDoHeader.setText(jsonToDoData.getString("header"));

            String content = jsonToDoData.getString("content");

            if (content.length() > 40) {
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < 40; i++) {
                    sb.append(content.charAt(i));
                }

                this.fxid_toDoContent.setText(sb.toString() + "...");
            } else {
                this.fxid_toDoContent.setText(content);
            }

            this.fxid_toDoDueData.setText("Due: " + toDoDateFormat.getYymmdd());

            if (jsonToDoData.getBoolean("ended")) {
                this.fxid_endedColorPane.setStyle("-fx-background-color: #6bd744");
                this.fxid_hasBeenEnded.setText("Completed");
            } else {
                this.fxid_endedColorPane.setStyle("-fx-background-color: orange");
                this.fxid_hasBeenEnded.setText("In-progress");
            }

            return (Pane) pane;
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

        return null;
    }

    @FXML
    private void openMenuDetails() {
        if (main.isOpened()) {
            return;
        }
        main.setUpdateButtonDisableParam(false);
        main.openDetailMenuAnimation();
    }

    @FXML
    private void completeToDo() {
        completed = !completed;
        if (apiConnection.completeTeamToDo(completed, this.teamkey, this.id)) {

            if (completed) {
                this.fxid_endedColorPane.setStyle("-fx-background-color: #6bd744");
                this.fxid_hasBeenEnded.setText("Completed");
            } else {
                this.fxid_endedColorPane.setStyle("-fx-background-color: orange");
                this.fxid_hasBeenEnded.setText("In-progress");
            }
            return;
        }

        // ? LOG: Failed to complete to do
    }

    @FXML
    private void deleteToDoEvent() {
        if (apiConnection.deleteTeamToDo(this.teamkey, this.id)) {
            main.preloadToDoElements();
            notificationController.show("To-Dos",
                    "You just deleted a To-Do", "Now");
        }
    }

    public boolean isCheckBoxSelected() {
        return this.fxid_checkBoxEntry.isSelected();
    }

    public String getHeader() {
        return jsonToDoData.getString("header");
    }

    public String getContent() {
        return jsonToDoData.getString("content");
    }

    public ToDoDateFormat getDate() {
        return this.toDoDateFormat;
    }

    public boolean isEnded() {
        return jsonToDoData.getBoolean("ended");
    }

    public boolean isCompleted() {
        return this.completed;
    }

    public String getCompleteToDoDate() {
        return toDoDateFormat.getEntireDate();
    }
}