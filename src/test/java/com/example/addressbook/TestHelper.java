package com.example.addressbook;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
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

}
