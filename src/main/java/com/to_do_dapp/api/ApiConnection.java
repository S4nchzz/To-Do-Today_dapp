package com.to_do_dapp.api;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.to_do_dapp.api.requests.req_AddUser.DataToJson;
import com.to_do_dapp.api.requests.req_AddUser.UserData;
import com.to_do_dapp.controllers.ToDoFiles;
import com.to_do_dapp.controllers.mainAppController.toDoManagement.ToDoController;

public class ApiConnection {
    protected final static String apiUrl = "http://192.168.1.98:8080";
    private static ApiConnection instance = new ApiConnection();

    private ApiConnection () {
    }

    public static ApiConnection getInstance() {
        return instance;
    }

    private String getApiAuthToken() {
        FileReader file;
        try {
            file = new FileReader(new File(ToDoFiles.toDoTodayAbsolutePath + ToDoFiles.authApiFile));
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

    private String getUserTempToken() {
        FileReader file;
        try {
            file = new FileReader(new File(ToDoFiles.toDoTodayAbsolutePath + ToDoFiles.authTempUserFile));
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
        httpHeaders.add("Authorization", "Bearer " + getApiAuthToken());
     
        HttpEntity<String> httpEntity = new HttpEntity<>(DataToJson.userDataToJson(userModelClient), httpHeaders);
        Boolean response = apiCreateAcc.postForObject(apiUrl + "/user/addUser", httpEntity, Boolean.class);
        
        return response;    
    }

    public JSONObject login(String username, String password) {
        RestTemplate apiLogin = new RestTemplate();
        
        HttpHeaders header = new HttpHeaders();
        header.add("Authorization", "Bearer " + getApiAuthToken());
        header.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> httpEntity = new HttpEntity<>(DataToJson.loginJson(username, password), header);
        ResponseEntity<String> response = apiLogin.postForEntity(apiUrl + "/user/login", httpEntity, String.class);
        
        JSONObject jsonResponse = new JSONObject(response.getBody());
        if (jsonResponse.getString("succed").equals("true")) {
            System.out.println(jsonResponse.toString());
            return jsonResponse;
        } else {
            // ? LOG : User or password incorrect
            return null;
        }
    }

    public ArrayList<JSONObject> getToDoS() {
        RestTemplate getToDoS = new RestTemplate();
        
        HttpHeaders header = new HttpHeaders();
        header.setContentType(MediaType.APPLICATION_JSON);
    
        JSONObject jsonUserToken;
        try {
            jsonUserToken = new JSONObject();
            jsonUserToken.put("userToken", ToDoFiles.getTempUserToken());
            HttpEntity<String> httpEntity = new HttpEntity<>(jsonUserToken.toString(), header);

            ResponseEntity<String> toDos = getToDoS.postForEntity(apiUrl + "/toDos/getToDos", httpEntity, String.class);
            
            JSONObject jsonResponse = new JSONObject(toDos.getBody());
            java.util.Iterator<String> iterator = jsonResponse.keys();

            ArrayList<JSONObject> toDoList = new ArrayList<>();
            while (iterator.hasNext()) {
                String i = iterator.next();
                toDoList.add(jsonResponse.getJSONObject(i));
            }

            return toDoList;
        } catch (IOException e) {
            // ? LOG: Error getting TempToken from userFile
        }

        return null;
    }

    public boolean addToDo (JSONObject toDoData) {
        RestTemplate addToDo = new RestTemplate();

        HttpHeaders header = new HttpHeaders();
        header.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> httpEntity = new HttpEntity<>(toDoData.toString(), header);

        ResponseEntity<String> response = addToDo.postForEntity(apiUrl + "/toDos/addToDo", httpEntity, String.class);

        return new JSONObject(response.getBody()).getBoolean("addToDoSucced");
    }

    public void generateUserTempToken() {
        RestTemplate getUserTempToken = new RestTemplate();

        HttpHeaders header = new HttpHeaders();
        header.setContentType(MediaType.TEXT_PLAIN);
        header.add("Authorization", "Bearer " + ToDoFiles.getKeepLoggedTkn());

        HttpEntity<String> entity = new HttpEntity<>(header);
        
        ResponseEntity<String> response = getUserTempToken.postForEntity(apiUrl + "/user/generateUserTempToken", entity, String.class);
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(new File(ToDoFiles.toDoTodayAbsolutePath + ToDoFiles.authTempUserFile)));
            writer.write(response.getBody());
            writer.close();
        } catch (IOException e) {
            //? LOG: File authTempUserFile not found
        }
    }

    public void generateKeepLoggedToken() {
        RestTemplate generateKeepLoggedInToken = new RestTemplate();

        HttpHeaders header = new HttpHeaders();
        header.setContentType(MediaType.APPLICATION_JSON);
        header.add("Authorization", "Bearer " + getUserTempToken());
        
        HttpEntity<String> httpEntity = new HttpEntity<>(header);

        ResponseEntity<String> response = generateKeepLoggedInToken.postForEntity(ApiConnection.apiUrl + "/user/generateKeepLoggedTkn", httpEntity, String.class);
        
        JSONObject json = new JSONObject(response.getBody());
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(new File(ToDoFiles.toDoTodayAbsolutePath + ToDoFiles.authKeepLoggedInFile)));
            writer.write(json.getString("KeepLoggedToken"));
            writer.close();
        } catch (IOException e) {
            // ? LOG: Unfounded file authKeepLoggedInFile
        }

