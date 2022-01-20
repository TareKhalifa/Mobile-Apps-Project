package com.example.project;

import java.util.ArrayList;

public class Transaction {
    private String name;
    private String PayerID;
    private ArrayList<String> PayeeIDs;
    private String date;
    private String amount;
    public Transaction()
    {
        this.name = "";
        this.PayerID = "";
        this.PayeeIDs = new ArrayList<String>();
        this.date = "";
        this.amount = "";
    }
    public Transaction(String name, String PayerID, ArrayList<String> PayeeIDs, String date, String amount) {
        this.name = name;
        this.PayerID = PayerID;
        this.PayeeIDs = PayeeIDs;
        //get current date and time
        this.date = date;
        this.amount = amount;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getPayerID() {
        return PayerID;
    }
    public void setPayerID(String PayerID) {
        this.PayerID = PayerID;
    }
    public ArrayList<String> getPayeeIDs() {
        return PayeeIDs;
    }
    public void setPayeeIDs(ArrayList<String> PayeeIDs) {
        this.PayeeIDs = PayeeIDs;
    }
    public String getAmount() {
        return amount;
    }
    public void setAmount(String amount) {
        this.amount = amount;
    }
    public String getDate() {
        return date;
    }
}

