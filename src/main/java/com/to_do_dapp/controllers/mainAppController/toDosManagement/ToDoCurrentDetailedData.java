package com.to_do_dapp.controllers.mainAppController.toDosManagement;

public class ToDoCurrentDetailedData{
    private final static ToDoCurrentDetailedData instance = new ToDoCurrentDetailedData();

    private int id;
    private int userid;
    private String header;
    private String content;
    private String date;
    private boolean fav;

    private ToDoCurrentDetailedData() {
    }

    public static ToDoCurrentDetailedData getInstance() {
        return instance;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public boolean isFav() {
        return fav;
    }

    public void setFav(boolean fav) {
        this.fav = fav;
    }
}