package com.to_do_dapp.controllers.mainAppController.groupManagement;

import org.json.JSONObject;

public class Member {
    private final String username;
    private final boolean admin;
    private final boolean online;

    public Member(JSONObject user) {
        this.username = user.getString("username");
        this.admin = user.getBoolean("groupAdmin");
        this.online = user.getBoolean("online");
    }

    public String getUsername() {
        return username;
    }

    public boolean isAdmin() {
        return admin;
    }

    public boolean isOnline() {
        return online;
    }
}