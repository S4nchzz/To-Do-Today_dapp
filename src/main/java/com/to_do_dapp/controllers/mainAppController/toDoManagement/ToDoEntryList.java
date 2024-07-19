package com.to_do_dapp.controllers.mainAppController.toDoManagement;

import java.util.ArrayList;

public class ToDoEntryList {
    private static final ToDoEntryList instance = new ToDoEntryList();

    private final ArrayList<ToDoData> toDoList = new ArrayList<>();

    private ToDoEntryList() {
    }

    public static ToDoEntryList getInstance() {
        return instance;
    }

    public void addToDoAtList(ToDoData toDoData) {
        // ! CODIGO TEMPORAL, COMO SE AÑADE TODOS LOS TO DOS EN CADA INTERACCION
        // ! CON LA API AL QUERER OBTENER COMO MINIMO UNO SE LIMPIA LA LISTA

        // * Habra que añadir unicamente el que se pide y no todos de golpe
        
        toDoList.add(toDoData);
    }

    public ArrayList<ToDoData> getToDoList() {
        return this.toDoList;
    }
}
