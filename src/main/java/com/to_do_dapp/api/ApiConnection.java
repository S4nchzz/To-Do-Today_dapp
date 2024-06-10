package com.to_do_dapp.api;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;

import com.to_do_dapp.api.resources.req_AddUser.DataToJson;
import com.to_do_dapp.api.resources.req_AddUser.UserData;

public class ApiConnection {
    private final String apiUrl = "http://192.168.1.97:8080";
    private static ApiConnection instance = new ApiConnection();

    private ApiConnection () {        
    
    }

    public static ApiConnection getInstance() {
        return instance;
    }

    public boolean addUser(UserData userModelClient) {
        try {
            URI uri = new URI(apiUrl + "/user/addUser");
            HttpURLConnection connectionToApi = (HttpURLConnection) uri.toURL().openConnection();
            
            connectionToApi.setRequestMethod("PUT");
            connectionToApi.setDoOutput(true);
            
            OutputStream out = connectionToApi.getOutputStream();
            out.write(DataToJson.userDataToJson(userModelClient).getBytes());

            // ? LOG: Save api response
            System.out.println(ApiResponseReader.getResponse(connectionToApi.getInputStream()));
        } catch (URISyntaxException | IOException e) {
            // ? ERROR WHILE TRYING TO CONNECT TO API
            e.printStackTrace();
        }


        return true;
    }
}
