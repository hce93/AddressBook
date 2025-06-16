package com.example.addressbook;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ContactHelper {

    private Connection connection;

    public ContactHelper(Connection connection){
        this.connection = connection;
    }

    public void save(Contact contact) throws SQLException {
        String sql = "INSERT INTO address (name, number, email, address) VALUES (?, ?, ?, ?)";

        try(PreparedStatement stmt = connection.prepareStatement(sql)){
            stmt.setString(1, contact.getName());
            stmt.setString(2, contact.getNumber().toString());
            stmt.setString(3, contact.getEmail());
            stmt.setString(4, contact.getAddress());
            stmt.executeUpdate();
        }
    }
}
