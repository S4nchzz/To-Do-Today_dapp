package com.to_do_dapp.controllers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import com.to_do_dapp.api.ApiConnection;

public class ToDoFiles {
    public static final String toDoTodayAbsolutePath = "C:/Users/" + System.getProperty("user.name") + "/AppData/Local/ToDoToday/";
    public static final String authApiFile = "authApi.tkn";
    public static final String authTempUserFile = "authTempUser.tkn";
    public static final String authKeepLoggedInFile = "authKeepLoggedIn.tkn";

    public static boolean createNecesaryFiles() {
        File folder = new File(toDoTodayAbsolutePath);
        folder.mkdir();
        
        File authServerMethodsFile = new File(toDoTodayAbsolutePath + authApiFile);
        File authTokenUser = new File(toDoTodayAbsolutePath + authTempUserFile);
        File authKeepLoggedIn = new File(toDoTodayAbsolutePath + authKeepLoggedInFile);

        try {
            authServerMethodsFile.createNewFile();
            authTokenUser.createNewFile();
            authKeepLoggedIn.createNewFile();

            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                File deleteTempToken = new File(toDoTodayAbsolutePath + authTempUserFile);
                deleteTempToken.delete();
            }));
            } catch (IOException e) {
                // ? LOG: Failed to create authApi.tkn file, check entire path
                // ? LOG: Failed to create ToDoToday folder on appdata/Local cause: Local folder exists(?) 
                e.printStackTrace();
                return false;
            }

        return true;
    }

    public static String getTempUserToken() throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(new File(toDoTodayAbsolutePath + authTempUserFile)));

        String content = reader.readLine();

        //If there is no token on the userTempToken file ensure to generate a new one
        
        if (content == null) {
            overwriteUserTempTokenOnLogin();
            content = reader.readLine();
        }
        reader.close();
        return content;
    }

    /**
     * This method will be called when the user mark keep logged in
     * in that moment the next time the user tries to get into the
     * app this method will autoGenerate a new token for that
     * login instance
     */ 
    
    public static void overwriteUserTempTokenOnLogin() {
        ApiConnection api = ApiConnection.getInstance();
        api.generateUserTempToken();
    }

    public static String getKeepLoggedTkn() {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(new File(toDoTodayAbsolutePath + authKeepLoggedInFile)));
            String content = reader.readLine();
            reader.close();
            
            return content;
        } catch (IOException e) {
            // ? LOG: File keepLogged in not found
        }

        return null;
    }
}
