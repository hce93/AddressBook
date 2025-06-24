package com.example.addressbook.controller;

import com.example.addressbook.model.Contact;
import com.example.addressbook.model.Group;
import com.example.addressbook.util.AlertManager;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.TilePane;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class EditContactController implements Initializable {
    private Contact contact;
    private List<Group> groups = new ArrayList<>();
    private List<Integer> groupsSelected = new ArrayList<>();
    @FXML private TextField nameField;
    @FXML private TextField numberField;
    @FXML private TextField emailField;
    @FXML private TextField addressField;
    @FXML private TilePane groupsTilePane;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {}

    public void setContactToEdit(Contact contact){
        this.contact = contact;
        nameField.setText(contact.getName());
        numberField.setText(contact.getNumber());
        emailField.setText(contact.getEmail());
        addressField.setText(contact.getAddress());

        loadGroups();

    }

    private void loadGroups(){
        groupsTilePane.getChildren().clear();
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

            groupsTilePane.getChildren().add(c);
        }
    }

    @FXML
    private void submitEditContactForm(){
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
            ViewManager.refreshContactsScene();
            ViewManager.contactsView();

        } catch (SQLException e){
            AlertManager.generateSaveDataError(e, newContact);
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
