package com.example.addressbook.view;

import com.example.addressbook.controller.ViewManager;
import com.example.addressbook.model.Group;
import com.example.addressbook.util.AlertManager;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

import java.sql.SQLException;

public class EditGroupView {
    private Group group;
    private VBox layout;

    public EditGroupView(Group group){
        this.group = group;

        layout = new VBox(10);
        layout.setPadding(new Insets(20));

        TextField nameField = new TextField();
        nameField.setText(group.getName());
        nameField.setPromptText("Enter name");

        Button submitButton = new Button("Submit");
        submitButton.setDefaultButton(true);
        submitButton.setOnAction(submitted -> {
            String name = nameField.getText();
            try {
                ViewManager.getDbHelper().editGroup(group.getId(), name);
                ViewManager.refreshGroupsView();
                ViewManager.refreshContactsView();
                ViewManager.refreshAddContactsView();
                ViewManager.groupsView();
            } catch (SQLException e){
                AlertManager.createErrorAlert("Error editing group: "+group.getName(), "Error Saving Group");
                e.printStackTrace();
            }
        });

        layout.getChildren().addAll(
                new Label("Name:"), nameField,
                submitButton
        );


    }

    public VBox getView() {
        return layout;
    }


}
