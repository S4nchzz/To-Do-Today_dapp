package com.to_do_dapp.controllers.mainAppController;

import java.util.ArrayList;

import org.json.JSONObject;

import com.to_do_dapp.api.ApiConnection;
import com.to_do_dapp.controllers.notification_system.NotificationController;
import com.to_do_dapp.controllers.mainAppController.groupManagement.GroupData;
import com.to_do_dapp.controllers.mainAppController.groupManagement.GroupElementController;
import com.to_do_dapp.controllers.mainAppController.groupManagement.GroupElementControllerList;
import com.to_do_dapp.controllers.mainAppController.groupManagement.Member;
import com.to_do_dapp.controllers.mainAppController.groupManagement.MemberController;
import com.to_do_dapp.controllers.mainAppController.teamToDoManagement.TeamToDoController;
import com.to_do_dapp.controllers.mainAppController.toDoManagement.ToDoController;
import com.to_do_dapp.controllers.mainAppController.toDoManagement.ToDoControllerList;

import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.layout.VBox;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.util.Duration;
import java.time.LocalDateTime;

public class MainControllerApp {
    private final ApiConnection apiConnection;
    private static MainControllerApp instance;
    @FXML
    private Pane fxid_leftPane;
    @FXML   
    private ImageView fxid_menuImage;

    private boolean isEmailVerified;
    // Left option menu panes
    @FXML
    private Pane fxid_hoverPaneA;
    @FXML
    private Pane fxid_hoverPaneB;
    @FXML
    private Pane fxid_hoverPaneC;
    @FXML
    private Pane fxid_hoverPaneD;

    private boolean isTeamsBloqued;

    // Principal app panes
    @FXML
    private Pane fxid_allPanes;
    
    // Account restrictions
    @FXML
    private ImageView fxid_accountNotVerifiedWarning;
    @FXML
    private ImageView fxid_teamsBloqued;

    // Each function with his pane
    @FXML
    private Pane fxid_toDoManagementPane;
    @FXML
    private Pane fxid_teamManagementPane;
    @FXML
    private Pane fxid_teamSearchAndCreate;

    //V-Box & SctrollPane from ToDos
    @FXML
    private Text fxid_toDoListText;
    @FXML
    private ScrollPane fxid_scrollPane;
    @FXML
    private VBox fxid_toDoVbox;

    // To Do Menu Detail
    @FXML
    private Pane fxid_toDoMenu;
    @FXML
    private Text fxid_detailMenuTitleText;
    @FXML
    private CheckBox fxid_TeamToDo;

    // Left Pane elements
    @FXML
    private Text fxid_userNameField;
    private String localUsername;

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
    private boolean isOpened;
    private ToDoController currentToDoControllerBeignEdited;

    private boolean menuHidden;
    private boolean toDoCreation = false; // If the user press the add ToDo button, this value will go true

    // Team search elements
    @FXML
    private VBox fxid_groupVBox;
    @FXML
    private ScrollPane fxid_groupScrollPane;
    @FXML
    private Pane fxid_createTeamPane;

    @FXML
    private Button fxid_teamToDoButton;
    private boolean teamToDoDisplay;
    @FXML
    private TextField fxid_createTeamNameField;
    @FXML
    private TextField fxid_createTeamDescriptionField;
    @FXML
    private PasswordField fxid_createTeamPasswordField;
    @FXML
    private CheckBox fxid_createTeamPrivateCheck;
    @FXML
    private Button fxid_createTeamSendTeam;
    
    // Notification elements + mainController requirements
    private NotificationController notificationController;
    @FXML
    private VBox fxid_notificationVbox;

    // Settings Pane
    @FXML
    private Pane fxid_settingsPane;
    @FXML
    private Text fxid_emailVerificationInfo;
    @FXML
    private Button fxid_verifyButton;
    @FXML
    private Pane fxid_settingsVerifyEmailCodePlacemenetPane;
    @FXML
    private TextField fxid_codeField;
    @FXML
    private Text fxid_settingsEmail;

