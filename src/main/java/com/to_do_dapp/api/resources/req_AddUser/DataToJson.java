package com.to_do_dapp.api.resources.req_AddUser;

import org.json.JSONObject;

public class DataToJson {
    public static String userDataToJson(UserData userData) {
        JSONObject json = new JSONObject();
        json.put("username", userData.getUsername());
        json.put("password", userData.getPass());
        json.put("email", userData.getEmail());
        return json.toString();
    }

    public static String loginJson (String username, String password) {
        JSONObject json = new JSONObject();
        json.put("username", username);
        json.put("password", password);
        return json.toString();
    }
}
