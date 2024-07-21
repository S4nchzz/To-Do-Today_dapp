package com.to_do_dapp.controllers.mainAppController.toDoManagement;

import java.io.IOException;
import java.time.LocalDate;

import org.json.JSONObject;

import com.to_do_dapp.api.ApiConnection;
import com.to_do_dapp.controllers.mainAppController.MainControllerApp;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

public class ToDoEntry {
    private JSONObject jsonToDoData;
    private final ApiConnection apiConnection;
    private ToDoCurrentEditMenuData detailMenuInstance;
    private ToDoDateFormat toDoDateFormat;
    private ToDoCurrentEditMenuData toDoCurrentDetailedData;

    //To Do elements
    @FXML
    private Text fxid_toDoHeader;
    @FXML
    private Text fxid_toDoContent;
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

    private final int systemYy;
    private final int systemMm;
    private final int systemDd;

    private final MainControllerApp main;

    public ToDoEntry (MainControllerApp main, JSONObject json) {
        LocalDate date = LocalDate.now();
        this.systemYy = date.getYear();
        this.systemMm = date.getMonthValue();
        this.systemDd = date.getDayOfMonth();
        this.main = main;
        
        this.apiConnection = ApiConnection.getInstance();
        this.jsonToDoData = json;

        this.detailMenuInstance = ToDoCurrentEditMenuData.getInstance();
        this.toDoCurrentDetailedData = ToDoCurrentEditMenuData.getInstance();
    }

    public Pane createPane() {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/com/to_do_dapp/fxml/mainApp/toDoTemplates/toDoEntryTemplate.fxml"));
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

            this.toDoDateFormat = new ToDoDateFormat(jsonToDoData);
            this.fxid_toDoDueData.setText("Due: " + toDoDateFormat.getYymmdd());

            if (jsonToDoData.getBoolean("ended")) {
                this.fxid_endedColorPane.setStyle("-fx-background-color: #6bd744");
                this.fxid_hasBeenEnded.setText("Completed");
            } else {
                this.fxid_endedColorPane.setStyle("-fx-background-color: orange");
                this.fxid_hasBeenEnded.setText("In-progress");

            }

            return (Pane)pane;
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        
        return null;
    }

    @FXML
    private void openMenuDetails() {
        if (detailMenuInstance.isOpened()) {
            return;
        }

        toDoCurrentDetailedData.setId(jsonToDoData.getInt("id"));
        toDoCurrentDetailedData.setHeader(jsonToDoData.getString("header"));
        toDoCurrentDetailedData.setContent(jsonToDoData.getString("content"));
        toDoCurrentDetailedData.setDate(jsonToDoData.getString("date"));
        toDoCurrentDetailedData.setFav(jsonToDoData.getBoolean("fav"));
        toDoCurrentDetailedData.setEnded(jsonToDoData.getBoolean("ended"));
        
        main.setUpdateButtonDisableParam(false);

        main.setDetailedMenuInfo("Task Manager");
        main.openDetailMenu();
    }

    @FXML
    private void completeToDo() {
        if (apiConnection.completeToDo(this)) {
            toDoCurrentDetailedData.setEnded(true);
            main.clearVbox();
            main.preloadToDoElements();
            return;
        }

        //? LOG: Failed to complete to do
    }

    @FXML
    private void checkBoxHandler() {
        
    }

    public boolean isCheckBoxSelected() {
        return this.fxid_checkBoxEntry.isSelected();
    }

    public int getId() {
        return jsonToDoData.getInt("id");
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

    public boolean isFav() {
        return jsonToDoData.getBoolean("fav");
    }

    public boolean isEnded() {
        return jsonToDoData.getBoolean("ended");
    }

    public String getCompleteToDoDate() {
        return toDoDateFormat.getEntireDate();
    }

    public int getYy() {
        return systemYy;
    }

    public int getMm() {
        return systemMm;
    }

    public int getDd() {
        return systemDd;
    }
}