        System.out.println(response);
    }

    public boolean checkKeepLoggedToken() {
        RestTemplate checkKeepLoggedUser = new RestTemplate();
        
        HttpHeaders header = new HttpHeaders();
        header.setContentType(MediaType.APPLICATION_JSON);
        header.add("Authorization", "Bearer " + getApiAuthToken());

        JSONObject keepLoggedUser = new JSONObject();

        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader(new File(
                    ToDoFiles.toDoTodayAbsolutePath + ToDoFiles.authKeepLoggedInFile)));
            
            String localKeepLoggedTkn = reader.readLine();
            localKeepLoggedTkn = localKeepLoggedTkn == null ? "" : localKeepLoggedTkn;

            keepLoggedUser.put("keepLoggedToken", localKeepLoggedTkn);
            reader.close();
            
            HttpEntity<String> httpEntity = new HttpEntity<>(keepLoggedUser.toString(), header);
            
            ResponseEntity<Boolean> response = checkKeepLoggedUser.postForEntity(
                    apiUrl + "/user/checkKeepLoggedTkn", httpEntity, Boolean.class);
            return response.getBody();

        } catch (IOException e) {
            // ? LOG: Unable to find or read file when calling api
            return false;
        }
    }

    public String getUserName() {
        RestTemplate getUserName = new RestTemplate();

        JSONObject json = new JSONObject();
        try {
            json.put("userTempToken", ToDoFiles.getTempUserToken());
        } catch (JSONException | IOException e) {
            //? LOG: File tempTokenUser not found
        }

        HttpEntity<String> httpEntity = new HttpEntity<>(json.toString(), new HttpHeaders());

        ResponseEntity<String> response = getUserName.postForEntity(apiUrl + "/user/getUserName", httpEntity, String.class);

        return response.getBody();
    }

    public boolean updateToDo(JSONObject updatedData) {
        RestTemplate connection = new RestTemplate();
        
        HttpHeaders header = new HttpHeaders();
        header.setContentType(MediaType.APPLICATION_JSON);
        
        HttpEntity<String> entity = new HttpEntity<>(updatedData.toString(), header);
        
        ResponseEntity<String> response = connection.postForEntity(apiUrl + "/toDos/updateToDo", entity, String.class);

        return new JSONObject(response.getBody()).getBoolean("updated");
    }

    public boolean completeToDo(ToDoController toDoEntry, boolean completed) {
        RestTemplate connection = new RestTemplate();

        HttpHeaders header = new HttpHeaders();
        header.setContentType(MediaType.APPLICATION_JSON);

        JSONObject jsonWithToDoData = new JSONObject();
        try {
            jsonWithToDoData.put("userToken", ToDoFiles.getTempUserToken());
        } catch (JSONException | IOException e) {
            //? LOG: User token not found or json error
        }

        jsonWithToDoData.put("id", toDoEntry.getId());
        jsonWithToDoData.put("completed", completed);

        HttpEntity<String> entity = new HttpEntity<>(jsonWithToDoData.toString(), header);

        ResponseEntity<String> response = connection.postForEntity(apiUrl + "/toDos/completeToDo", entity, String.class);

        try {
            JSONObject jsonResponse = new JSONObject(response.getBody());
            if (jsonResponse.getBoolean("toDoCompletedUpdateResult")) {
                return true;
            } else {
                // ? LOG: Uncorrectly updated ToDo
            }
        } catch (JSONException je) {
            // ? LOG: json response from server invalid
        }

        return false;
    }

    public boolean deleteToDo(int id) {
        RestTemplate connection = new RestTemplate();

        HttpHeaders header = new HttpHeaders();
        header.setContentType(MediaType.APPLICATION_JSON);
        
        JSONObject jsonWithId;
        try {
            jsonWithId = new JSONObject()
            .put("userToken", ToDoFiles.getTempUserToken())
            .put("deleteID", id);

            HttpEntity<String> entity = new HttpEntity<>(jsonWithId.toString(), header);

            ResponseEntity<String> response = connection.postForEntity(apiUrl + "/toDos/deleteToDo", entity,
                    String.class);

            if (new JSONObject(response.getBody()).getBoolean("deleted")) {
                return true;
            }

        } catch (JSONException | IOException e) {
            // ? LOG: UserToken not found
        }

        return false;
    }

    public boolean setFav(ToDoController toDoController, boolean isFavSelected) {
        RestTemplate conn = new RestTemplate();

        HttpHeaders header = new HttpHeaders();
        header.setContentType(MediaType.APPLICATION_JSON);

        JSONObject toDoJson;
        try {
            toDoJson = new JSONObject()
            .put("userToken", ToDoFiles.getTempUserToken())
            .put("id", toDoController.getId())
            .put("fav", isFavSelected);

            HttpEntity<String> entity = new HttpEntity<>(toDoJson.toString(), header);

            ResponseEntity<String> response = conn.postForEntity(apiUrl + "/toDos/addFav", entity, String.class);

            JSONObject responseOnJson = new JSONObject(response.getBody());
            if (responseOnJson.getBoolean("updated")) {
                return true;
            }
        } catch (JSONException | IOException e) {
            // ? LOG: Unnable to find temp token
        }

        return false;
    }
}
