package com.to_do_dapp.controllers.mainAppController;

import java.io.IOException;
import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import com.to_do_dapp.api.ApiConnection;
import com.to_do_dapp.controllers.mainAppController.toDosManagement.ToDoEntry;

import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.util.Duration;

public class MainControllerApp {
    @FXML
    private Pane fxid_leftPane;
    @FXML
    private ImageView fxid_menuImage;

    // Principal app panes
    @FXML
    private Pane fxid_allPanes;

    //V-Box ToDos
    @FXML
    private AnchorPane fxid_toDoAnchorPane;
    @FXML
    private VBox fxid_toDoVbox;

    // To Do Menu Detail
    @FXML
    private Pane fxid_toDoMenu;

    // Left Pane elements
    @FXML
    private Text fxid_userNameField;

    //Right menu swipeable entry
    @FXML
    private TextField fxid_toDoMenuHeader;
    @FXML
    private TextArea fxid_toDoMenuContent;
    @FXML
    private Text fxid_toDoMenuDate;

    private boolean menuHidden;
    private final ApiConnection apiConnection;

    public MainControllerApp() {
        this.menuHidden = false;
        apiConnection = ApiConnection.getInstance();
        Platform.runLater(() -> {
            preloadToDoElements();
            this.fxid_userNameField.setText(apiConnection.getUserName());
        });
    }

    @FXML
    private void showLeftMenu () {
        int imageMoveX = !menuHidden ? -160 : 0;

        int paneMoveX = !menuHidden ? paneMoveX = -160 : 0;

        TranslateTransition leftAnimationPane = new TranslateTransition();
        leftAnimationPane.setNode(fxid_leftPane);
        leftAnimationPane.setToX(paneMoveX);
        leftAnimationPane.setDuration(Duration.millis(200));
        leftAnimationPane.play();

        TranslateTransition leftAnimationImage = new TranslateTransition();
        leftAnimationImage.setNode(fxid_menuImage);
        leftAnimationImage.setToX(imageMoveX);
        leftAnimationImage.setDuration(Duration.millis(200));
        
        leftAnimationImage.play();
        menuHidden = !menuHidden;
    }

    private void preloadToDoElements() {
        JSONObject toDoJson = apiConnection.getToDoS();
        java.util.Iterator<String> iterator = toDoJson.keys();

        ArrayList<JSONObject> toDoList = new ArrayList<>();
        while (iterator.hasNext()) {
            String i = iterator.next();
            toDoList.add(toDoJson.getJSONObject(i));
        }
        
        for (int i = 0; i < toDoList.size(); i++) {
            ToDoEntry entry = new ToDoEntry(this);
            fxid_toDoVbox.getChildren().add(entry.createPane(toDoList.get(i)));
        }
    }

    @FXML
    private void addToDo() {
        try {
            apiConnection.addToDo();
            this.fxid_toDoVbox.getChildren().clear();
            preloadToDoElements();
        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void closeMenuDetails() {
        if (!ToDoEntry.getHasBeenOpened()) {
            return;
        }

        TranslateTransition toDoMenu = new TranslateTransition();
        toDoMenu.setNode(this.fxid_toDoMenu);
        toDoMenu.setByX(282);
        toDoMenu.setDuration(Duration.millis(500));
        toDoMenu.play();

        ToDoEntry.setHasBeenOpened(false);
    }

    public void setTextAreaHeader(String text) {
        this.fxid_toDoMenuHeader.setText(text);
    }

    public void setTextAreaContent(String text) {
        this.fxid_toDoMenuContent.setText(text);
    }

    public void setTextDate(String text) {
        this.fxid_toDoMenuDate.setText(text);
    }

    public Pane getFxid_toDoMenu() {
        return fxid_toDoMenu;
    }
}
