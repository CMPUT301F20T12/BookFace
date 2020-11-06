package com.example.bookface;

public class Book {
    private String title;
    private String author;
    private String ISBN;
    private String description;
    private String status;
    private String ownerUsername;
    private String borrowerUsername;
    // private (Image?) photo;

    public Book(String title, String author, String ISBN, String description,
                String status, String ownerUsername, String borrowerUsername) { //mising photo
        this.title = title;
        this.author = author;
        this.ISBN = ISBN;
        this.description = description;
        this.status = status;
        this.ownerUsername = ownerUsername;
        this.borrowerUsername = borrowerUsername;
    }

    public String getOwnerUsername() {
        return ownerUsername;
    }

    public void setOwnerUsername(String ownerUsername) {
        this.ownerUsername = ownerUsername;
    }

    public String getBorrowerUsername() {
        return borrowerUsername;
    }

    public void setBorrowerUsername(String borrowerUsername) {
        this.borrowerUsername = borrowerUsername;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getISBN() {
        return ISBN;
    }

    public void setISBN(String ISBN) {
        this.ISBN = ISBN;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}