package com.example.addressbook.controller;

import com.example.addressbook.model.Contact;
import com.example.addressbook.model.Group;
import com.example.addressbook.util.AlertManager;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import org.controlsfx.control.CheckComboBox;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class ContactFormController implements Initializable {
    private boolean editMode = false;
    private CheckComboBox<Group> comboBox;
    private Contact contact;
    private List<Group> groups = new ArrayList<>();
    private List<Integer> groupsSelected = new ArrayList<>();
    @FXML private TextField nameField;
    @FXML private TextField numberField;
    @FXML private TextField emailField;
    @FXML private TextField addressField;
    @FXML private VBox comboBoxHolder;

    public void initialize(URL url, ResourceBundle rb) {
        try {
            groups = ViewManager.getDbHelper().getGroups();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        comboBox = new CheckComboBox<>(
                FXCollections.observableArrayList(groups));
        comboBoxHolder.getChildren().add(comboBox);
        comboBox.setId("groupsComboBox");

    }

    public void setContactToEdit(Contact contact) {
        this.contact = contact;
        this.editMode = true;
        nameField.setText(contact.getName());
        numberField.setText(contact.getNumber());
        emailField.setText(contact.getEmail());
        addressField.setText(contact.getAddress());
        loadGroups();
    }

    private void loadGroups(){
        for (Group group : groups){
            if(contact.getGroups().contains(group.getId())){
                comboBox.getCheckModel().check(group);
            }
        }
    }

    @FXML
    private void submitContactForm() {
        String name = nameField.getText();
        String num = numberField.getText();
        String email = emailField.getText();
        String address = addressField.getText();
        Contact newContact = null;

        groupsSelected = comboBox.getCheckModel().getCheckedItems().stream()
                .map(group -> group.getId())
                .collect(Collectors.toList());
        try {
            newContact = new Contact(name, num, email, address);
        } catch (IllegalArgumentException e) {
            AlertManager.createErrorAlert(e.getMessage(), "DataValidation Error");
            e.printStackTrace();
        }
        try {
            if (editMode){
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
            } else {
                int newId = ViewManager.getDbHelper().save(newContact);
                ViewManager.getDbHelper().save(newId, groupsSelected);
                resetPage();
            }
            ViewManager.refreshContactsScene();
            ViewManager.contactsView();

        } catch (SQLException e) {
            AlertManager.generateSaveDataError(e, newContact);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void resetPage() {
        nameField.setText("");
        numberField.setText("");
        emailField.setText("");
        addressField.setText("");
        comboBox.getCheckModel().clearChecks();
    }
}
