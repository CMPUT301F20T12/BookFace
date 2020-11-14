package com.example.bookface;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * This is a class to test the getters and setters of the object class User
 */
public class UserTest {
    private User mockUser() {
        return new User("username", "username@ualberta.ca", "000-000-0000");
    }

    @Test
    public void testGetUsername() {
        User user = mockUser();
        assertEquals("username", user.getUsername());
    }

    @Test
    public void testSetUsername() {
        User user = mockUser();
        user.setUsername("username2");
        assertEquals("username2", user.getUsername());
    }

    @Test
    public void testGetEmail() {
        User user = mockUser();
        assertEquals("username@ualberta.ca", user.getEmail());
    }

    @Test
    public void testSetEmail() {
        User user = mockUser();
        user.setEmail("username2@ualberta.ca");
        assertEquals("username2@ualberta.ca", user.getEmail());
    }

    @Test
    public void testGetContactNo() {
        User user = mockUser();
        assertEquals("000-000-0000", user.getContactNo());
    }

    @Test
    public void testSetContactNo() {
        User user = mockUser();
        user.setContactNo("000-000-0001");
        assertEquals("000-000-0001", user.getContactNo());
    }


}
