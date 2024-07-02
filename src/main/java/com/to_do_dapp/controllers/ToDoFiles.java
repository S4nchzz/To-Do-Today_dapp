package com.to_do_dapp.controllers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

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
                //? LOG: Failed to create ToDoToday folder on appdata/Local cause: Local folder exists(?) 
                e.printStackTrace();
                return false;
            }

        return true;
    }

    public static String getAuthUserToken() throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(new File(toDoTodayAbsolutePath + authTempUserFile)));
        
        String content = reader.readLine();
        reader.close();
        return content;
    }
}
