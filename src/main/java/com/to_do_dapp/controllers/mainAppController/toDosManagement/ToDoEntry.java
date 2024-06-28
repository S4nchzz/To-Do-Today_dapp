package com.to_do_dapp.controllers.mainAppController.toDosManagement;

import java.io.IOException;

import org.json.JSONObject;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

public class ToDoEntry {
    //To Do elements
    @FXML
    private Text fxid_toDoHeader;

    public Pane createPane(JSONObject jsonObject) {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/com/to_do_dapp/fxml/mainApp/toDoTemplates/toDoEntryTemplate.fxml"));
        loader.setController(this);
        
        try {
            Parent pane = loader.load();
            this.fxid_toDoHeader.setText(jsonObject.getString("Header"));
            return (Pane)pane;
        } catch (IOException e) {
            
        }
        
        return null;
    }
}