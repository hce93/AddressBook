package com.example.addressbook;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class ContactsView {

    private Connection connection;
    private DatabaseHelper dbHelper;
    private List<Contact> contacts;


    private VBox layout;
    public ContactsView(Connection connection){
        this.connection=connection;
        this.dbHelper = new DatabaseHelper(connection);

        try {
            contacts = dbHelper.getContacts();
        } catch (SQLException e){
            e.printStackTrace();
        }


        layout = new VBox(10);
        layout.setPadding(new Insets(20));

        for (Contact contact : contacts){
            VBox newContact = new VBox();
            Label name = new Label(contact.getName());
            Label number = new Label(contact.getNumber());
            Label email = new Label(contact.getEmail());
            Label address = new Label(contact.getAddress());
            newContact.getChildren().addAll(name, number, email, address);
            layout.getChildren().add(newContact);
        }
        Label label = new Label("This si the contacts page");
        layout.getChildren().add(label);

    }

    public VBox getView() {
        return layout;
    }
}
