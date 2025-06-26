package com.example.addressbook.model;

import static org.junit.jupiter.api.Assertions.*;

public class ContactTest {

    private Contact contact;

    @org.junit.Before
    public void setup() {
        contact = new Contact("Test Contact", "07123456789", "test@test.com", "Test address");
    }

    @org.junit.Test
    public void checkName() throws Exception{
        assertEquals("Test Contact", contact.getName());
    }

    @org.junit.Test
    public void checkNum() throws Exception{
        assertEquals("07123456789", contact.getNumber());
    }

    @org.junit.Test
    public void checkEmail() throws Exception{
        assertEquals("test@test.com", contact.getEmail());
    }

    @org.junit.Test
    public void checkAddress() throws Exception{
        assertEquals("Test address", contact.getAddress());
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void checkEmailValidation() throws Exception{
        Contact testContact = new Contact("Test", "07123456789", "bad-email", "Test Address");
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void checkNumberValidation() throws Exception{
        Contact testContact = new Contact("Test", "1234", "test@test.com", "Test Address");
    }
}