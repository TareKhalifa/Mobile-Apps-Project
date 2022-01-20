package com.example.project;

public class LoggedInUser {
    public static String displayName;
    public static String email;
    public static String photoUrl;
    public static Group [] groups;
    public static String uid;
    public static boolean isthisthefirsttime = true;
    //create getters and setters
    public String getDisplayName() {
        return displayName;
    }
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getPhotoUrl() {
        return photoUrl;
    }
    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }
    public Group[] getGroups() {
        return groups;
    }
    public void setGroups(Group[] groups) {
        this.groups = groups;
    }
    public String getUid() {
        return uid;
    }
    public void setUid(String uid) {
        this.uid = uid;
    }

}