    // User group Pane
    private GroupData groupData;
    @FXML
    private Text fxid_groupName;
    @FXML
    private Text fxid_groupDescription;

    private GroupElementControllerList groupElementControllerList;

    ArrayList<HBox> memberListHbox;
    private int memberListPointer;
    @FXML
    private HBox fxid_membersGroup1;
    @FXML
    private HBox fxid_membersGroup2;
    @FXML
    private HBox fxid_membersGroup3;
    @FXML
    private Text fxid_restOfUsersCount;
    @FXML
    private TextField fxid_groupPasswordPlace;
    @FXML
    private Pane fxid_showPasssordMenu;
    @FXML
    private Text fxid_groupPasswordTextView;
    private boolean passRevealed;
    @FXML
    private Pane fxid_modifyOrAddPasswordPane;
    @FXML
    private Pane fxid_adminWarningLeaving;
    @FXML
    private ComboBox<String> fxid_comboBoxNextAdmin;
    @FXML
    private ImageView fxid_deleteEntireTeam;

    @FXML
    private TextField fxid_teamNameJoinPrivate;
    @FXML
    private PasswordField fxid_teamPasswordJoinPrivate;
    @FXML
    private Text fxid_joinPrivateWarn;

    @FXML
    public void initialize() {
        this.localUsername = apiConnection.getUserName();
        this.fxid_userNameField.setText(localUsername);
        preloadToDoElements();
        this.notificationController = NotificationController.getInstance();
        if (!isEmailVerified) {
            this.fxid_accountNotVerifiedWarning.setVisible(true);
            this.fxid_teamsBloqued.setVisible(true);
            blockTeams();
        }

        this.memberListHbox.add(fxid_membersGroup1);
        this.memberListHbox.add(fxid_membersGroup2);
        this.memberListHbox.add(fxid_membersGroup3);
    }

    private MainControllerApp() {
        apiConnection = ApiConnection.getInstance();
        this.menuHidden = false;
        this.isOpened = false;
        this.isTeamsBloqued = false;

        this.groupElementControllerList = GroupElementControllerList.getInstance();

        this.isEmailVerified = apiConnection.isEmailVerified();
        this.groupData = GroupData.getInstance();
        if (apiConnection.getGroupData()) {
            // ? LOG: Group Data has been saved correctly
            groupData = GroupData.getInstance();
            Platform.runLater(() -> {
                this.fxid_TeamToDo.setDisable(false);
            });
        }

        this.memberListHbox = new ArrayList<>();
        this.memberListPointer = 0;
        this.passRevealed = false;

        this.teamToDoDisplay = true;
    }

    public static MainControllerApp getInstance() {
        if (instance == null) {
            instance = new MainControllerApp();
        }

        return instance;
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
        
        if (toDoList == null) {
            return;
        }

        ToDoControllerList toDoEntryList = ToDoControllerList.getInstance();
        toDoEntryList.clearList(); // This might cause some lag if the user have a lot of to-do's

        clearVbox();
        for (int i = 0; i < toDoList.size(); i++) {
            ToDoController entry = new ToDoController(toDoList.get(i));
            toDoEntryList.addToDoAtList(entry);
            fxid_toDoVbox.getChildren().add(entry.createPane());
        }
    }

    private void enableOrDisableMenuElements(boolean disable) {
        this.fxid_detailMenuTitleText.setDisable(disable);
        this.fxid_toDoMenuHeader.setDisable(disable);
        this.fxid_toDoMenuContent.setDisable(disable);
        this.fxid_toDoMenuDate.setDisable(disable);
        this.fxid_toDoMenuTime.setDisable(disable);
    }

    public void setDetailedMenuInfo(String detailMenuText, ToDoController toDoController) {
        this.fxid_toDoMenuHeader.setText(toDoController.getHeader());
        this.fxid_toDoMenuContent.setText(toDoController.getContent());

        this.fxid_toDoMenuDate.setText(toDoController.getDate().getYymmdd());
        this.fxid_toDoMenuTime.setText(toDoController.getDate().getHhmmss());
        this.fxid_detailMenuTitleText.setText(detailMenuText);
    }

