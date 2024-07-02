package com.to_do_dapp.controllers.mainAppController.toDosManagement;

import java.io.IOException;
import java.time.LocalDate;

import org.json.JSONObject;

import com.to_do_dapp.controllers.mainAppController.MainControllerApp;

import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.util.Duration;

public class ToDoEntry {
    //To Do elements
    @FXML
    private Text fxid_toDoHeader;
    @FXML
    private Text fxid_toDoContent;
    @FXML
    private Text fxid_toDoDate;
    @FXML
    private Pane fxid_timePaneColor;

    private final int yy;
    private final int mm;
    private final int dd;

    private final MainControllerApp main;
    public ToDoEntry (MainControllerApp main) {
        LocalDate date = LocalDate.now();
        this.yy = date.getYear();
        this.mm = date.getMonthValue();
        this.dd = date.getDayOfMonth();
        this.main = main;
    }

    public Pane createPane(JSONObject jsonObject) {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/com/to_do_dapp/fxml/mainApp/toDoTemplates/toDoEntryTemplate.fxml"));
        loader.setController(this);
        
        try {
            Parent pane = loader.load();
            this.fxid_toDoHeader.setText(jsonObject.getString("Header"));

            String content = jsonObject.getString("Content");

            if (content.length() > 40) {
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < 40; i++) {
                    sb.append(content.charAt(i));
                }
    
                this.fxid_toDoContent.setText(sb.toString() + "...");
            } else {
                this.fxid_toDoContent.setText(content);
            }

            ToDoDateFormat tddf = new ToDoDateFormat(jsonObject);
            this.fxid_toDoDate.setText(tddf.getFormatForToDoElement());
            
            this.fxid_timePaneColor.setStyle("-fx-background-color:" + chooseCorrectColor(tddf));

            return (Pane)pane;
        } catch (IOException e) {
            
        }
        
        return null;
    }

    private String chooseCorrectColor(ToDoDateFormat tddf) {
        if (tddf.getYy() > this.yy) {
            return "green";
        } else if (tddf.getMm() > this.mm) {
            return "orange";
        } else if (tddf.getDd() >= this.dd) {
            return "red";
        } else {
            return "black";
        }
    }

    @FXML
    private void openMenuDetails() {
        TranslateTransition toDoMenu = new TranslateTransition();
        toDoMenu.setNode(main.getFxid_toDoMenu());
        toDoMenu.setByX(-282);
        toDoMenu.setDuration(Duration.millis(500));
        toDoMenu.play();
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
}