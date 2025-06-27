package com.example.addressbook.util;

import com.example.addressbook.TestHelper;
import com.example.addressbook.controller.ViewManager;
import com.example.addressbook.model.Contact;
import com.example.addressbook.model.Group;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.sql.Connection;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;

public class DatabaseHelperTest {
    private static Connection conn;
    private static String contactName = "Test Contact";
    private static String contactNumber = "09124356879";
    private static String contactEmail = "test@test.com";
    private static String contactAddress = "Test Address";
    private static String groupName = "Test Group";

    @BeforeClass
    public static void setup() throws Exception{
        conn = TestHelper.setupDatabaseConnection();
        ViewManager.setConn(conn);
    }
    @Test
    public void checkContactSave() throws Exception{
        Contact newContact = new Contact(contactName, contactNumber, contactEmail, contactAddress);
        ViewManager.getDbHelper().save(newContact);

        String sql = "SELECT * FROM address WHERE name = 'Test Contact'";
        Contact dbContact = TestHelper.getContactForTesting(sql, conn);

        assertEquals(contactName, dbContact.getName());
        assertEquals(contactNumber, dbContact.getNumber());
        assertEquals(contactEmail, dbContact.getEmail());
        assertEquals(contactAddress, dbContact.getAddress());
    }

    @Test
    public void checkEditContact() throws Exception {
        Contact newContact = new Contact(contactName, contactNumber, contactEmail, contactAddress);
        ViewManager.getDbHelper().save(newContact);

        String updatedName = "Test Contact 2";
        Contact updatedContact = new Contact(updatedName, contactNumber, contactEmail, contactAddress);
        ViewManager.getDbHelper().editContact(contactName, updatedContact);

        String sql = "SELECT * FROM address WHERE name = 'Test Contact 2'";
        Contact dbContact = TestHelper.getContactForTesting(sql, conn);
        assertEquals(updatedName, dbContact.getName());
    }

    @Test(expected = IllegalArgumentException.class)
    public void checkDeleteContact() throws Exception {
//        illegal argument expected when the getContactForTesting method tries to create a contact with blank parameters
        Contact newContact = new Contact(contactName, contactNumber, contactEmail, contactAddress);
        ViewManager.getDbHelper().save(newContact);

        ViewManager.getDbHelper().deleteContact(newContact);

        String sql = "SELECT * FROM address WHERE name = 'Test Contact'";
        Contact dbContact = TestHelper.getContactForTesting(sql, conn);
    }

    @Test
    public void addGroupToContactTest() throws Exception{
        Contact newContact = new Contact(contactName, contactNumber, contactEmail, contactAddress);
        Group newGroup = new Group(groupName);
        Integer contactID = ViewManager.getDbHelper().save(newContact);
        Integer groupID = ViewManager.getDbHelper().save(newGroup);
        ViewManager.getDbHelper().save(contactID, Arrays.asList(groupID));

        String sql = String.format("SELECT * FROM address_contact_groups WHERE address_id = %s", contactID);
        Integer dbGroupID = TestHelper.getContactGroupForTesting(sql, conn);
        assertEquals(groupID, dbGroupID);
    }

    @After
    public void rollbackAfterEachTest() throws Exception {
        conn.rollback();
    }

    @AfterClass
    public static void tearDown() throws Exception {
        conn.close();
    }

}