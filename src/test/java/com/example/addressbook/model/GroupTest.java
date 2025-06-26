package com.example.addressbook.model;

import static org.junit.jupiter.api.Assertions.*;

public class GroupTest {

    private Group group;

    @org.junit.Before
    public void setup() {
        group = new Group("Test Group");
    }

    @org.junit.Test
    public void checkName() throws Exception{
        assertEquals("Test Group", group.getName());
    }
}