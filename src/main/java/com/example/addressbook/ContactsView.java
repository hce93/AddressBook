package com.example.addressbook;

import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.sql.SQLException;
import java.util.List;

public class ContactsView {

    private DatabaseHelper dbHelper;
    private List<Contact> contacts;

    private VBox layout;
    public ContactsView(){
        this.dbHelper = new DatabaseHelper();

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

            Button deleteButton = new Button("Delete");
            deleteButton.setOnAction(deleteEvent -> {
                try {
                    if (dbHelper.deleteContact(contact)) {
                        ViewManager.contactsView();
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText("Unable to delete user: " + name);
                    alert.setTitle("Database Error");
                    alert.showAndWait();
                }
            });

            Button editButton = new Button("Edit");
            editButton.setOnAction(editEvent -> {
                ViewManager.editContactsView(contact);
            });

            newContact.getChildren().addAll(name, number, email, address, editButton,deleteButton);
            layout.getChildren().add(newContact);
        }
    }

    public VBox getView() {
        return layout;
    }
}
