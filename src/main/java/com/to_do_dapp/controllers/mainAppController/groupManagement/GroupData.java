package com.to_do_dapp.controllers.mainAppController.groupManagement;

public class GroupData {
    private static GroupData groupData;
    
    private String teamKey;
    private String title;
    private String description;
    private int administrator;
    private boolean publicgroup;
    private String password;
    private String date;

    private GroupData () {
    }

    public static GroupData getInstance() {
        if (groupData == null) {
            groupData = new GroupData();
        }

        return groupData;
    }

    public String getTeamKey() {
        return teamKey;
    }

    public void setTeamKey(String teamKey) {
        this.teamKey = teamKey;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getAdministrator() {
        return administrator;
    }

    public void setAdministrator(int administrator) {
        this.administrator = administrator;
    }

    public boolean isPublicgroup() {
        return publicgroup;
    }

    public void setPublicgroup(boolean publicgroup) {
        this.publicgroup = publicgroup;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
