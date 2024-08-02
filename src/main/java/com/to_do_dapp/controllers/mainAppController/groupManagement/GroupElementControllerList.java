package com.to_do_dapp.controllers.mainAppController.groupManagement;

import java.util.ArrayList;

public class GroupElementControllerList {
    private static final GroupElementControllerList instance = new GroupElementControllerList();

    private final ArrayList<GroupElementController> groupElementList = new ArrayList<>();

    private GroupElementControllerList() {
    }

    public static GroupElementControllerList getInstance() {
        return instance;
    }

    public void addGroupElement(GroupElementController groupElement) {
        groupElementList.add(groupElement);
    }

    public void removeGroupElementById(final String teamkey) {
        int listPos = 0;
        for (GroupElementController groupElement : groupElementList) {
            if (groupElement.getTeamkey() == teamkey) {
                groupElementList.remove(listPos);
                return;
            }
            listPos++;
        }
    }

    public GroupElementController getGroupElementById(String teamkey) {
        for (GroupElementController groupElement : groupElementList) {
            if (groupElement.getTeamkey() == teamkey) {
                return groupElement;
            }
        }
        return null;
    }

    public void clearList() {
        groupElementList.clear();
    }

    public ArrayList<GroupElementController> getGroupElementList() {
        return this.groupElementList;
    }
}