    @FXML
    private void addToDo() {
        clearCurrentDetailMenuInfo();
        this.fxid_detailMenuTitleText.setText("New To-Do");
        this.fxid_toDoMenuHeader.setPromptText("Titulo"); // * This can be changed by an IA analyzing all ToDos
        this.fxid_toDoMenuContent.setPromptText("Contenido");
        this.fxid_toDoMenuDate.setPromptText("mm-dd-yyyy");
        this.fxid_toDoMenuTime.setPromptText("ss:mm:hh");
        this.toDoCreation = true;
        
        if (!isOpened) {
            openDetailMenuAnimation();
        }
    }

    public void openDetailMenuAnimation() {
        if (isOpened) {
            return;
        }

        if (currentToDoControllerBeignEdited != null) {
            setDetailedMenuInfo("Task Manager", currentToDoControllerBeignEdited);
        }
        
        enableOrDisableMenuElements(false);

        this.fxid_sendInfoButton.setDisable(false);
        TranslateTransition toDoMenu = new TranslateTransition();
        toDoMenu.setNode(this.fxid_toDoMenu);
        toDoMenu.setByX(-282);
        toDoMenu.setDuration(Duration.millis(500));

        moveScrollPane(-100);
        toDoMenu.play();

        this.isOpened = true;
    }

    @FXML
    private void closeMenuDetails() {
        if (!isOpened) {
            return;
        }

        enableOrDisableMenuElements(false);
        clearCurrentDetailMenuInfo();

        TranslateTransition toDoMenu = new TranslateTransition();
        toDoMenu.setNode(this.fxid_toDoMenu);
        toDoMenu.setByX(282);
        toDoMenu.setDuration(Duration.millis(500));

        moveScrollPane(100);
        toDoMenu.play();

        if (toDoCreation) {
            toDoCreation = false;
        }

        this.currentToDoControllerBeignEdited = null;
        this.isOpened = false;
    }

    public void moveScrollPane(int coords) {
        TranslateTransition toDoText = new TranslateTransition();
        toDoText.setNode(this.fxid_toDoListText);
        toDoText.setByX(coords);

        TranslateTransition toDoGoDownAnimation = new TranslateTransition();
        toDoGoDownAnimation.setNode(this.fxid_scrollPane);

        toDoGoDownAnimation.setByX(coords);

        toDoText.play();
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
        if (this.fxid_TeamToDo.isSelected()) {
            // Add teamToDo
            toDo.put("header", this.fxid_toDoMenuHeader.getText());
            toDo.put("content", this.fxid_toDoMenuContent.getText());

            JSONObject dateOnJson = new JSONObject();
            dateOnJson.put("date", this.fxid_toDoMenuDate.getText());
            dateOnJson.put("time", this.fxid_toDoMenuTime.getText());

            toDo.put("date", dateOnJson.toString());
            toDo.put("fav", false);
            toDo.put("ended", false);

            apiConnection.addTeamToDo(toDo);
        } else {
            if (this.fxid_toDoMenuDate.getText().isBlank() || this.fxid_toDoMenuTime.getText().isBlank()) {
                return;
            }

            if (currentToDoControllerBeignEdited != null) {
                toDo.put("id", currentToDoControllerBeignEdited.getId());
                toDo.put("fav", currentToDoControllerBeignEdited.isFavSelected());
            } else {
                toDo.put("fav", false);
            }

            toDo.put("header", this.fxid_toDoMenuHeader.getText());
            toDo.put("content", this.fxid_toDoMenuContent.getText());

            JSONObject dateOnJson = new JSONObject();
            dateOnJson.put("date", this.fxid_toDoMenuDate.getText());
            dateOnJson.put("time", this.fxid_toDoMenuTime.getText());

            toDo.put("date", dateOnJson.toString());

            if (toDoCreation) {
                toDo.put("ended", false);
                boolean hasBeenCreated = apiConnection.addToDo(toDo);
                toDoCreation = false;

                if (!hasBeenCreated) {
                    // ? LOG: Unnable to create ToDo
                    return;
                }

                notificationController.show("To-Dos",
                        "You just created a new To-Do", "Now");

            } else {
                toDo.put("ended", currentToDoControllerBeignEdited.isCompleted());
                boolean hasBeenUpdated = apiConnection.updateToDo(toDo);

                if (!hasBeenUpdated) {
                    // ? LOG: Unnable to update ToDo
                    return;
                }

                notificationController.show("To-Dos",
                        "You just updated 1 To-Dos", "Now");
            }

            this.fxid_sendInfoButton.setDisable(true);

            clearVbox();
            preloadToDoElements();
            closeMenuDetails();
        }
    }

