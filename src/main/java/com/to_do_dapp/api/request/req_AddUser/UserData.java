package com.to_do_dapp.api.request.req_AddUser;

public class UserData {
    private final String username;
    private final String pass;
    private final String email;

    public UserData(String username, String pass, String email) {
        this.username = username;
        this.pass = pass;
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public String getPass() {
        return pass;
    }

    public String getEmail() {
        return email;
    }
}