package com.example.addressbook.view;

import com.example.addressbook.controller.ViewManager;
import com.example.addressbook.model.Group;
import com.example.addressbook.util.AlertManager;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class EditGroupView implements Initializable {
    private Group group;
    @FXML private TextField nameField;

    public void initialize(URL url, ResourceBundle rb) {}

    public void setGroupToEdit(Group group){
        this.group = group;
        this.nameField.setText(group.getName());
    }

    @FXML
    private void submitEditGroupForm(){
        String name = nameField.getText();
        try {
            ViewManager.getDbHelper().editGroup(group.getId(), name);
            ViewManager.refreshGroupsScene();
            ViewManager.refreshContactsScene();
            ViewManager.refreshAddContactsScene();
            ViewManager.groupsView();
        } catch (SQLException e){
            AlertManager.createErrorAlert("Error editing group: "+group.getName(), "Error Saving Group");
            e.printStackTrace();
        } catch (IOException e){
            e.printStackTrace();
        }
    }
}
