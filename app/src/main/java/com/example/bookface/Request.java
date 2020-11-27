package com.example.bookface;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.firestore.DocumentReference;


/**
 * This is a class that contains the attributes for a Request
 */
public class Request implements Parcelable {
    private Book bookRequested;
    private DocumentReference borrower;
    private String requestStatus;

    public Request(Book bookRequested, DocumentReference borrower, String requestStatus) {
        this.bookRequested = bookRequested;
        this.borrower = borrower;
        this.requestStatus = requestStatus;
    };

    //TODO: Location Object

    protected Request(Parcel in) {
        requestStatus = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(requestStatus);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Request> CREATOR = new Creator<Request>() {
        @Override
        public Request createFromParcel(Parcel in) {
            return new Request(in);
        }

        @Override
        public Request[] newArray(int size) {
            return new Request[size];
        }
    };

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

    public DocumentReference getBorrower() {
        return borrower;
    }

    public void setBorrower(DocumentReference borrower) {
        this.borrower = borrower;
    }

    public String getRequestStatus() {
        return requestStatus;
    }

    public void setRequestStatus(String requestStatus) {
        this.requestStatus = requestStatus;
    }
}
