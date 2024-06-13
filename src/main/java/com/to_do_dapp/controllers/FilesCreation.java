package com.to_do_dapp.controllers;

import java.io.File;
import java.io.IOException;

public class FilesCreation {
    public static final String toDoTodayAbsolutePath = "C:/Users/" + System.getProperty("user.name") + "/AppData/Local/ToDoToday/";
    public static final String authApiFile = "authApi.tkn";
    public static final String authTempUserFile = "authTempUser.tkn";

    public static boolean createNecesaryFiles() {
        File folder = new File(toDoTodayAbsolutePath);
        folder.mkdir();
        
        File authServerMethodsFile = new File(toDoTodayAbsolutePath + authApiFile);
        File authTokenUser = new File(toDoTodayAbsolutePath + authTempUserFile);
        try {
            authServerMethodsFile.createNewFile();
            authTokenUser.createNewFile();

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
}
