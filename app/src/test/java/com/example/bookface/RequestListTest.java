package com.example.bookface;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class RequestListTest {

    private RequestList mockRequestList() {
        RequestList requestList = new RequestList();
        requestList.addRequest(mockRequest());
        return requestList;
    }

    private Request mockRequest() {
        return new Request(mockBookRequested1(), mockBorrower(), "pending");
    }

    private Book mockBookRequested1() {
        return new Book("The Book Title", "Author Name", "1000010000100", "This is a mock book.",
                "available", "mlee1","");
    }

    private User mockBorrower() {
        return new User("username", "User Name", "username@ualberta.ca", "000-000-0000");
    }

    private Book mockBookRequested2() {
        return new Book("The Book Title2", "Author Name2", "1000010000102", "This is a mock book 2.",
                "available", "mlee1","");
    }

    private Book mockBookRequested3() {
        return new Book("The Book Title3", "Author Name3", "1000010000103", "This is a mock book 3.",
                "available", "mlee1","");
    }

    @Test
    public void testAddRequest() {
        RequestList requestList = mockRequestList();
        assertEquals(1, requestList.size());
        Request request = new Request(mockBookRequested2(), mockBorrower(), "pending");
        requestList.addRequest(request);
        assertEquals(2, requestList.size());

        assertTrue(requestList.contains(request));
    }

    @Test
    public void testAddException() {
        final RequestList requestList = mockRequestList();
        final Request request = new Request(mockBookRequested3(), mockBorrower(), "pending");
        requestList.addRequest(request);

        assertThrows(IllegalArgumentException.class, () -> {
            requestList.addRequest(request);
        });
    }

    @Test
    public void testDeleteRequest() {
        RequestList requestList = mockRequestList();
        assertEquals(1, requestList.size());
        Request request = new Request(mockBookRequested2(), mockBorrower(), "pending");
        requestList.addRequest(request);
        assertEquals(2, requestList.size());

        assertEquals(true, requestList.deleteRequest(request));
        assertFalse(requestList.contains(request));
    }

}
