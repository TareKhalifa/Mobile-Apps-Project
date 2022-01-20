package com.example.project;

public class DebtData
{
    String name1;
    String img1;
    String value;
    String name2;
    String img2;

    public DebtData(String name1, String img1, String value, String name2, String img2)
    {
        this.name1 = name1;
        this.img1 = img1;
        this.value = value;
        this.name2 = name2;
        this.img2 = img2;
    }
    //setters and getters
    public String getName1() {
        return name1;
    }

    public void setName1(String name1) {
        this.name1 = name1;
    }

    public String getImg1() {
        return img1;
    }

    public void setImg1(String img1) {
        this.img1 = img1;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getName2() {
        return name2;
    }

    public void setName2(String name2) {
        this.name2 = name2;
    }

    public String getImg2() {
        return img2;
    }

    public void setImg2(String img2) {
        this.img2 = img2;
    }
}
