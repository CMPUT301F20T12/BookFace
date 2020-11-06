package com.example.bookface;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

import android.location.Location;

import java.util.ArrayList;

public class NotificationTest {

    private Notification mockNotification() {
        return new Notification(mockBookRequested(), mockBorrower(), "pending", mockLocation());
    }

    private Book mockBookRequested() {
        return new Book("The Book Title", "Author Name", "1000010000100", "This is a mock book.",
                "available", "mlee1","", "image.com");
    }

    private User mockBorrower() {
        return new User("username", "username@ualberta.ca", "000-000-0000");
    }

    private ArrayList<String> mockBookOwned() {
        ArrayList<String> bookOwned = new ArrayList<String>();
        bookOwned.add("ownedBook");
        return bookOwned;
    }

    private ArrayList<String> mockBookBorrowed() {
        ArrayList<String> bookBorrowed = new ArrayList<String>();
        bookBorrowed.add("borrowedBook");
        return bookBorrowed;
    }

    private Location mockLocation() {
        return new Location("provider");
    }

    @Test
    public void testGetRequestedBook() {
        Notification notif = mockNotification();
        Book book = new Book("The Book Title", "Author Name", "1000010000100", "This is a mock book.",
                "available", "mlee1","", "image.com");
        assertEquals(book, notif.getRequestedBook());
    }

    @Test
    public void testSetRequestedBook() {
        Notification notif = mockNotification();
        Book book = new Book("The Book Title", "Author Name", "1000010000100", "This is a mock book.",
                "available", "mlee1","", "image.com");
        notif.setRequestedBook(book);
        assertEquals(book, notif.getRequestedBook());
    }

    @Test
    public void testGetBorrower() {
        Notification notif = mockNotification();
        User borrower = new User("username", "username@ualberta.ca", "000-000-0000");
        assertEquals(borrower, notif.getBorrower());
    }

    @Test
    public void testSetBorrower() {
        Notification notif = mockNotification();
        User borrower = new User("username2", "username2@ualberta.ca", "000-000-0002");
        notif.setBorrower(borrower);
        assertEquals(borrower, notif.getBorrower());
    }

    @Test
    public void testGetRequestStatus() {
        Notification notif = mockNotification();
        assertEquals("pending", notif.getRequestStatus());
    }

    @Test
    public void testSetRequestStatus() {
        Notification notif = mockNotification();
        notif.setRequestStatus("accepted");
        assertEquals("accepted", notif.getRequestStatus());
    }

    @Test
    public void testGetLocation() {
        Notification notif = mockNotification();
        Location location = new Location("provider");
        notif.setLocation(location);
        assertEquals(location, notif.getLocation());
    }

    @Test
    public void testSetLocation() {
        Notification notif = mockNotification();
        Location location = new Location("provider2");
        notif.setLocation(location);
        assertEquals(location, notif.getLocation());
    }

}
