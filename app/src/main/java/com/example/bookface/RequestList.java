//RequestList.java

package com.example.bookface;

import java.util.*;

/**
 * This is a class that keeps track of a list of requests
 */
public class RequestList {
    private List<Book> requests = new ArrayList<>();

    /**
     * This changes the status of the request as accepted
     * @param request
     */
    public void acceptRequest(Request request) {
        request.setRequestStatus("accepted");
    }

    /**
     * This changes the status of the request as declined
     * @param request
     */
    public void declineRequest(Request request) {
        request.setRequestStatus("declined");
    }
}
