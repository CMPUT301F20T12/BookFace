//RequestList.java

package com.example.bookface;

import java.util.*;


public class RequestList {
    private List<Book> requests = new ArrayList<>();

    public void acceptRequest(Request request) {
        request.setRequestStatus("accepted");
    }

    public void declineRequest(Request request) {
        request.setRequestStatus("declined");
    }
}
