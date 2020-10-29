package com.example.bookface;

import java.util.ArrayList;
import java.util.List;

/**
 * This is a class that keeps track of a list of request objects
 */
public class RequestList {
    private List<Request> requests = new ArrayList<>();

    /**
     * This method checks if the request exists in the request list
     * @param request
     * @return true if exists in request list
     *  false if it doesn't exist in request list
     */
    public boolean contains(Request request) {
        if (requests.contains(request)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * This method returns the size of the list
     * @return size of the list
     */
    public int size() {
        return requests.size();
    }

    /**
     * This adds a request to the list if the request does not exist
     * @param request
     */
    public void addRequest(Request request) {
        if (requests.contains(request)) {
            throw new IllegalArgumentException();
        }
        requests.add(request);
    }

    /**
     * This method deletes the book given to it
     * @param request
     * This is a candidate book to delete
     * @return true if the book given was actually in the list and was deleted successfully
     * false otherwise
     */
    public boolean deleteRequest(Request request) {
        if (requests.contains(request)) {
            requests.remove(request);
            return true;
        }
        else {
            return false;
        }
    }

}

