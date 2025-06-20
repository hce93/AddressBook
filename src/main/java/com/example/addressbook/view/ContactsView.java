package com.example.addressbook.view;

import com.example.addressbook.controller.ViewManager;
import com.example.addressbook.model.Contact;
import com.example.addressbook.model.Group;
import com.example.addressbook.util.AlertManager;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ContactsView {

    private List<Contact> contacts;
    private List<Contact> displayContacts;
    private VBox layout;
    private VBox contactsBox = new VBox();
    private Integer filtersSelectedCount = 0;
    private List<Integer> filtersSelected = new ArrayList<>();
    public ContactsView(){

        try {
            contacts = ViewManager.getDbHelper().getContacts();
            displayContacts = new ArrayList<>(contacts);
        } catch (SQLException e){
            e.printStackTrace();
        }

        layout = new VBox(10);
        layout.setPadding(new Insets(20));

        buildContacts();
        layout.getChildren().addAll(getFilters(), contactsBox);
    }

    private HBox getFilters(){
        List<Group> groups = new ArrayList<>();
        try {
            groups = ViewManager.getDbHelper().getGroups();
        } catch (SQLException e){
            e.printStackTrace();
        }

        HBox filters = new HBox();
        for (Group group : groups){
            ToggleButton b = new ToggleButton(group.getName());

            b.setOnAction(pressed -> {
                if(b.isSelected()){
                    // add filter
                    filtersSelected.add(group.getId());
                } else{
                    // remove filter
                    filtersSelected.remove(Integer.valueOf(group.getId()));
                }

                if (filtersSelected.isEmpty()){
                    displayContacts=contacts;
                } else {
                    displayContacts = contacts.stream()
                            .filter(contact -> contact.getGroups()
                                    .stream()
                                    .anyMatch(id -> filtersSelected.contains(id)))
                            .collect(Collectors.toList());

                }
                buildContacts();
            });
            filters.getChildren().add(b);
        }
        return filters;
    }

    private void buildContacts(){
        contactsBox.getChildren().clear();
        if (displayContacts.isEmpty()){
            Label emptyLabel = new Label("You have no contacts");
            contactsBox.getChildren().add(emptyLabel);
        } else {
            for (Contact contact : displayContacts) {
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
                contactsBox.getChildren().add(newContact);
            }
        }
    }

    public VBox getView() {
        return layout;
    }
}
