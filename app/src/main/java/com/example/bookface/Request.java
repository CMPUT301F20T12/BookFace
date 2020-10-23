package com.example.bookface;

public class Request {
    private Book bookRequested;
    private User borrower;
    private String requestStatus;

    // TODO: Location JSON Object

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
