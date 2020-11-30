package com.example.bookface;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * This is a class to test the methods of the object class Request
 */
public class RequestTest {

    private LocationHelper mockLocation() {
        return new LocationHelper(0, 0);
    }

    private Request mockRequest() {
        return new Request(mockBookRequested(), mockBorrower(), "pending", mockLocation());
    }

    private Book mockBookRequested() {
        return new Book("The Book Title", "Author Name", "1000010000100", "This is a mock book.",
                "available", "mlee1","", "imageurl.com");
    }

    private User mockBorrower() {
        return new User("username", "username@ualberta.ca", "000-000-0000");
    }


// To be implemented
//    @Test
//    public void testNotifyOwner() {
//        Request request = mockRequest();
//        //
//    }

// To be implemented
//    @Test
//    public void testNotifyBorrower() {
//        Request request = mockRequest();
//        //
//    }

    @Test
    public void testGetBookRequested() {
        Request request = mockRequest();
        Book book = new Book("The Book Title", "Author Name", "1000010000100", "This is a mock book.",
                "available", "mlee1","", "imageurl.com");
        // assertEquals(book, request.getBookRequested());
        assertTrue(book.equals(request.getBookRequested()));
    }

    @Test
    public void testSetBookRequested() {
        Request request = mockRequest();
        Book book = new Book("The Book Title2", "Author Name2", "1000010000102", "This is a mock book 2.",
                "available", "mlee1","", "imageurl.com");
        request.setBookRequested(book);
        assertEquals(book, request.getBookRequested());
    }

    @Test
    public void testGetBorrower() {
        Request request = mockRequest();
        User borrower = new User("username", "username@ualberta.ca", "000-000-0000");
        assertTrue(borrower.equals(request.getBorrower()));
    }

    @Test
    public void testSetBorrower() {
        Request request = mockRequest();
        User borrower = new User("username2", "username2@ualberta.ca", "000-000-0002");
        request.setBorrower(borrower);
        assertEquals(borrower, request.getBorrower());
    }

    @Test
    public void testGetRequestStatus() {
        Request request = mockRequest();
        assertEquals("pending", request.getRequestStatus());
    }

    @Test
    public void testSetRequestStatus() {
        Request request = mockRequest();
        request.setRequestStatus("accepted");
        assertEquals("accepted", request.getRequestStatus());
    }

}
