package com.to_do_dapp.controllers.mainAppController.toDoManagement;

import java.util.ArrayList;

public class ToDoControllerList {
    private static final ToDoControllerList instance = new ToDoControllerList();

    private final ArrayList<ToDoController> toDoList = new ArrayList<>();

    private ToDoControllerList() {
    }

    public static ToDoControllerList getInstance() {
        return instance;
    }

    public void addToDoAtList(ToDoController toDoData) {
        toDoList.add(toDoData);
    }

    public void removeToDoAtList(final int id) {
        int listPos = 0;
        for (ToDoController todo : toDoList) {
            if (todo.getId() == id) {
                toDoList.remove(listPos);
                return;
            }

            listPos++;
        }
    }

    public void clearList() {
        toDoList.clear();
    }

    public ArrayList<ToDoController> getToDoList() {
        return this.toDoList;
    }

}
