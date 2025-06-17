package com.example.addressbook;

import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
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
        buildContacts();
    }

    private void buildContacts(){
        for (Contact contact : contacts) {
            VBox newContact = new VBox();
            Label name = new Label(contact.getName());
            Label number = new Label(contact.getNumber());
            Label email = new Label(contact.getEmail());
            Label address = new Label(contact.getAddress());
            Button btn = new Button("Delete");
            btn.setOnAction(actionEvent -> {
                try {
                    if (dbHelper.deleteContact(contact)) {
                        layout.getChildren().clear();
                        contacts.remove(contact);
                        buildContacts();

                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText("Unable to delete user: " + name);
                    alert.setTitle("Database Error");
                    alert.showAndWait();
                }

            });
            newContact.getChildren().addAll(name, number, email, address, btn);
            layout.getChildren().add(newContact);
        }
    }

    public VBox getView() {
        return layout;
    }
}
