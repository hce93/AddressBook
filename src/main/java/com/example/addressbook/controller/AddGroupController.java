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

public class AddGroupController implements Initializable {

    @FXML private TextField nameField;

    public void initialize(URL url, ResourceBundle rb) {}

    @FXML
    private void submitAddGroupForm(){
        String name = nameField.getText();

        Group group = null;
        try{
            group = new Group(name);
        } catch (IllegalArgumentException e){
            AlertManager.createErrorAlert(e.getMessage(), "DataValidation Error");
            e.printStackTrace();
        }
        try {
            ViewManager.getDbHelper().save(group);
            nameField.setText("");
            ViewManager.refreshContactsScene();
            ViewManager.refreshAddContactsScene();
            ViewManager.refreshGroupsScene();
            ViewManager.groupsView();
        } catch (SQLException e) {
            AlertManager.generateSaveDataError(e, group);
        } catch (IOException e){
            e.printStackTrace();
        }
    }
}
