package com.example.addressbook.util;

import com.example.addressbook.controller.ViewManager;
import com.example.addressbook.model.Contact;
import com.example.addressbook.model.Group;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper {

    public Integer save(Contact contact) throws SQLException {
        String sql = "INSERT INTO address (name, number, email, address) VALUES (?, ?, ?, ?)";
        Integer id=-1;
        try(PreparedStatement stmt = ViewManager.getConnection().prepareStatement(
                sql,Statement.RETURN_GENERATED_KEYS)){
            stmt.setString(1, contact.getName());
            stmt.setString(2, contact.getNumber().toString());
            stmt.setString(3, contact.getEmail());
            stmt.setString(4, contact.getAddress());
            stmt.executeUpdate();

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()){
                if (generatedKeys.next()){
                    id = generatedKeys.getInt(1);
                }
            }
        }
        return id;
    }

    public Integer save(Group group) throws SQLException {
        Integer id = -1;
        String sql = String.format(
                "INSERT INTO contact_groups (name) VALUES ('%s')",
                group.getName());

        try(PreparedStatement stmt = ViewManager.getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)){
            stmt.executeUpdate();
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()){
                if (generatedKeys.next()){
                    id = generatedKeys.getInt(1);
                }
            }
        }
        return id;
    }

    public void save(Integer contact_id, List<Integer> group_ids) throws SQLException{
        for (Integer group_id: group_ids){
            String sql = String.format(
                    "INSERT INTO address_contact_groups (address_id, contact_groups_id) VALUES (%s, %s)",
                    contact_id, group_id);
            try(PreparedStatement stmt =
                        ViewManager.getConnection().prepareStatement(sql)){
                stmt.executeUpdate();
            }
        }
    }

    public List<Contact> getContacts() throws SQLException{
        List<Contact> contactList = new ArrayList<>();
        String sql = "SELECT a.*,GROUP_CONCAT(ag.contact_groups_id SEPARATOR ',') AS contact_groups\n" +
                "FROM address_book.address a\n" +
                "LEFT JOIN address_book.address_contact_groups ag ON a.id = ag.address_id\n" +
                "GROUP BY  a.id;";
        try(Statement stmt = ViewManager.getConnection().createStatement()){
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()){
                Contact newContact = new Contact(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("number"),
                        rs.getString("email"),
                        rs.getString("address"));
                String group_ids = rs.getString("contact_groups");
                if (group_ids!=null && !group_ids.isEmpty()){
                    String[] groupsArray = group_ids.split(",");
                    for(String id : groupsArray){
                        newContact.addGroup(Integer.parseInt(id));
                    }
                }

                contactList.add(newContact);
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

    public List<Group> getGroups() throws SQLException{
        List<Group> groupList = new ArrayList<>();

        String sql = "SELECT * FROM contact_groups";
        try(Statement stmt = ViewManager.getConnection().createStatement()){
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()){
                groupList.add(new Group(
                        rs.getInt("id"),
                        rs.getString("name")
                ));
            }
        }
        return groupList;
    }

    public boolean deleteGroup(Integer id) throws SQLException {
        String sql = String.format("DELETE FROM contact_groups WHERE id='%s';", id);
        try(Statement stmt = ViewManager.getConnection().createStatement()){
            Integer rowsAffected =  stmt.executeUpdate(sql);
            return rowsAffected > 0;
        }
    }

    public boolean editGroup(Integer id, String newName) throws SQLException{
        String sql = String.format(
                "UPDATE contact_groups SET name='%s' WHERE id=%s",
                newName,
                id);
        try(Statement stmt = ViewManager.getConnection().createStatement()){
            Integer rowsAffected = stmt.executeUpdate(sql);
            return rowsAffected > 0;
        }
    }



    public void deleteContactGroup(Integer contact_id, List<Integer> group_ids) throws SQLException{
        for (Integer group : group_ids){
            String sql = String.format("DELETE FROM address_contact_groups " +
                    "WHERE address_id=%s AND contact_groups_id=%s;",
                    contact_id, group);
            try(Statement stmt = ViewManager.getConnection().createStatement()){
                stmt.executeUpdate(sql);
            }
        }
    }



}
