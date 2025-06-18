package com.example.addressbook.view;

import com.example.addressbook.util.AlertManager;
import com.example.addressbook.model.Contact;
import com.example.addressbook.controller.ViewManager;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.sql.SQLException;
import java.util.List;

public class ContactsView {

    private List<Contact> contacts;
    private VBox layout;
    public ContactsView(){

        try {
            contacts = ViewManager.getDbHelper().getContacts();
        } catch (SQLException e){
            e.printStackTrace();
        }

        layout = new VBox(10);
        layout.setPadding(new Insets(20));
        buildContacts();
    }

    private void buildContacts(){

        if (contacts.isEmpty()){
            Label emptyLabel = new Label("You have no contacts");
            layout.getChildren().add(emptyLabel);
        } else {
            for (Contact contact : contacts) {
                VBox newContact = new VBox();
                Label name = new Label(contact.getName());
                Label number = new Label(contact.getNumber());
                Label email = new Label(contact.getEmail());
                Label address = new Label(contact.getAddress());

                Button deleteButton = new Button("Delete");
                deleteButton.setOnAction(deleteEvent -> {
                    try {
                        if (ViewManager.getDbHelper().deleteContact(contact)) {
                            ViewManager.contactsView();
                        }
                    } catch (SQLException e) {
                        AlertManager.createErrorAlert(
                                "Unable to delete user: " + name, "Database Error");
                        e.printStackTrace();
                    }
                });

                Button editButton = new Button("Edit");
                editButton.setOnAction(editEvent -> {
                    ViewManager.editContactsView(contact);
                });

                newContact.getChildren().addAll(name, number, email, address, editButton, deleteButton);
                layout.getChildren().add(newContact);
            }
        }
    }

    public VBox getView() {
        return layout;
    }
}
