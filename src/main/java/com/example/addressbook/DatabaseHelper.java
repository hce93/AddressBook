package com.example.addressbook;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper {

    public void save(Contact contact) throws SQLException {
        String sql = "INSERT INTO address (name, number, email, address) VALUES (?, ?, ?, ?)";

        try(PreparedStatement stmt = ViewManager.getConnection().prepareStatement(sql)){
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
        try(Statement stmt = ViewManager.getConnection().createStatement()){
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

    public boolean deleteContact(Contact contact) throws SQLException {
        String sql = String.format("DELETE FROM address WHERE name='%s';", contact.getName());
        try(Statement stmt = ViewManager.getConnection().createStatement()){
            Integer rowsAffected =  stmt.executeUpdate(sql);
            return rowsAffected > 0;
        }
    }

    public boolean editContact(String initialName, Contact updatedContact) throws SQLException {
        String sql = String.format(
                "UPDATE address SET name='%s', number='%s', email='%s', address='%s' WHERE name='%s'",
                updatedContact.getName(),
                updatedContact.getNumber(),
                updatedContact.getEmail(),
                updatedContact.getAddress(),
                initialName);
        try(Statement stmt = ViewManager.getConnection().createStatement()){
            Integer rowsAffected = stmt.executeUpdate(sql);
            return rowsAffected > 0;
        }
    }
}
