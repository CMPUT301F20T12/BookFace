package com.example.bookface;

import java.util.ArrayList;

public class User {
    private String username;
    private String email;
    private String contactNo;
    private ArrayList<String> myBookList;

    public User(String username, String email, String contactNo) {
        this.username = username;
        this.email = email;
        this.contactNo = contactNo;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getContactNo() {
        return contactNo;
    }

    public void setContactNo(String contactNo) {
        this.contactNo = contactNo;
    }

    public ArrayList<String> getMyBookList() {
        return myBookList;
    }

    public void setMyBookList(ArrayList<String> myBookList) {
        this.myBookList = myBookList;
    }
}
