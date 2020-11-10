package com.example.bookface;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * This is a class to test the getters and setter of the object class Book
 */
public class BookTest {

    private Book mockBook() {
        return new Book("The Book Title", "Author Name", "1000010000100", "This is a mock book.",
                "available", "mlee1","pzafra", "image.com");
    }

    @Test
    public void testGetTitle() {
        Book book = mockBook();
        assertEquals("The Book Title", book.getTitle());
    }

    @Test
    public void testSetTitle() {
        Book book = mockBook();
        book.setTitle("The Other Title");
        assertEquals("The Other Title", book.getTitle());
    }

    @Test
    public void testGetAuthor() {
        Book book = mockBook();
        assertEquals("Author Name", book.getAuthor());
    }

    @Test
    public void testSetAuthor() {
        Book book = mockBook();
        book.setAuthor("Other Author");
        assertEquals("Other Author", book.getAuthor());
    }

    @Test
    public void testGetISBN() {
        Book book = mockBook();
        assertEquals("1000010000100", book.getISBN());
    }

    @Test
    public void testSetISBN() {
        Book book = mockBook();
        book.setISBN("10000000000000");
        assertEquals("10000000000000", book.getISBN());
    }

    @Test
    public void testGetDescription() {
        Book book = mockBook();
        assertEquals("This is a mock book.", book.getDescription());
    }

    @Test
    public void testSetDescription() {
        Book book = mockBook();
        book.setDescription("This is the other mock book.");
        assertEquals("This is the other mock book.", book.getDescription());
    }

    @Test
    public void testGetStatus() {
        Book book = mockBook();
        assertEquals("available", book.getStatus());
    }

    @Test
    public void testSetStatus() {
        Book book = mockBook();
        book.setStatus("accepted");
        assertEquals("accepted", book.getStatus());
    }

    @Test
    public void testGetOwnerUsername() {
        Book book = mockBook();
        assertEquals("mlee1", book.getOwnerUsername());
    }

    @Test
    public void testSetOwnerUsername() {
        Book book = mockBook();
        book.setOwnerUsername("poulomi");
        assertEquals("poulomi", book.getOwnerUsername());
    }

    @Test
    public void testGetBorrowerUsername() {
        Book book = mockBook();
        assertEquals("pzafra", book.getBorrowerUsername());
    }

    @Test
    public void testSetBorrowerUsername() {
        Book book = mockBook();
        book.setBorrowerUsername("kanishk");
        assertEquals("kanishk", book.getBorrowerUsername());
    }

    @Test
    public void testGetImageURL() {
        Book book = mockBook();
        assertEquals("image.com", book.getImageUrl());
    }

    @Test
    public void testSetImageURL() {
        Book book = mockBook();
        book.setImageUrl("imageurl.com");
        assertEquals("imageurl.com", book.getImageUrl());
    }

}
