package com.example.addressbook.view;

import com.example.addressbook.controller.ViewManager;
import com.example.addressbook.model.Contact;
import com.example.addressbook.model.Group;
import com.example.addressbook.util.AlertManager;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AddContactView {

    private VBox layout;
    private List<Group> groups = new ArrayList<>();
    private List<Integer> groupsSelected = new ArrayList<>();

    public AddContactView(){

        layout = new VBox(10);
        layout.setPadding(new Insets(20));

        TextField nameField = new TextField();
        nameField.setPromptText("Enter name");

        TextField numberField = new TextField();
        numberField.setPromptText("Enter number");

        TextField emailField = new TextField();
        emailField.setPromptText("Enter email");

        TextField addressField = new TextField();
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

            Contact contact = null;
            try{
                contact = new Contact(name, num, email, address);
            } catch (IllegalArgumentException e){
                AlertManager.createErrorAlert(e.getMessage(), "DataValidation Error");
                e.printStackTrace();
            }
            try {
                int newId = ViewManager.getDbHelper().save(contact);
                ViewManager.getDbHelper().save(newId, groupsSelected);
                ViewManager.contactsView();
            } catch (SQLException e) {
                AlertManager.generateSaveDataError(e, contact);
            }
        });

        Button resetButton = new Button("Reset");
        resetButton.setOnAction(refresh -> {
            nameField.setText("");
            numberField.setText("");
            emailField.setText("");
            addressField.setText("");
            for(Node node : t.getChildren()){
                CheckBox checkBox = (CheckBox) node;
                checkBox.setSelected(false);
            }
        });

        layout.getChildren().addAll(
                new Label("Name:"), nameField,
                new Label("Number:"), numberField,
                new Label("Email:"), emailField,
                new Label("Address:"), addressField,
                t,
                submitButton,
                resetButton
        );
    }

    public VBox getView() {
        return layout;
    }
}



