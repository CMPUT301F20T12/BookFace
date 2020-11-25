package com.example.bookface;

/**
 * This is a class that contains the attributes for a Request
 */
public class Request {
    // Variable declarations
    private Book bookRequested;
    private User borrower;
    private String requestStatus;

    /**
     * This is the constructor
     * @param bookRequested
     * @param borrower
     * @param requestStatus
     */
    public Request(Book bookRequested, User borrower, String requestStatus) {
        this.bookRequested = bookRequested;
        this.borrower = borrower;
        this.requestStatus = requestStatus;
    };

    //TODO: Location Object

    /**
     * This method is used to notify the owner
     * @return
     */
    public boolean notifyOwner(){
        return false;
    }

    /**
     * This method is used to notify the borrower
     * @return
     */
    public boolean notifyBorrower(){
        return false;
    }

    /**
     * This method is used to get the book that was requested
     * @return
     * The book that was requested
     */
    public Book getBookRequested() {
        return bookRequested;
    }

    // Getters and setters
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