    @FXML
    private void swapBetweenTeamToDoAndPrivateToDos () {
        if (this.teamToDoDisplay) {
            preloadToDoElements();
        } else {
            preloadTeamToDos();
        }

        this.teamToDoDisplay = !this.teamToDoDisplay;
    }

    private void preloadTeamToDos() {
        JSONObject entries = apiConnection.getTeamToDos();
        java.util.Set<String> s = entries.keySet();

        for (String key : s) {
            this.clearVbox();
            this.fxid_toDoVbox.getChildren().add(new TeamToDoController(new JSONObject(entries.getString(key))).createPane());
        }
    }

    @FXML
    private void deleteSelectedToDos() {
        ToDoControllerList toDoList = ToDoControllerList.getInstance();
        ArrayList<Integer> toDosToBeDeletedFromList = new ArrayList<>();

        for (ToDoController toDo : toDoList.getToDoList()) {
            if (toDo.isCheckBoxSelected()) {
                apiConnection.deleteToDo(toDo.getId());

                toDosToBeDeletedFromList.add(toDo.getId());
            }
        }

        for (Integer i : toDosToBeDeletedFromList) {
            toDoList.removeToDoAtList(i);
        }

        notificationController.show("To-Dos", "You just deleted " + toDosToBeDeletedFromList.size() + " To-Dos", "Now");

        preloadToDoElements();
    }

    @FXML
    private void openToDos() {
        setVisibleMainControllerPanes(false);
        this.fxid_toDoManagementPane.setVisible(true);
    }

    @FXML
    public void openTeams() {
        if (this.isTeamsBloqued) {
            return;
        }

        setVisibleMainControllerPanes(false);
        if (apiConnection.userIsInGroup()) {
            this.fxid_teamManagementPane.setVisible(true);
            openUserGroup();
        } else {
            this.fxid_teamSearchAndCreate.setVisible(true);
            preloadTeamsPanes();
        }
    }
    
    private void openUserGroup() {
        this.fxid_adminWarningLeaving.setVisible(false);
        this.fxid_comboBoxNextAdmin.getItems().clear();
        groupData.clearMembers();
        this.fxid_modifyOrAddPasswordPane.setVisible(false);
        fxid_deleteEntireTeam.setVisible(false);
        passRevealed = true;
        
        if (groupData.getPassword() != null && !groupData.getPassword().isEmpty()) {
            this.fxid_showPasssordMenu.setVisible(true);
            revealPassword();
        } else {
            this.fxid_showPasssordMenu.setVisible(false);
        }
        memberListPointer = 0;

        apiConnection.getGroupData();   
        this.fxid_groupName.setText(groupData.getTitle());
        this.fxid_groupDescription.setText(groupData.getDescription());

        // Members
        this.fxid_membersGroup1.getChildren().clear();
        this.fxid_membersGroup2.getChildren().clear();
        this.fxid_membersGroup3.getChildren().clear();
        JSONObject membersOnJson = apiConnection.getMembersFromGroup();
        
        for (String key : membersOnJson.keySet()) {
            JSONObject user = membersOnJson.getJSONObject(key);
            
            if (memberListPointer <= 2 && memberListHbox.get(memberListPointer).getChildren().size() >= 4) {
                memberListPointer++;
            }
            
            if (memberListPointer <= 2) {
                groupData.setMember(new Member(user));
                memberListHbox.get(memberListPointer).getChildren().add(new MemberController(user).getPane());
            }

            if (membersOnJson.keySet().size() > 12) {
                this.fxid_restOfUsersCount.setText((membersOnJson.keySet().size() - 12) + " more users...");
            }
        }

        if (!groupData.getPassword().isEmpty()) {
            this.fxid_showPasssordMenu.setVisible(true);
        }

        if (apiConnection.amIAdminFromGroup()) {
            fxid_modifyOrAddPasswordPane.setVisible(true);
            fxid_deleteEntireTeam.setVisible(true);
        }
    }

