package com.example.bookface;

import java.util.ArrayList;

public class User {
    private String username;
    private String email;
    private String contactNo;
    private ArrayList<String> booksOwned;

    public User(String username, String email, String contactNo) {
        this.username = username;
        this.email = email;
        this.contactNo = contactNo;
        this.booksOwned = new ArrayList<String>();
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

    public ArrayList<String> getBooksOwned() {
        return booksOwned;
    }

    public void setBooksOwned(ArrayList<String> booksOwned) {
        this.booksOwned = booksOwned;
    }
}
