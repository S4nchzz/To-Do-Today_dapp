package com.to_do_dapp.controllers.mainAppController.toDoManagement;

public class ToDoData {
    private final int id;
    private final int userId;
    private final String header;
    private final String content;
    private final String date;
    private final boolean fav;
    private final boolean ended;

    public ToDoData(int id, int userId, String header, String content, String date, boolean fav, boolean ended) {
        this.id = id;
        this.userId = userId;
        this.header = header;
        this.content = content;
        this.date = date;
        this.fav = fav;
        this.ended = ended;
    }

    public int getId() {
        return id;
    }

    public int getUserId() {
        return userId;
    }

    public String getHeader() {
        return header;
    }

    public String getContent() {
        return content;
    }

    public String getDate() {
        return date;
    }

    public boolean isFav() {
        return fav;
    }

    public boolean isEnded() {
        return this.ended;
    }
}