    @FXML
    private void leaveGroupAction() {
        if (apiConnection.amIAdminFromGroup() && groupData.getMembers().size() > 1) {
            adminLeavingGroupWarn();
        } else if (groupData.getMembers().size() == 1) {
            apiConnection.deleteEmptyGroup();
            disableToDoTeamCheckbox();
            openTeams();
        } else {
            confirmLeaveGroup();
        }
    }

    private void confirmLeaveGroup() {
        if (apiConnection.leaveGroup()) {
            disableToDoTeamCheckbox();
            openTeams();
        } else {
            //? Log: an error occurs when trying to leave the group
        }
    }

    private void adminLeavingGroupWarn() {
        this.fxid_adminWarningLeaving.setVisible(true);
        for (Member m : groupData.getMembers()) {
            if (!m.getUsername().equals(this.localUsername)) {
                this.fxid_comboBoxNextAdmin.getItems().add(m.getUsername());  
            }
        }
    }

    @FXML
    private void setNewGroupAdmin () {
        if (!this.fxid_comboBoxNextAdmin.getSelectionModel().getSelectedItem().isEmpty() && apiConnection.updateTeamAdmin(groupData.getTeamKey(), this.fxid_comboBoxNextAdmin.getSelectionModel().getSelectedItem())) {
            confirmLeaveGroup();
        }
    }

    @FXML
    private void addOrChangePassword() {
        if (!fxid_groupPasswordPlace.getText().isEmpty()) {
            apiConnection.addOrChangePassword(groupData.getTeamKey(), this.fxid_groupPasswordPlace.getText());
            openTeams();
        }
    }

    @FXML
    private void revealPassword() {
        if (!passRevealed) {
            this.fxid_groupPasswordTextView.setText(groupData.getPassword());
            this.passRevealed = true;
        } else {
            this.fxid_groupPasswordTextView.setText("**********");
            this.passRevealed = false;
        }
    }

    private void preloadTeamsPanes() {
        updateGroupElementControllerList();
        this.fxid_groupVBox.getChildren().clear();
        for (GroupElementController groupElementController : groupElementControllerList.getGroupElementList()) {
            this.fxid_groupVBox.getChildren().add(groupElementController.getPane());
        }
    }

    @FXML
    private void createNewTeam() {
        if (this.fxid_createTeamNameField.getText().isEmpty() || this.fxid_createTeamDescriptionField.getText().isEmpty()) {
            // ! Show error message
            return;
        }

        LocalDateTime localdate = LocalDateTime.now();
        JSONObject currentDate = new JSONObject()
        .put("date", localdate.getDayOfMonth() + "/" + localdate.getMonthValue() + "/" + localdate.getYear())
        .put("time", localdate.getHour() + ":" + localdate.getMinute());

        // Group creation
        if (apiConnection.createTeam(new JSONObject().put("name", this.fxid_createTeamNameField.getText()).put("description", this.fxid_createTeamDescriptionField.getText()).put("password", this.fxid_createTeamPasswordField.getText()).put("public", !this.fxid_createTeamPrivateCheck.isSelected()).put("date", currentDate.toString()))) {
            // If the group is private join and return
            if (this.fxid_createTeamPrivateCheck.isSelected() && apiConnection.associateUserToPrivateGroup(this.fxid_createTeamNameField.getText(), this.fxid_createTeamPasswordField.getText())) {
                this.fxid_TeamToDo.setDisable(false);
                openTeams();
                return;
            }

            updateGroupElementControllerList();

            // If the group is public, find the controller and use his join function, !less api calls
            for (GroupElementController c : groupElementControllerList.getGroupElementList()) {
                if (c.getTitle().equals(this.fxid_createTeamNameField.getText())) {
                    c.writePasswordPlacementOnCreation(this.fxid_createTeamPasswordField.getText());
                    c.joinActionHandler();
                    apiConnection.getGroupData();
                    openTeams();
                    break;
                }
            }
        }
    }

