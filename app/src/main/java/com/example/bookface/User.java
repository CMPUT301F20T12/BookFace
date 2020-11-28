package com.example.bookface;

import java.util.ArrayList;

/**
 * This is a class that contains the attributes for a user
 */
public class User {

    // Declare variables
    private String username;
    private String email;
    private String contactNo;
    private ArrayList<String> booksOwned;
    private ArrayList<String> booksBorrowed;

    /**
     * This is the constructor
     * @param username
     * @param email
     * @param contactNo
     */
    public User(String username, String email, String contactNo) {
        this.username = username;
        this.email = email;
        this.contactNo = contactNo;
        this.booksOwned = new ArrayList<String>();
        this.booksBorrowed = new ArrayList<String>();
    }

    public User() {

    }

    // Getters and setters
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

    // the following getters and setters to be implemented later - not included in tests
    public ArrayList<String> getBooksOwned() {
        return booksOwned;
    }

    public void setBooksOwned(ArrayList<String> booksOwned) {
        this.booksOwned = booksOwned;
    }

    public ArrayList<String> getBooksBorrowed() {
        return booksBorrowed;
    }

    public void setBooksBorrowed(ArrayList<String> booksBorrowed) {
        this.booksBorrowed = booksBorrowed;
    }

    /**
     * Overriding the equals method to make it compatible with the class
     * @param o
     * @return
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        User user = (User) o;
        if (username.equals(user.username) && email.equals(user.email) && contactNo.equals(user.contactNo)) {
            return true;
        } else {
            return false;
        }
    }
}
