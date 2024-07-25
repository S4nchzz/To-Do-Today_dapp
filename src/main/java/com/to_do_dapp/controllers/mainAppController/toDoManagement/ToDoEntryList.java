package com.to_do_dapp.controllers.mainAppController.toDoManagement;

import java.util.ArrayList;

public class ToDoEntryList {
    private static final ToDoEntryList instance = new ToDoEntryList();

    private final ArrayList<ToDoController> toDoList = new ArrayList<>();

    private ToDoEntryList() {
    }

    public static ToDoEntryList getInstance() {
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
