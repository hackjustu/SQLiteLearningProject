package com.example.banana.sqlitelearningproject.model;

/**
 * Created by Citrixer on 2/27/15.
 */
public class Group {

    private int groupId;
    private String groupName;

    public Group() {
    }

    public Group(String groupName) {
        this.groupName = groupName;
    }

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }
}
