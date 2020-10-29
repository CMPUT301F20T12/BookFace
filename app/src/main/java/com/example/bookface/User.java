package com.example.bookface;

public class User {
    private String username;
    private String name;
    private String email;
    private String contactNo;

    public User(String username, String name, String email, String contactNo) {
        this.username = username;
        this.name = name;
        this.email = email;
        this.contactNo = contactNo;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
}
