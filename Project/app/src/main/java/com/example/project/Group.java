package com.example.project;

import java.util.ArrayList;
import java.util.HashMap;

public class Group {
    private String name;
    private String ownerID;
    private String id;
    private String[] membersID;
    public HashMap<String, String>  photos = new HashMap<>();
    public HashMap<String, String>  names = new HashMap<>();
    public HashMap<String, Transaction> transactions = new HashMap<String, Transaction>();
    //create getters and setters
    Group(String name, String ownerID) {
        this.name = name;
        this.ownerID = ownerID;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getOwnerID() {
        return this.ownerID;
    }
    public void setOwnerID(String ownerID) {
        this.ownerID = ownerID;
    }
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String[] getMembersIDs() {
        return membersID;
    }
    public void setMembers(String[] members) {
        this.membersID = members;
    }

}
