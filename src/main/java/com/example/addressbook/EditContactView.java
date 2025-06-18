package com.example.addressbook;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

import java.sql.SQLException;

public class EditContactView {

    private Contact contact;


    private VBox layout;

    public EditContactView(Contact contact) {
        this.contact = contact;

        layout = new VBox(10);
        layout.setPadding(new Insets(20));

        TextField nameField = new TextField();
        nameField.setText(contact.getName());
        nameField.setPromptText("Enter name");

        TextField numberField = new TextField();
        numberField.setText(contact.getNumber());
        numberField.setPromptText("Enter number");

        TextField emailField = new TextField();
        emailField.setText(contact.getEmail());
        emailField.setPromptText("Enter email");

        TextField addressField = new TextField();
        addressField.setText(contact.getAddress());
        addressField.setPromptText("Enter address");

        Button submitButton = new Button("Submit");
        submitButton.setOnAction(submitted -> {
            String name = nameField.getText();
            String num = numberField.getText();
            String email = emailField.getText();
            String address = addressField.getText();

            Contact newContact = new Contact(name, num, email, address);
            try{
                if(ViewManager.getDbHelper().editContact(this.contact.getName(), newContact)){
                    ViewManager.contactsView();
                }

            } catch (SQLException e){
                e.printStackTrace();
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
