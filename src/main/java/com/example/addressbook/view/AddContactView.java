package com.example.addressbook.view;

import com.example.addressbook.util.AlertManager;
import com.example.addressbook.model.Contact;
import com.example.addressbook.controller.ViewManager;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

import java.sql.SQLException;

public class AddContactView {

    private VBox layout;

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
                ViewManager.getDbHelper().save(contact);
                ViewManager.contactsView();
            } catch (SQLException e) {
                AlertManager.generateSaveDataError(e, contact);
            }
        });

        layout.getChildren().addAll(
                new Label("Name:"), nameField,
                new Label("Number:"), numberField,
                new Label("Email:"), emailField,
                new Label("Address:"), addressField,
                submitButton
        );
    }

    public VBox getView() {
        return layout;
    }
}



