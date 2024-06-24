package com.to_do_dapp.controllers.mainAppController;

import java.io.IOException;

import org.json.JSONException;

import com.to_do_dapp.api.ApiConnection;

import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

public class MainControllerApp {
    @FXML
    private Pane fxid_leftPane;
    @FXML
    private ImageView fxid_menuImage;

    // Principal app panes
    @FXML
    private Pane fxid_allPanes;

    private boolean menuHidden;
    private final ApiConnection apiConnection;

    public MainControllerApp() {
        this.menuHidden = true;
        apiConnection = ApiConnection.getInstance();
    }

    @FXML
    private void showLeftMenu () {
        int imageMoveX = 150;
        int paneMoveX = 160;
        if (!menuHidden) {
            imageMoveX = 0;
            paneMoveX = -paneMoveX;
        }

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

    // private void preloadToDoElements() {
    //     // CALL API METHOD AND GET BY JSON ALL ENTRIES
    // }

    @FXML
    private void addToDo() {
        try {
            apiConnection.addToDo();
        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }
    }
}
