package com.to_do_dapp.controllers.mainAppController;

import java.io.IOException;
import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import com.to_do_dapp.api.ApiConnection;
import com.to_do_dapp.controllers.ToDoFiles;
import com.to_do_dapp.controllers.mainAppController.toDoManagement.ToDoCurrentEditMenuData;
import com.to_do_dapp.controllers.mainAppController.toDoManagement.ToDoEntry;
import com.to_do_dapp.controllers.mainAppController.toDoManagement.ToDoEntryList;

import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
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
    private TextField fxid_toDoMenuDate;
    @FXML
    private TextField fxid_toDoMenuTime;
    @FXML
    private Button fxid_sendInfoButton;

    private boolean menuHidden;
    private boolean toDoCreation = false; // If the user press the add ToDo button this value will go true
    

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
        
        ToDoEntryList toDoEntryList = ToDoEntryList.getInstance();
        toDoEntryList.clearList(); // This might cause some lag if the user have a lot of to-do's

        for (int i = 0; i < toDoList.size(); i++) {
            ToDoEntry entry = new ToDoEntry(this, toDoList.get(i));
            toDoEntryList.addToDoAtList(entry);
            fxid_toDoVbox.getChildren().add(entry.createPane());
        }
    }

    public void setDetailedMenuInfo(String detailMenuText) {
        this.fxid_toDoMenuHeader.setText(detailMenuInstance.getHeader());
        this.fxid_toDoMenuContent.setText(detailMenuInstance.getContent());

        JSONObject dateJson = new JSONObject(detailMenuInstance.getDate());

        this.fxid_toDoMenuDate.setText(dateJson.getString("date"));
        this.fxid_toDoMenuTime.setText(dateJson.getString("time"));
        this.fxid_detailMenuTitleText.setText(detailMenuText);
    }

    @FXML
    private void addToDo() {
        this.fxid_detailMenuTitleText.setText("New To-Do");
        this.fxid_toDoMenuHeader.setPromptText("Titulo"); // * This can be changed by an IA analyzing all ToDos
        this.fxid_toDoMenuContent.setPromptText("Contenido");
        this.fxid_toDoMenuDate.setPromptText("mm/dd/yy");
        this.toDoCreation = true;
        openDetailMenu();
    }

    public void openDetailMenu() {
        if (detailMenuInstance.isOpened()) {
            return;
        }

        this.fxid_sendInfoButton.setDisable(false);
        TranslateTransition toDoMenu = new TranslateTransition();
        toDoMenu.setNode(this.fxid_toDoMenu);
        toDoMenu.setByX(-282);
        toDoMenu.setDuration(Duration.millis(500));

        moveScrollPane(-100);
        toDoMenu.play();

        detailMenuInstance.setOpened(true);
    }

    @FXML
    private void closeMenuDetails() {
        if (!detailMenuInstance.isOpened()) {
            return;
        }

        clearCurrentDetailMenuInfo();

        TranslateTransition toDoMenu = new TranslateTransition();
        toDoMenu.setNode(this.fxid_toDoMenu);
        toDoMenu.setByX(282);
        toDoMenu.setDuration(Duration.millis(500));

        moveScrollPane(100);
        toDoMenu.play();

        detailMenuInstance.setOpened(false);
    }

    public void moveScrollPane(int coords) {
        TranslateTransition toDoGoDownAnimation = new TranslateTransition();
        toDoGoDownAnimation.setNode(this.fxid_scrollPane);

        toDoGoDownAnimation.setByX(coords);
        toDoGoDownAnimation.play();
    }

    public void clearCurrentDetailMenuInfo() {
        this.fxid_toDoMenuHeader.setText("");
        this.fxid_toDoMenuContent.setText("");
        this.fxid_toDoMenuDate.setText("");
        this.fxid_toDoMenuTime.setText("");
    }

    @FXML
    private void sendToDoData() {
        JSONObject toDo = new JSONObject();
        try {
            toDo.put("userToken", ToDoFiles.getTempUserToken());
        } catch (JSONException | IOException e) {
            //? LOG: UserToken not found or JSON error
        }
        
        ToDoCurrentEditMenuData toDoCurrentDetailedData = ToDoCurrentEditMenuData.getInstance();

        toDo.put("id", toDoCurrentDetailedData.getId());
        toDo.put("header", this.fxid_toDoMenuHeader.getText());
        toDo.put("content", this.fxid_toDoMenuContent.getText());

        JSONObject dateOnJson = new JSONObject();
        dateOnJson.put("date", this.fxid_toDoMenuDate.getText());
        dateOnJson.put("time", this.fxid_toDoMenuTime.getText());

        toDo.put("date", dateOnJson.toString());
        toDo.put("fav", toDoCurrentDetailedData.isFav());
        toDo.put("ended", toDoCurrentDetailedData.isEnded());

        if (toDoCreation) {
            boolean hasBeenCreated = apiConnection.addToDo(toDo);
            toDoCreation = false;

            if (!hasBeenCreated) {
                // ? LOG: Unnable to create ToDo
                return;
            }
        } else {
            boolean hasBeenUpdated = apiConnection.updateToDo(toDo);

            if (!hasBeenUpdated) {
                // ? LOG: Unnable to update ToDo
                return;
            }
        }

        this.fxid_sendInfoButton.setDisable(true);
        
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
        this.fxid_sendInfoButton.setDisable(status);
    }

    public Pane getFxid_toDoMenu() {
        return fxid_toDoMenu;
    }

    public Node getScrollPane() {
        return this.fxid_scrollPane;
    }
}
