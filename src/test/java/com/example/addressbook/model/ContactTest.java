package com.example.addressbook.model;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ContactTest {

    private Contact contact;

    @Before
    public void setup() {
        contact = new Contact("Test Contact", "07123456789", "test@test.com", "Test address");
    }

    @Test
    public void checkName() throws Exception{
        assertEquals("Test Contact", contact.getName());
    }

    @Test
    public void checkNum() throws Exception{
        assertEquals("07123456789", contact.getNumber());
    }

    @Test
    public void checkEmail() throws Exception{
        assertEquals("test@test.com", contact.getEmail());
    }

    @Test
    public void checkAddress() throws Exception{
        assertEquals("Test address", contact.getAddress());
    }

    @Test(expected = IllegalArgumentException.class)
    public void checkEmailValidation() throws Exception{
        Contact testContact = new Contact("Test", "07123456789", "bad-email", "Test Address");
    }

    @Test(expected = IllegalArgumentException.class)
    public void checkNumberValidation() throws Exception{
        Contact testContact = new Contact("Test", "1234", "test@test.com", "Test Address");
    }
}