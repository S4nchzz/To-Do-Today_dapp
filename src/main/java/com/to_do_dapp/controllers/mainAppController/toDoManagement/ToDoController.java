package com.to_do_dapp.controllers.mainAppController.toDoManagement;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDate;

import org.json.JSONObject;

import com.to_do_dapp.api.ApiConnection;
import com.to_do_dapp.controllers.mainAppController.MainControllerApp;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.CheckBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

public class ToDoController {
    private JSONObject jsonToDoData;
    private final ApiConnection apiConnection;
    private ToDoCurrentEditedData detailMenuInstance;
    private ToDoCurrentEditedData toDoCurrentDetailedData;

    // ? ToDo values
    private final int id;
    private final int userId;
    private final String header;
    private final String content;
    private final ToDoDateFormat toDoDateFormat;
    private final boolean fav;
    private final boolean ended;

    //To Do elements
    @FXML
    private Text fxid_toDoHeader;
    @FXML
    private Text fxid_toDoContent;
    @FXML
    private ImageView fxid_favElement;
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

    private boolean isFavSelected;
    private boolean completed;

    private final int systemYy;
    private final int systemMm;
    private final int systemDd;

    private final MainControllerApp main;

    public ToDoController (MainControllerApp main, JSONObject json) {
        LocalDate localDate = LocalDate.now();
        this.systemYy = localDate.getYear();
        this.systemMm = localDate.getMonthValue();
        this.systemDd = localDate.getDayOfMonth();
        this.main = main;
        
        this.apiConnection = ApiConnection.getInstance();
        this.jsonToDoData = json;

        this.id = jsonToDoData.getInt("id");
        this.userId = jsonToDoData.getInt("userId");
        this.header = jsonToDoData.getString("header");
        this.content = jsonToDoData.getString("content");
        this.toDoDateFormat = new ToDoDateFormat(jsonToDoData);
        this.fav = jsonToDoData.getBoolean("fav");
        this.ended = jsonToDoData.getBoolean("ended");

        this.detailMenuInstance = ToDoCurrentEditedData.getInstance();
        this.toDoCurrentDetailedData = ToDoCurrentEditedData.getInstance();

        this.isFavSelected = this.fav;
        this.completed = this.ended;
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

            this.fxid_toDoDueData.setText("Due: " + toDoDateFormat.getYymmdd());

            if (jsonToDoData.getBoolean("ended")) {
                this.fxid_endedColorPane.setStyle("-fx-background-color: #6bd744");
                this.fxid_hasBeenEnded.setText("Completed");
            } else {
                this.fxid_endedColorPane.setStyle("-fx-background-color: orange");
                this.fxid_hasBeenEnded.setText("In-progress");
            }

            if (this.fav) {
                // ! This might cause problems on a diferent enviroment
                this.fxid_favElement.setImage(new Image(new FileInputStream(
                    new File("C:/Users/" + System.getProperty("user.name") + "/OneDrive/Informática/Programacion/Visual Studio/Proyects/Java/To_Do_Today/todo_today_dapp/src/main/resources/com/to_do_dapp/src/pictures/bright_star.png"))));
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

        main.setDetailedMenuInfo("Task Manager", false);
        main.openDetailMenu();
    }

    @FXML
    private void completeToDo() {
        completed = !completed;
        if (apiConnection.completeToDo(this, completed)) {
            main.clearVbox();
            main.preloadToDoElements();

            if (toDoCurrentDetailedData.getId() == this.id) {
                toDoCurrentDetailedData.setEnded(completed);
            }
            return;
        }

        //? LOG: Failed to complete to do
    }

    @FXML
    private void addToFavFunction() {
        isFavSelected = !isFavSelected;
        if (apiConnection.setFav(this, isFavSelected) && isFavSelected) {
            try {
                this.fxid_favElement.setImage(new Image(new FileInputStream(
                        new File("C:/Users/" + System.getProperty("user.name") + "/OneDrive/Informática/Programacion/Visual Studio/Proyects/Java/To_Do_Today/todo_today_dapp/src/main/resources/com/to_do_dapp/src/pictures/bright_star.png"))));
            } catch (FileNotFoundException e) {
                // ? LOG: Image not found
            }
        } else {
            try {
                this.fxid_favElement.setImage(new Image(new FileInputStream(
                        new File("C:/Users/" + System.getProperty("user.name") + "/OneDrive/Informática/Programacion/Visual Studio/Proyects/Java/To_Do_Today/todo_today_dapp/src/main/resources/com/to_do_dapp/src/pictures/star.png"))));
            } catch (FileNotFoundException e) {
                // ? LOG: Image not found
            }
        }   
    }

    @FXML
    private void deleteToDoEvent() {
        if (apiConnection.deleteToDo(this.id)) {
            main.preloadToDoElements();
        }
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