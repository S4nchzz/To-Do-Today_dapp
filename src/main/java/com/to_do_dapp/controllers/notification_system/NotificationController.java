package com.to_do_dapp.controllers.notification_system;

import java.io.IOException;

import com.to_do_dapp.controllers.mainAppController.MainControllerApp;

import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.util.Duration;
import javafx.scene.layout.VBox;

public class NotificationController {
    private static NotificationController instance;
    private MainControllerApp main;
    private Pane notification;

    @FXML
    private Text fxml_title;
    @FXML
    private Text fxml_message;
    @FXML
    private Text fxml_time;

    private NotificationController () {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/com/to_do_dapp/fxml/mainApp/app_notification.fxml"));
        loader.setController(this);

        this.main = MainControllerApp.getInstance();
        try {
            this.notification = loader.load();
        } catch (IOException e) {
            // ? LOG: Unnable to load FXML on pane
        }
    }

    public static NotificationController getInstance() {
        if (instance == null) {
            instance = new NotificationController();
        }

        return instance;
    }

    public void show(String title, String message, String time) {
        VBox notificationVbox = main.getNotificationVbox();
        if (notificationVbox.getChildren().size() == 0) {
            notificationVbox.getChildren().add(this.notification);
        }

        this.fxml_title.setText(title);
        this.fxml_message.setText(message);
        this.fxml_time.setText(time);

        TranslateTransition moveVboxDown = new TranslateTransition();
        moveVboxDown.setNode(notificationVbox);
        moveVboxDown.setByY(100);
        moveVboxDown.setDelay(Duration.millis(300));
        moveVboxDown.play();

        new Thread(() -> {
            try {
                Thread.sleep(2000);
                
                TranslateTransition moveVboxUp = new TranslateTransition();
                moveVboxUp.setNode(notificationVbox);
                moveVboxUp.setByY(-100);
                moveVboxUp.setDelay(Duration.millis(300));
                moveVboxUp.play();
            } catch (InterruptedException e) {
                // ? LOG: Error on Threda.sleep
            }
        }).start();
    }

    public Pane getNotification() {
        return this.notification;
    }

    public Text getFxml_title() {
        return fxml_title;
    }

    public void setFxml_title(String text) {
        this.fxml_title.setText(text);
    }

    public Text getFxml_message() {
        return fxml_message;
    }

    public void setFxml_message(String text) {
        this.fxml_message.setText(text);
    }

    public Text getFxml_time() {
        return fxml_time;
    }

    public void setFxml_time(String text) {
        this.fxml_time.setText(text);
    }
}