    private void updateGroupElementControllerList() {
        ArrayList<GroupElementController> teamEntryList = apiConnection.getTeams();
        groupElementControllerList.clearList();
        for (GroupElementController groupElementController : teamEntryList) {
            groupElementControllerList.addGroupElement(groupElementController);
        }
    }

    @FXML
    private void deleteEntireGroup() {
        if (apiConnection.deleteEntireTeam(groupData.getTeamKey())) {
            openTeams();
        }
    }

    private void setVisibleMainControllerPanes(boolean property) {
        for (Node node : this.fxid_allPanes.getChildren()) {
            node.setVisible(property);
        }
    }

    @FXML
    private void openSettings() {
        setVisibleMainControllerPanes(false);
        this.fxid_settingsPane.setVisible(true);

        this.fxid_settingsEmail.setText("Email: ");
        this.fxid_settingsEmail.setText(this.fxid_settingsEmail.getText() + apiConnection.getEmail());

        if (apiConnection.isEmailVerified()) {
            setEmailVerificationInfo("Your email is verified!", "#19C701");
        } else {
            setEmailVerificationInfo("Your email is not verified.", "red");

            this.fxid_verifyButton.setVisible(true);
        }
    }

    private void setEmailVerificationInfo(String info, String hexColor) {
        this.fxid_emailVerificationInfo.setStyle("-fx-background-color: " + hexColor);
        this.fxid_emailVerificationInfo.setText(info);
    }

    @FXML
    private void startEmailVerification() {
        if (apiConnection.requestVerification()) {
            this.fxid_settingsVerifyEmailCodePlacemenetPane.setVisible(true);
            setEmailVerificationInfo("A code has been sent to the email.", "#19C701");
        }
    }

    @FXML
    private void sendVerificationCode() {
        if (this.fxid_codeField.getText().isEmpty() || !this.fxid_codeField.getText().matches("\\d+")) {
            setEmailVerificationInfo("Place a code number valid", "red");
            return;
        }

        if (apiConnection.sendVerificationCode(this.fxid_codeField.getText())) {
            this.fxid_verifyButton.setVisible(false);
            this.fxid_settingsVerifyEmailCodePlacemenetPane.setVisible(false);

            setEmailVerificationInfo("Email verification completed", "#19C701");
            this.fxid_accountNotVerifiedWarning.setVisible(false);
            this.fxid_teamsBloqued.setVisible(false);
            unBlockTeams();
        }
    }

    @FXML
    private void joinPrivate() {
        if (apiConnection.associateUserToPrivateGroup(this.fxid_teamNameJoinPrivate.getText(), this.fxid_teamPasswordJoinPrivate.getText())) {
            enableToDoTeamCheckBox();
            openTeams();
        } else {
            this.fxid_joinPrivateWarn.setText("Check the name or password");
        }
    }

    public void disableToDoTeamCheckbox() {
        this.fxid_TeamToDo.setDisable(true);
    }

    public void enableToDoTeamCheckBox() {
        this.fxid_TeamToDo.setDisable(false);
    }

    private void blockTeams() {
        this.isTeamsBloqued = true;
    }

    private void unBlockTeams() {
        this.isTeamsBloqued = false;
    }
    
    public ToDoController getCurrentToDoControllerBeignEdited() {
        return this.currentToDoControllerBeignEdited;
    }

    public void setCurrentToDoControllerBeignEdited(ToDoController toDoController) {
        this.currentToDoControllerBeignEdited = toDoController;
    }

    public VBox getNotificationVbox() {
        return this.fxid_notificationVbox;
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

    public boolean isOpened() {
        return this.isOpened;
    }
}
