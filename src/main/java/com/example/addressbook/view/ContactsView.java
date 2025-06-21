package com.example.addressbook.view;

import com.example.addressbook.controller.ViewManager;
import com.example.addressbook.model.Contact;
import com.example.addressbook.model.Group;
import com.example.addressbook.util.AlertManager;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class ContactsView {

    private List<Contact> contacts;
    private List<Contact> searchedContacts;
    private List<Contact> filteredContacts;
    private List<Contact> displayContacts;
    private VBox layout;
    private VBox contactsBox = new VBox();
    private List<Integer> filtersSelected = new ArrayList<>();
    private TextField searchField;
    public ContactsView(){

        try {
            contacts = ViewManager.getDbHelper().getContacts().stream()
                    .sorted(Comparator.comparing(Contact::getName))
                    .collect(Collectors.toList());
            searchedContacts = new ArrayList<>(contacts);
            filteredContacts = new ArrayList<>(contacts);

        } catch (SQLException e){
            e.printStackTrace();
        }

        layout = new VBox(10);
        layout.setPadding(new Insets(20));

        buildContacts();
        layout.getChildren().addAll(getSearchBar(), getSortButton(), getFilters(), contactsBox);
    }

    private ToggleButton getSortButton(){
        ToggleButton sortButton = new ToggleButton("A->Z");
        sortButton.setOnAction(pressed -> {
            Collections.reverse(contacts);
            Collections.reverse(filteredContacts);
            Collections.reverse(searchedContacts);
            if (sortButton.isSelected()) {
                sortButton.setText("Z->A");
            } else {
                sortButton.setText("A->Z");
            }
            buildContacts();
        });
        return sortButton;
    }

    private HBox getSearchBar(){
        HBox searchBox = new HBox();
        searchField = new TextField();
        Button searchButton = new Button("Search");
        searchButton.setOnAction(search -> {
            searchContacts();
        });
        searchButton.setDefaultButton(true);
        searchBox.getChildren().addAll(searchField, searchButton);
        return searchBox;
    }

    private void searchContacts(){
        String searchText = searchField.getText();
        if (!searchText.isBlank()) {
            Pattern pattern = Pattern.compile(searchText, Pattern.CASE_INSENSITIVE);
            searchedContacts = contacts.stream()
                    .filter(contact -> pattern.matcher(contact.getName()).find())
                    .collect(Collectors.toList());
        } else {
            searchedContacts=new ArrayList<>(contacts);
        }
        buildContacts();
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
                filterContacts();
            });
            filters.getChildren().add(b);
        }
        return filters;
    }

    private void filterContacts(){
        if (filtersSelected.isEmpty()) {
            filteredContacts = new ArrayList<>(contacts);
        } else {
            filteredContacts = contacts.stream()
                    .filter(contact -> contact.getGroups()
                            .stream()
                            .anyMatch(id -> filtersSelected.contains(id)))
                    .collect(Collectors.toList());
        }
        buildContacts();
    }

    private void updateDisplayContacts(){
        displayContacts = contacts.stream()
                .filter(contact -> filteredContacts.contains(contact) &&
                        searchedContacts.contains(contact))
                .collect(Collectors.toList());
    }


    private void buildContacts(){
        updateDisplayContacts();
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
                            contacts.remove(contact);
                            filteredContacts.removeIf(contact1 -> (contact1==contact));
                            searchedContacts.removeIf(contact1 -> (contact1==contact));
                            buildContacts();
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
