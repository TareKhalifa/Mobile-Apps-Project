package com.example.project;

public class DropDownData {
    String name;
    String userImg;

    public DropDownData(String name, String userImg) {
        this.name = name;
        this.userImg = userImg;
    }

    public void setData(String name, String img) {
        this.name = name;
        this.userImg = img;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getUserImg() {
        return userImg;
    }
    public void setUserImg(String userImg) {
        this.userImg = userImg;
    }
}
