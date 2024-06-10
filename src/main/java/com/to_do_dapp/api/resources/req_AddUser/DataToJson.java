package com.to_do_dapp.api.resources.req_AddUser;

import org.json.JSONObject;

public class DataToJson {
    public static String userDataToJson(UserData userData) {
        JSONObject json = new JSONObject();
        json.put("username", userData.getUsername());
        json.put("password", userData.getPass());
        json.put("email", userData.getEmail());

        System.out.println(json.toString());
        return json.toString();
    }
}
