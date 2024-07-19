package com.to_do_dapp.controllers.mainAppController;

import java.io.IOException;
import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import com.to_do_dapp.api.ApiConnection;
import com.to_do_dapp.controllers.ToDoFiles;
import com.to_do_dapp.controllers.mainAppController.toDoManagement.ToDoCurrentEditMenuData;
import com.to_do_dapp.controllers.mainAppController.toDoManagement.ToDoEntry;

import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.util.Duration;

public class MainControllerApp {
    private final ApiConnection apiConnection;
    private ToDoCurrentEditMenuData detailMenuInstance;
    
    @FXML
    private Pane fxid_leftPane;
    @FXML   
    private ImageView fxid_menuImage;

    // Principal app panes
    @FXML
    private Pane fxid_allPanes;

    //V-Box & SctrollPane from ToDos
    @FXML
    private ScrollPane fxid_scrollPane;
    @FXML
    private VBox fxid_toDoVbox;

    // To Do Menu Detail
    @FXML
    private Pane fxid_toDoMenu;
    @FXML
    private Text fxid_detailMenuTitleText;

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
    @FXML
    private Button fxid_updateButton;

    private boolean menuHidden;


    public MainControllerApp() {
        this.menuHidden = false;
        apiConnection = ApiConnection.getInstance();
        Platform.runLater(() -> {
            preloadToDoElements();
            this.fxid_userNameField.setText(apiConnection.getUserName());
        });

        this.detailMenuInstance = ToDoCurrentEditMenuData.getInstance();
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

    public void preloadToDoElements() {
        ArrayList<JSONObject> toDoList = apiConnection.getToDoS();
        
        for (int i = 0; i < toDoList.size(); i++) {
            ToDoEntry entry = new ToDoEntry(this, toDoList.get(i));
            fxid_toDoVbox.getChildren().add(entry.createPane());
        }
    }

    @FXML
    private void addToDo() {
        try {
            apiConnection.addToDo();
            clearVbox();
            preloadToDoElements();
        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void closeMenuDetails() {
        if (!detailMenuInstance.isOpened()) {
            return;
        }

        TranslateTransition toDoMenu = new TranslateTransition();
        toDoMenu.setNode(this.fxid_toDoMenu);
        toDoMenu.setByX(282);
        toDoMenu.setDuration(Duration.millis(500));

        moveScrollPaneRight();
        toDoMenu.play();

        detailMenuInstance.setOpened(false);
    }

    private void moveScrollPaneRight() {
        TranslateTransition toDoGoDownAnimation = new TranslateTransition();
        toDoGoDownAnimation.setNode(this.fxid_scrollPane);

        toDoGoDownAnimation.setByX(100);
        toDoGoDownAnimation.play();
    }

    @FXML
    private void updateToDoData() {
        JSONObject updatedData = new JSONObject();
        try {
            updatedData.put("userToken", ToDoFiles.getTempUserToken());
        } catch (JSONException | IOException e) {
            //? LOG: UserToken not found or JSON error
        }

        ToDoCurrentEditMenuData toDoCurrentDetailedData = ToDoCurrentEditMenuData.getInstance();

        updatedData.put("id", toDoCurrentDetailedData.getId());
        updatedData.put("header", this.fxid_toDoMenuHeader.getText());
        updatedData.put("content", this.fxid_toDoMenuContent.getText());
        updatedData.put("date", this.fxid_toDoMenuDate.getText());
        updatedData.put("fav", toDoCurrentDetailedData.isFav());

        boolean hasBeenUpdated = apiConnection.updateToDo(updatedData);
        
        if (!hasBeenUpdated) {
            // ? LOG: Unnable to update ToDo
            return;
        }

        this.fxid_updateButton.setDisable(true);
        
        clearVbox();
        preloadToDoElements();
        closeMenuDetails();

    }

    public void setDetailMenuTitle(String title) {
        this.fxid_detailMenuTitleText.setText(title);
    }

    public void clearVbox() {
        this.fxid_toDoVbox.getChildren().clear();
    }

    public void setUpdateButtonDisableParam(boolean status) {
        this.fxid_updateButton.setDisable(status);
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

    public Node getScrollPane() {
        return this.fxid_scrollPane;
    }
}
