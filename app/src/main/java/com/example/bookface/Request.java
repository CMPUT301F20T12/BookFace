package com.example.bookface;

/**
 * This is a class that contains the attributes for a Request
 */
public class Request {
    private Book bookRequested;
    private User borrower;
    private String requestStatus;

    public Request(Book bookRequested, User borrower, String requestStatus) {
        this.bookRequested = bookRequested;
        this.borrower = borrower;
        this.requestStatus = requestStatus;
    };

    //TODO: Location Object

    public boolean notifyOwner(){
        return false;
    }

    public boolean notifyBorrower(){
        return false;
    }

    public Book getBookRequested() {
        return bookRequested;
    }

    public void setBookRequested(Book bookRequested) {
        this.bookRequested = bookRequested;
    }

    public User getBorrower() {
        return borrower;
    }

    public void setBorrower(User borrower) {
        this.borrower = borrower;
    }

    public String getRequestStatus() {
        return requestStatus;
    }

    public void setRequestStatus(String requestStatus) {
        this.requestStatus = requestStatus;
    }
}
