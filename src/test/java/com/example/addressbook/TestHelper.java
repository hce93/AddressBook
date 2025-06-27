package com.example.addressbook;

import com.example.addressbook.model.Contact;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Properties;

public class TestHelper {

    private static Properties loadDbProperties() throws IOException {
        Properties props = new Properties();
        try(FileInputStream fis = new FileInputStream("config/test.properties")){
            props.load(fis);
        }
        return props;
    }

    public static Connection setupDatabaseConnection() throws Exception{
        Properties dbProps = loadDbProperties();
        String url = dbProps.getProperty("db.url");
        String username = dbProps.getProperty("db.user");
        String password = dbProps.getProperty("db.password");

        Connection conn = DriverManager.getConnection(
                url, username, password);
        conn.setAutoCommit(false);
        return conn;
    }
    public static Contact getContactForTesting(String SQLStatement, Connection conn) throws Exception{

        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(SQLStatement);
        String returnedName = "";
        String returnedNumber = "";
        String returnedEmail = "";
        String returnedAddress = "";
        if (rs.next()){
            returnedName = rs.getString("name");
            returnedNumber = rs.getString("number");
            returnedEmail = rs.getString("email");
            returnedAddress = rs.getString("address");
        }
        Contact newContact = new Contact(returnedName, returnedNumber, returnedEmail, returnedAddress);
        return newContact;
    }

    public static Integer getContactGroupForTesting(String SQLStatement, Connection conn) throws Exception {
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(SQLStatement);
        Integer group_id = -1;
        if (rs.next()){
            group_id = rs.getInt("contact_groups_id");
        }
        return group_id;
    }

}
