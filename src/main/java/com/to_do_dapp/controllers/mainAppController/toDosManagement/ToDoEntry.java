package com.to_do_dapp.controllers.mainAppController.toDosManagement;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Date;

import org.json.JSONObject;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

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
    
    public ToDoEntry () {
        LocalDate date = LocalDate.now();
        this.yy = date.getYear();
        this.mm = date.getMonthValue();
        this.dd = date.getDayOfMonth();
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
}