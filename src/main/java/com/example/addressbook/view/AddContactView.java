package com.example.addressbook.view;


import com.example.addressbook.controller.ViewManager;
import com.example.addressbook.model.Contact;
import com.example.addressbook.model.Group;
import com.example.addressbook.util.AlertManager;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.TilePane;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class AddContactView implements Initializable {
    private List<Group> groups = new ArrayList<>();
    private List<Integer> groupsSelected = new ArrayList<>();

    @FXML
    private TextField nameField;
    @FXML
    private TextField numberField;
    @FXML
    private TextField emailField;
    @FXML
    private TextField addressField;
    @FXML
    private TilePane groupsTilePane = new TilePane();

    public void initialize(URL url, ResourceBundle rb) {
        loadGroupCheckboxes();
    }

    private void loadGroupCheckboxes() {
        try {
            groups = ViewManager.getDbHelper().getGroups();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        for (Group group : groups) {
            CheckBox c = new CheckBox(group.getName());
            c.setOnAction(selected -> {
                if (c.isSelected()) {
                    groupsSelected.add(group.getId());
                } else {
                    groupsSelected.remove(Integer.valueOf(group.getId()));
                }
            });
            groupsTilePane.getChildren().add(c);
        }
    }

    @FXML
    private void submitAddContactForm() {
        String name = nameField.getText();
        String num = numberField.getText();
        String email = emailField.getText();
        String address = addressField.getText();

        Contact contact = null;
        try {
            contact = new Contact(name, num, email, address);
        } catch (IllegalArgumentException e) {
            AlertManager.createErrorAlert(e.getMessage(), "DataValidation Error");
            e.printStackTrace();
        }
        try {
            int newId = ViewManager.getDbHelper().save(contact);
            ViewManager.getDbHelper().save(newId, groupsSelected);
            resetPage();
            ViewManager.refreshContactsScene();
            ViewManager.contactsView();
        } catch (SQLException e) {
            AlertManager.generateSaveDataError(e, contact);
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
        for (Node node : groupsTilePane.getChildren()) {
            CheckBox checkBox = (CheckBox) node;
            checkBox.setSelected(false);
        }
    }
}


