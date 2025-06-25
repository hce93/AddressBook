package com.example.addressbook.controller;

import com.example.addressbook.model.Contact;
import com.example.addressbook.model.Group;
import com.example.addressbook.util.AlertManager;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class ContactsController implements Initializable {

    private List<Contact> contacts;
    private List<Contact> searchedContacts;
    private List<Contact> filteredContacts;
    private List<Contact> displayContacts;
    private List<Integer> filtersSelected = new ArrayList<>();
    @FXML private VBox contactsBox;
    @FXML private ToggleButton sortButton;
    @FXML private HBox filtersBox;
    @FXML private TextField searchField;
    public void initialize(URL url, ResourceBundle rb){

        try {
            contacts = ViewManager.getDbHelper().getContacts().stream()
                    .sorted(Comparator.comparing(contact->contact.getName().toLowerCase()))
                    .collect(Collectors.toList());
            searchedContacts = new ArrayList<>(contacts);
            filteredContacts = new ArrayList<>(contacts);

        } catch (SQLException e){
            e.printStackTrace();
        }

        buildContacts();
        getFilters();
    }

    @FXML private void onSortClicked(){
        Collections.reverse(contacts);
        Collections.reverse(filteredContacts);
        Collections.reverse(searchedContacts);
        if (sortButton.isSelected()) {
            sortButton.setText("Z->A");
        } else {
            sortButton.setText("A->Z");
        }
        buildContacts();
    }

    @FXML private void searchContacts(){
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
        filtersBox.getChildren().clear();
        List<Group> groups = new ArrayList<>();
        try {
            groups = ViewManager.getDbHelper().getGroups();
        } catch (SQLException e){
            e.printStackTrace();
        }

        for (Group group : groups){
            ToggleButton b = new ToggleButton(group.getName());
            b.prefHeightProperty().bind(sortButton.heightProperty());
            b.minHeightProperty().bind(sortButton.heightProperty());
            b.maxHeightProperty().bind(sortButton.heightProperty());
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
            filtersBox.getChildren().add(b);
        }
        return filtersBox;
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
                newContact.setId("contactCard");
                Label name = new Label(contact.getName());
                name.setId("contactName");
                Label number = new Label(contact.getNumber());
                Label email = new Label(contact.getEmail());
                Label address = new Label(contact.getAddress());

                HBox buttonsHolder = new HBox();
                buttonsHolder.setId("contactsButtonHolder");
                buttonsHolder.setSpacing(5);

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
                    try{
                        ViewManager.editContactsView(contact);
                    } catch (IOException e){
                        e.printStackTrace();
                    }

                });
                buttonsHolder.getChildren().addAll(deleteButton, editButton);
                newContact.getChildren().addAll(name, number, email, address, buttonsHolder);
                contactsBox.getChildren().add(newContact);
            }
        }
    }
}
