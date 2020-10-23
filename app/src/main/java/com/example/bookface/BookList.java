package com.example.bookface;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * This is a class that keeps track of a list of book objects
 */
public class BookList {
    private List<Book> books = new ArrayList<>();

    /**
     * This adds a book to the list if the book does not exist
     * @param book
     * This is a candidate book to add
     */
    public void addBook(Book book) {
        if (books.contains(book)) {
            throw new IllegalArgumentException();
        }
        books.add(book);
    }


    /**
     * This method deletes the book given to it
     * @param book
     * This is a candidate book to delete
     * @return
     * true if the book given was actually in the list and was deleted successfully
     * false otherwise
     */
    public boolean deleteBook(Book book) {
        if (books.contains(book)) {
            books.remove(book);
            return true;
        }
        else {
            return false;
        }
    }


    /**
     * This method searches the book based on the criterion keyword
     * @param keyword
     * This is the keyword on the basis of which search occurs
     * @return
     * The list of the books that matches the search
     */
    public List<Book> searchBook(String keyword) {
        List<Book> list = books;
        // TODO
        // Logic for this method

        return list;
    }

}
