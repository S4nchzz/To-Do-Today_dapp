package com.to_do_dapp.controllers.mainAppController.toDoManagement;

import java.io.IOException;
import java.time.LocalDate;

import org.json.JSONObject;

import com.to_do_dapp.api.ApiConnection;
import com.to_do_dapp.controllers.mainAppController.MainControllerApp;

import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.util.Duration;

public class ToDoEntry {
    private JSONObject jsonToDoData;
    private final ApiConnection apiConnection;

    //To Do elements
    @FXML
    private Text fxid_toDoHeader;
    @FXML
    private Text fxid_toDoContent;
    @FXML
    private Text fxid_toDoDate;
    @FXML
    private Pane fxid_endedColorPane;
    @FXML
    private Text fxid_hasBeenEnded;

    private final int yy;
    private final int mm;
    private final int dd;

    private static boolean hasBeenOpened = false;

    private final MainControllerApp main;

    public ToDoEntry (MainControllerApp main, JSONObject json) {
        LocalDate date = LocalDate.now();
        this.yy = date.getYear();
        this.mm = date.getMonthValue();
        this.dd = date.getDayOfMonth();
        this.main = main;
        
        this.apiConnection = ApiConnection.getInstance();
        this.jsonToDoData = json;
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

            ToDoDateFormat tddf = new ToDoDateFormat(jsonToDoData);
            this.fxid_toDoDate.setText(tddf.getFormatForToDoElement());

            if (jsonToDoData.getBoolean("ended")) {
                this.fxid_endedColorPane.setStyle("-fx-background-color: #6bd744");
                this.fxid_hasBeenEnded.setText("Completed");
            } else {
                this.fxid_endedColorPane.setStyle("-fx-background-color: orange");
                this.fxid_hasBeenEnded.setText("In-progress");

            }

            return (Pane)pane;
        } catch (IOException e) {
            
        }
        
        return null;
    }

    @FXML
    private void openMenuDetails() {
        if (ToDoEntry.hasBeenOpened) {
            return;
        }

        ToDoCurrentEditMenuData toDoCurrentDetailedData = ToDoCurrentEditMenuData.getInstance();
        toDoCurrentDetailedData.setId(jsonToDoData.getInt("id"));
        toDoCurrentDetailedData.setHeader(jsonToDoData.getString("header"));
        toDoCurrentDetailedData.setContent(jsonToDoData.getString("content"));
        toDoCurrentDetailedData.setDate(jsonToDoData.getString("date"));
        toDoCurrentDetailedData.setFav(jsonToDoData.getBoolean("fav"));

        main.setTextAreaHeader(jsonToDoData.getString("header"));
        main.setTextAreaContent(jsonToDoData.getString("content"));
        main.setTextDate(jsonToDoData.getString("date"));

        main.setUpdateButtonDisableParam(false);

        TranslateTransition toDoMenu = new TranslateTransition();
        toDoMenu.setNode(main.getFxid_toDoMenu());
        toDoMenu.setByX(-282);
        toDoMenu.setDuration(Duration.millis(500));
        toDoMenu.play();

        ToDoEntry.hasBeenOpened = true;
    }

    @FXML
    private void completeToDo () {
        if (apiConnection.completeToDo(this)) {
            main.clearVbox();
            main.preloadToDoElements();
            return;
        }

        //? LOG: Failed to complete to do
        
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

    public boolean isFav() {
        return jsonToDoData.getBoolean("fav");
    }

    public boolean isEnded() {
        return jsonToDoData.getBoolean("ended");
    }

    public int getYy() {
        return yy;
    }

    public int getMm() {
        return mm;
    }

    public int getDd() {
        return dd;
    }

    public static boolean getHasBeenOpened() {
        return hasBeenOpened;
    }

    public static void setHasBeenOpened(boolean newStatus) {
        hasBeenOpened = newStatus;
    }
}