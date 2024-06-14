package com.to_do_dapp.api;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.to_do_dapp.api.requests.req_AddUser.DataToJson;
import com.to_do_dapp.api.requests.req_AddUser.UserData;
import com.to_do_dapp.controllers.FilesCreation;

public class ApiConnection {
    protected final static String apiUrl = "http://192.168.1.98:8080";
    private static ApiConnection instance = new ApiConnection();

    private ApiConnection () {        
    
    }

    public static ApiConnection getInstance() {
        return instance;
    }

    private String getToken() {
        FileReader file;
        try {
            file = new FileReader(new File(FilesCreation.toDoTodayAbsolutePath + FilesCreation.authApiFile));
            BufferedReader reader = new BufferedReader(file);

            final String token = reader.readLine();
            reader.close();

            return token;
        } catch (IOException e) {
            // ? LOG: Token File not founded skiping...
            e.printStackTrace();
        }

        return null;
    }

    public boolean addUser(UserData userModelClient) {
        RestTemplate apiCreateAcc = new RestTemplate();
        
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.add("Authorization", "Bearer " + getToken());
     
        HttpEntity<String> httpEntity = new HttpEntity<>(DataToJson.userDataToJson(userModelClient), httpHeaders);
        Boolean response = apiCreateAcc.postForObject(apiUrl + "/user/addUser", httpEntity, Boolean.class);
        
        return response;    
    }

    public Object login(String username, String password) {
        RestTemplate apiLogin = new RestTemplate();
        
        HttpHeaders header = new HttpHeaders();
        header.add("Authorization", "Bearer " + getToken());
        header.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> httpEntity = new HttpEntity<>(DataToJson.loginJson(username, password), header);
        ResponseEntity<String> response = apiLogin.postForEntity(apiUrl + "/user/login", httpEntity, String.class);
        
        if (response.getBody().equals("false")) {
            return false;
        }

        try {
            new JSONObject(response.getBody().toString());      
        } catch (JSONException jsone) {
            // ? LOG: Bad request from server Not a JSON format
            return false;
        }

        return response.getBody();
    }
}
