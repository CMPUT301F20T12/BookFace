package com.example.bookface;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * This is a class that contains the attributes for a Book
 */
public class Book implements Parcelable {
    private String title;
    private String author;
    private String ISBN;
    private String description;
    private String status;
    private String ownerUsername;
    private String borrowerUsername;
    private String imageUrl;

    public Book(String title, String author, String ISBN, String description,
                String status, String ownerUsername, String borrowerUsername, String imageUrl) {

        this.title = title;
        this.author = author;
        this.ISBN = ISBN;
        this.description = description;
        this.status = status;
        this.ownerUsername = ownerUsername;
        this.borrowerUsername = borrowerUsername;
        this.imageUrl = imageUrl;
    }

    protected Book(Parcel in) {
        title = in.readString();
        author = in.readString();
        ISBN = in.readString();
        description = in.readString();
        status = in.readString();
        ownerUsername = in.readString();
        borrowerUsername = in.readString();
        imageUrl = in.readString();
    }

    public static final Creator<Book> CREATOR = new Creator<Book>() {
        @Override
        public Book createFromParcel(Parcel in) {
            return new Book(in);
        }

        @Override
        public Book[] newArray(int size) {
            return new Book[size];
        }
    };

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
  
    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    // for testing
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        Book book = (Book) o;
        if (title.equals(book.title) && author.equals(book.author) && ISBN.equals(book.ISBN)
                && description.equals(book.description) && status.equals(book.status)
                && borrowerUsername.equals(book.borrowerUsername)) {
            return true;
        } else {
            return false;
        }

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(author);
        dest.writeString(ISBN);
        dest.writeString(description);
        dest.writeString(status);
        dest.writeString(ownerUsername);
        dest.writeString(borrowerUsername);
        dest.writeString(imageUrl);
    }
}

