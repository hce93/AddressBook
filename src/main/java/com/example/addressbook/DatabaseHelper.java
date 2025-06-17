package com.example.addressbook;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper {

    private Connection connection;

    public DatabaseHelper(Connection connection){
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

    public List<Contact> getContacts() throws SQLException{
        List<Contact> contactList = new ArrayList<>();

        String sql = "SELECT * FROM address";
        try(Statement stmt = connection.createStatement()){
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()){
                contactList.add(new Contact(
                        rs.getString("name"),
                        rs.getString("number"),
                        rs.getString("email"),
                        rs.getString("address")
                ));
            }


        }
        return contactList;
    }
}
