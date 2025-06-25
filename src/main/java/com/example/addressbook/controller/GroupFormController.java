package com.example.addressbook.controller;

import com.example.addressbook.model.Group;
import com.example.addressbook.util.AlertManager;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class GroupFormController implements Initializable {

    private boolean editMode=false;
    @FXML Group group;
    @FXML private TextField nameField;

    public void initialize(URL url, ResourceBundle rb) {}

    public void setGroupToEdit(Group group){
        this.group = group;
        this.nameField.setText(group.getName());
        this.editMode=true;
    }
    @FXML
    private void submitGroupForm(){
        String name = nameField.getText();
        if (editMode){
            try {
                ViewManager.getDbHelper().editGroup(group.getId(), name);
            } catch (SQLException e){
                AlertManager.createErrorAlert("Error editing group: "+group.getName(), "Error Saving Group");
                e.printStackTrace();
            }
        } else {
            Group group = null;
            try {
                group = new Group(name);
                ViewManager.getDbHelper().save(group);
                nameField.setText("");
            } catch (IllegalArgumentException e) {
                AlertManager.createErrorAlert(e.getMessage(), "DataValidation Error");
                e.printStackTrace();
            } catch (SQLException e) {
                AlertManager.generateSaveDataError(e, group);
            }
        }

        try {
            ViewManager.refreshContactsScene();
            ViewManager.refreshAddContactsScene();
            ViewManager.refreshGroupsScene();
            ViewManager.groupsView();
        } catch (IOException e){
            e.printStackTrace();
        }
    }
}
