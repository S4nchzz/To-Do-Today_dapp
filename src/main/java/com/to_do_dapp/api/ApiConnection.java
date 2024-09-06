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
import com.to_do_dapp.controllers.mainAppController.groupManagement.GroupData;
import com.to_do_dapp.controllers.mainAppController.groupManagement.GroupElementController;
import com.to_do_dapp.controllers.mainAppController.toDoManagement.ToDoController;

public class ApiConnection {
    protected final static String apiUrl = "http://192.168.1.98:8080";
    private static ApiConnection instance = new ApiConnection();

    private ApiConnection () {
    }

    public static ApiConnection getInstance() {
        return instance;
    }

    private ResponseEntity<String> simpleRequestWithoutContent(String path) {
        RestTemplate conn = new RestTemplate();

        HttpHeaders header = new HttpHeaders();
        try {
            header.add("Authorization", "Bearer " + ToDoFiles.getTempUserToken());
            HttpEntity<String> entity = new HttpEntity<>(header);

            return conn.postForEntity(apiUrl + path, entity, String.class);
        } catch (IOException e) {
            //? LOG: Token fiele not founded
        }

        return null;
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

    public boolean addUser(UserData userModelClient) {
        RestTemplate apiCreateAcc = new RestTemplate();
        
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
     
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
        jsonUserToken = new JSONObject();
        
        try {
            header.add("Authorization", "Bearer " + ToDoFiles.getTempUserToken());
        } catch (IOException e) {
            // ? LOG: Cannot find user temp token
        }

        HttpEntity<String> httpEntity = new HttpEntity<>(jsonUserToken.toString(), header);

        ResponseEntity<String> toDos = getToDoS.postForEntity(apiUrl + "/toDos/getToDos", httpEntity, String.class);
        
        if (toDos.getBody() != null) {
            JSONObject jsonResponse = new JSONObject(toDos.getBody());
            java.util.Iterator<String> iterator = jsonResponse.keys();

            ArrayList<JSONObject> toDoList = new ArrayList<>();
            while (iterator.hasNext()) {
                String i = iterator.next();

                try {
                    toDoList.add(jsonResponse.getJSONObject(i));
                } catch (JSONException e) {

                }
            }
            return toDoList;
        }
        
        return null;
        
    }

    public boolean addToDo (JSONObject toDoData) {
        RestTemplate addToDo = new RestTemplate();

        HttpHeaders header = new HttpHeaders();
        header.setContentType(MediaType.APPLICATION_JSON);
        try {
            header.add("Authorization", "Bearer " + ToDoFiles.getTempUserToken());
        } catch (IOException e) {
            //? LOG: Cannot find user temp token
        }

        HttpEntity<String> httpEntity = new HttpEntity<>(toDoData.toString(), header);

        ResponseEntity<String> response = addToDo.postForEntity(apiUrl + "/toDos/addToDo", httpEntity, String.class);

        return new JSONObject(response.getBody()).getBoolean("addToDoSucced");
    }

    public void generateUserTempToken() {
        ResponseEntity<String> response = simpleRequestWithoutContent("/user/generateUserTempToken");
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(new File(ToDoFiles.toDoTodayAbsolutePath + ToDoFiles.authTempUserFile)));
            writer.write(response.getBody());
            writer.close();
        } catch (IOException e) {
            //? LOG: File authTempUserFile not found
        }
    }

    public void generateKeepLoggedToken() {
        ResponseEntity<String> response = simpleRequestWithoutContent("/user/generateKeepLoggedTkn");
        
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
        ResponseEntity<String> response = simpleRequestWithoutContent("/user/getUserName");
        return new JSONObject(response.getBody()).getString("username");
    }

    public boolean updateToDo(JSONObject updatedData) {
        RestTemplate connection = new RestTemplate();
        
        HttpHeaders header = new HttpHeaders();
        header.setContentType(MediaType.APPLICATION_JSON);
        try {
            header.add("Authorization", "Bearer " + ToDoFiles.getTempUserToken());
        } catch (IOException e) {
            //? log: Unnable to find user temp token
        }
        
        HttpEntity<String> entity = new HttpEntity<>(updatedData.toString(), header);
        
        ResponseEntity<String> response = connection.postForEntity(apiUrl + "/toDos/updateToDo", entity, String.class);

        return new JSONObject(response.getBody()).getBoolean("updated");
    }

    public boolean completeToDo(ToDoController toDoEntry, boolean completed) {
        RestTemplate connection = new RestTemplate();

        HttpHeaders header = new HttpHeaders();
        header.setContentType(MediaType.APPLICATION_JSON);
        
        try {
            header.add("Authorization", "Bearer " + ToDoFiles.getTempUserToken());
        } catch (IOException e) {
            //? LOG: User temp token not found
        }

        JSONObject jsonWithToDoData = new JSONObject();
        

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

        JSONObject jsonWithId;
        try {

            HttpHeaders header = new HttpHeaders();
            header.setContentType(MediaType.APPLICATION_JSON);
            header.add("Authorization", "Bearer " + ToDoFiles.getTempUserToken());

            jsonWithId = new JSONObject()
            .put("deleteID", id);

            HttpEntity<String> entity = new HttpEntity<>(jsonWithId.toString(), header);

            ResponseEntity<String> response = connection.postForEntity(apiUrl + "/toDos/deleteToDo", entity,
                    String.class);

            if (new JSONObject(response.getBody()).getBoolean("deleted")) {
                return true;
            }

        } catch (JSONException |IOException e) {
            // ? LOG: UserToken not found
        }

        return false;
    }

    public boolean setFav(ToDoController toDoController, boolean isFavSelected) {
        RestTemplate conn = new RestTemplate();

        HttpHeaders header = new HttpHeaders();
        header.setContentType(MediaType.APPLICATION_JSON);

        try {
            header.add("Authorization", "Bearer " + ToDoFiles.getTempUserToken());
        } catch (IOException e) {
            // ? log: Unnable to find user temp token
        }

        JSONObject toDoJson;
        try {
            toDoJson = new JSONObject()
            .put("id", toDoController.getId())
            .put("fav", isFavSelected);

            HttpEntity<String> entity = new HttpEntity<>(toDoJson.toString(), header);

            ResponseEntity<String> response = conn.postForEntity(apiUrl + "/toDos/addFav", entity, String.class);

            JSONObject responseOnJson = new JSONObject(response.getBody());
            if (responseOnJson.getBoolean("updated")) {
                return true;
            }
        } catch (JSONException e) {
            // ? LOG: Unnable to find temp token
        }

        return false;
    }

    public boolean userIsInGroup() {
        ResponseEntity<String> response = simpleRequestWithoutContent("/user/isInGroup");
        JSONObject responseOnJson = new JSONObject(response.getBody());
    
        return responseOnJson.getBoolean("hasGroup");
    }

    public ArrayList<GroupElementController> getTeams() {
        ResponseEntity<String> responseEntity = simpleRequestWithoutContent("/teams/getGroups");
            JSONObject response = new JSONObject(responseEntity.getBody());
            if (!response.getBoolean("responseStatus")) {
                return null;
            }

            response.remove("responseStatus");

            java.util.Iterator<String> iterator = response.keys();

            ArrayList<GroupElementController> groupElementList = new ArrayList<>();
            while (iterator.hasNext()) {
                String i = iterator.next();

                JSONObject group = new JSONObject(response.getJSONObject(i).toString());

                if (group.getBoolean("publicgroup")) {
                    groupElementList.add(new GroupElementController(group));
                }
            }
            return groupElementList;
    }

    public boolean associateUserToGroup(GroupElementController group) {
        RestTemplate conn = new RestTemplate();
        
        HttpHeaders header = new HttpHeaders();
        header.setContentType(MediaType.APPLICATION_JSON);
        try {
            header.add("Authorization" ,"Bearer " + ToDoFiles.getTempUserToken());

            HttpEntity<String> entity = new HttpEntity<>(new JSONObject().put("groupId", group.getTeamkey()).toString(), header);
            
            ResponseEntity<String> response = conn.postForEntity(apiUrl + "/teams/associateUser", entity, String.class);

            JSONObject responseOnJSON = new JSONObject(response.getBody());

            if (!responseOnJSON.getBoolean("joined") && !getGroupData()) {
                return false;
            }
            
            getGroupData();
            return responseOnJSON.getBoolean("joined");
        
        } catch (IOException e) {
            //? LOG: User temp token not found
        }

        return false;
    }

    public boolean getGroupData() {
        ResponseEntity<String> response = simpleRequestWithoutContent("/teams/getGroupData");
        JSONObject responseOnJSON = new JSONObject(response.getBody());

        if (!responseOnJSON.getBoolean("dataExist")) {
            return false;
        }

        GroupData groupData = GroupData.getInstance();
        groupData.setTeamKey(responseOnJSON.getString("teamKey"));
        groupData.setTitle(responseOnJSON.getString("title"));
        groupData.setDescription(responseOnJSON.getString("description"));
        groupData.setAdministrator(responseOnJSON.getInt("administrator"));
        groupData.setPublicgroup(responseOnJSON.getBoolean("publicGroup"));
        groupData.setPassword(responseOnJSON.getString("password"));
        groupData.setDate(responseOnJSON.getString("date"));
        groupData.setGroupDataPlaced();

        return true;
    }

    public boolean createTeam(JSONObject team) {
        RestTemplate conn = new RestTemplate();

        HttpHeaders header = new HttpHeaders();
        header.setContentType(MediaType.APPLICATION_JSON);
        try {
            header.add("Authorization", "Bearer " + ToDoFiles.getTempUserToken());
            
            HttpEntity<String> entity = new HttpEntity<>(team.toString(), header);
            ResponseEntity<String> response = conn.postForEntity(apiUrl + "/teams/createNewTeam", entity, String.class);

            JSONObject responseOJsonObject = new JSONObject(response.getBody());

            return responseOJsonObject.getBoolean("newGroupRequest");
            
        } catch (IOException e) {
            //? LOG: User temp token not found
        }

        return false;
    }

    public boolean isEmailVerified() {
        ResponseEntity<String> response = simpleRequestWithoutContent("/user/isEmailVerified");
        JSONObject responseOJsonObject = new JSONObject(response.getBody());
        return responseOJsonObject.getBoolean("isEmailVerified");
    }

    public boolean requestVerification() {
        ResponseEntity<String> response = simpleRequestWithoutContent("/verifyEmail/requestVerification");
            JSONObject responseOJsonObject = new JSONObject(response.getBody());

            return responseOJsonObject.getBoolean("requestVerificationStatus");
    }

    public boolean sendVerificationCode(String code) {
        int parseCode = Integer.valueOf(code);
        RestTemplate conn = new RestTemplate();

        HttpHeaders header = new HttpHeaders();
        header.setContentType(MediaType.APPLICATION_JSON);
        try {
            header.add("Authorization", "Bearer " + ToDoFiles.getTempUserToken());

            HttpEntity<String> entity = new HttpEntity<>(new JSONObject().put("code", parseCode).toString(), header);
            ResponseEntity<String> response = conn.postForEntity(apiUrl + "/verifyEmail/confirmVerification", entity,
                    String.class);

            JSONObject responseOJsonObject = new JSONObject(response.getBody());

            return responseOJsonObject.getBoolean("verificationCompletedStatus");

        } catch (IOException e) {
            // ? LOG: User temp token not found
        }

        return false;
    }

    public String getEmail() {
        ResponseEntity<String> response = simpleRequestWithoutContent("/user/getEmail");
        JSONObject responseOJsonObject = new JSONObject(response.getBody());

        return responseOJsonObject.getString("email");
    }

    public JSONObject getMembersFromGroup() {
        RestTemplate conn = new RestTemplate();

        HttpHeaders header = new HttpHeaders();
        try {
            header.add("Authorization", "Bearer " + ToDoFiles.getTempUserToken());

            GroupData groupData = GroupData.getInstance();

            HttpEntity<String> entity = new HttpEntity<>(new JSONObject().put("teamKey", 
                    groupData.getTeamKey()).toString(), header);
            ResponseEntity<String> response = conn.postForEntity(apiUrl + "/teams/getMembers", entity,
                    String.class);

            return new JSONObject(response.getBody());


        } catch (IOException e) {
            // ? LOG: User temp token not found
        }

        return null;
    }

    public void setUserOnline() {
        simpleRequestWithoutContent("/user/setOnlineUser");
    }

    public boolean leaveGroup() {
        ResponseEntity<String> response = simpleRequestWithoutContent("/teams/leaveGroup");
        JSONObject responseOnJson = new JSONObject(response.getBody());
        
        return responseOnJson.getBoolean("leavedGroup");
    }

    public void addOrChangePassword(String teamKey, String password) {
        RestTemplate conn = new RestTemplate();
        HttpHeaders header = new HttpHeaders();
        try {
            header.add("Authorization", "Bearer " + ToDoFiles.getTempUserToken());
        } catch (IOException e) {
            //? LOG: Unnable to find user temp token
            return;
        }

        header.setContentType(MediaType.APPLICATION_JSON);
        
        HttpEntity<String> entity = new HttpEntity<>(new JSONObject().put("teamKey", teamKey).put("newPassword", password).toString(), header);
        conn.postForEntity(apiUrl + "/teams/addOrChangePassword", entity, String.class);
    }

    public boolean amIAdminFromGroup() {
        ResponseEntity<String> response = simpleRequestWithoutContent("/teams/amiadmin");
        return new JSONObject(response.getBody()).getBoolean("areYouAdmin");
    }

    public boolean updateTeamAdmin(String teamKey, String username) {
        RestTemplate conn = new RestTemplate();
        HttpHeaders header = new HttpHeaders();
        try {
            header.add("Authorization", "Bearer " + ToDoFiles.getTempUserToken());
        } catch (IOException e) {
            // ? LOG: Unnable to find user temp token
            return false;
        }

        header.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> entity = new HttpEntity<>(
                new JSONObject().put("teamKey", teamKey).put("newAdminUsername", username).toString(), header);
        ResponseEntity<String> response = conn.postForEntity(apiUrl + "/teams/updateTeamAdmin", entity, String.class);
        JSONObject responseOnJson = new JSONObject(response.getBody());
        return responseOnJson.getBoolean("newAdminModificationStatus");
    
    }

    public void deleteEmptyGroup() {
        simpleRequestWithoutContent("/teams/deleteEmptyGroup");
    }

    public boolean deleteEntireTeam(String teamKey) {
        RestTemplate conn = new RestTemplate();
        HttpHeaders header = new HttpHeaders();
        try {
            header.add("Authorization", "Bearer " + ToDoFiles.getTempUserToken());
        } catch (IOException e) {
            // ? LOG: Unnable to find user temp token
            return false;
        }

        header.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> entity = new HttpEntity<>(
                new JSONObject().put("teamKey", teamKey).toString(), header);
        ResponseEntity<String> response = conn.postForEntity(apiUrl + "/teams/deleteEntireTeam", entity, String.class);
        JSONObject responseOnJson = new JSONObject(response.getBody());

        return responseOnJson.getBoolean("deleteTeamAction");
    }
}
