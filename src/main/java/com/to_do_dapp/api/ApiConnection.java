package com.to_do_dapp.api;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;

import com.to_do_dapp.api.request.ApiResponseReader;
import com.to_do_dapp.api.request.req_AddUser.DataToJson;
import com.to_do_dapp.api.request.req_AddUser.UserData;

public class ApiConnection {
    protected final static String apiUrl = "http://192.168.1.97:8080";
    private static ApiConnection instance = new ApiConnection();

    private ApiConnection () {        
    
    }

    public static ApiConnection getInstance() {
        return instance;
    }

    private String getToken() {
        FileReader file;
        try {
            file = new FileReader(new File("C:/Users/" + System.getProperty("user.name") + "/Desktop/token_auth"));
            BufferedReader reader = new BufferedReader(file);

            final String token = reader.readLine();
            reader.close();

            return token;
        } catch (IOException e) {
            // ? LOG: Token File not founded skiping...
            e.printStackTrace();
        }

        return "";
    }

    public String addUser(UserData userModelClient) {
        try {
            URI uri = new URI(apiUrl + "/user/addUser");
            HttpURLConnection connectionToApi = (HttpURLConnection) uri.toURL().openConnection();
            
            connectionToApi.setRequestMethod("PUT");
            connectionToApi.setDoOutput(true);
            connectionToApi.setRequestProperty("Authorization", "Bearer " + getToken());
            
            OutputStream out = connectionToApi.getOutputStream();
            out.write(DataToJson.userDataToJson(userModelClient).getBytes());

            // ? LOG: Save api response
            return ApiResponseReader.getResponse(connectionToApi.getInputStream());
        } catch (URISyntaxException | IOException e) {
            // ? ERROR WHILE TRYING TO CONNECT CONTROLLER METHOD USING PUT
            e.printStackTrace();
        }

        return null;
    }

    public String login(String username, String password) {
        try {
            URI uri = new URI(apiUrl + "/user/login");
            HttpURLConnection connection = (HttpURLConnection)uri.toURL().openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            connection.setRequestProperty("Authorization", "Bearer " + getToken());

            OutputStream out = connection.getOutputStream();
            out.write(DataToJson.loginJson(username, password).getBytes("UTF-8"));

            System.out.println(ApiResponseReader.getResponse(connection.getInputStream()));
        } catch (IOException | URISyntaxException e) {
            // ? LOG : Error while trying to connect to controller method using put
            e.printStackTrace();
        }

        return null;
    }
}
