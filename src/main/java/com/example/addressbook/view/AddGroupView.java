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

public class AddGroupView {

    private VBox layout;

    public AddGroupView(){

        layout = new VBox(10);
        layout.setPadding(new Insets(20));

        TextField nameField = new TextField();
        nameField.setPromptText("Enter name");

        Button submitButton = new Button("Submit");
        submitButton.setDefaultButton(true);

        submitButton.setOnAction(submitted -> {
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
                ViewManager.groupsView();
            } catch (SQLException e) {
                AlertManager.generateSaveDataError(e, group);
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
