package com.example.addressbook.view;

import com.example.addressbook.model.Group;
import com.example.addressbook.util.AlertManager;
import com.example.addressbook.model.Contact;
import com.example.addressbook.controller.ViewManager;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EditContactView {
    private Contact contact;
    private VBox layout;
    private List<Group> groups = new ArrayList<>();
    private List<Integer> groupsSelected = new ArrayList<>();

    public EditContactView(Contact contact) {
        this.contact = contact;

        layout = new VBox(10);
        layout.setPadding(new Insets(20));

        TextField nameField = new TextField();
        nameField.setText(contact.getName());
        nameField.setPromptText("Enter name");

        TextField numberField = new TextField();
        numberField.setText(contact.getNumber());
        numberField.setPromptText("Enter number");

        TextField emailField = new TextField();
        emailField.setText(contact.getEmail());
        emailField.setPromptText("Enter email");

        TextField addressField = new TextField();
        addressField.setText(contact.getAddress());
        addressField.setPromptText("Enter address");

        //checkboxes for groups
        TilePane t = new TilePane();

        try {
            groups = ViewManager.getDbHelper().getGroups();
        } catch(SQLException e){
            e.printStackTrace();
        }

        for (Group group : groups){
            CheckBox c = new CheckBox(group.getName());
            if(contact.getGroups().contains(group.getId())){
                c.setSelected(true);
                groupsSelected.add(group.getId());
            }
            c.setOnAction(selected -> {
                if (c.isSelected()){
                    groupsSelected.add(group.getId());
                } else {
                    groupsSelected.remove(Integer.valueOf(group.getId()));
                }
            });

            t.getChildren().add(c);
        }

        Button submitButton = new Button("Submit");
        submitButton.setDefaultButton(true);
        submitButton.setOnAction(submitted -> {
            String name = nameField.getText();
            String num = numberField.getText();
            String email = emailField.getText();
            String address = addressField.getText();
            Contact newContact = null;
            try{
                newContact = new Contact(name, num, email, address);
            } catch (IllegalArgumentException e){
                AlertManager.createErrorAlert(e.getMessage(), "DataValidation Error");
            }
            try{
                ViewManager.getDbHelper().editContact(this.contact.getName(), newContact);
                // update group selection
                List<Integer> groupsToAdd = new ArrayList<>(groupsSelected);
                List<Integer> groupsToDelete = new ArrayList<>(contact.getGroups());
                groupsToAdd.removeAll(contact.getGroups());
                groupsToDelete.removeAll(groupsSelected);
                if(!groupsToAdd.isEmpty()) {
                    ViewManager.getDbHelper().save(contact.getId(), groupsToAdd);
                }
                if(!groupsToDelete.isEmpty()){
                    ViewManager.getDbHelper().deleteContactGroup(contact.getId(), groupsToDelete);
                }
                ViewManager.refreshContactsView();
                ViewManager.contactsView();

            } catch (SQLException e){
                AlertManager.generateSaveDataError(e, newContact);
                e.printStackTrace();
            }
        });

        layout.getChildren().addAll(
                new Label("Name:"), nameField,
                new Label("Number:"), numberField,
                new Label("Email:"), emailField,
                new Label("Address:"), addressField,
                t,
                submitButton
        );
    }

    public VBox getView() {
        return layout;
    }
}
