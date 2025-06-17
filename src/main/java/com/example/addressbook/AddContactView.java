package com.example.addressbook;

import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.sql.Connection;
import java.sql.SQLException;

public class AddContactView {
    private Connection connection;
    private DatabaseHelper dbHelper;

    private VBox layout;

    public AddContactView(Connection connection){
        this.connection = connection;
        this.dbHelper = new DatabaseHelper(connection);

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
        Text confirmationText = new Text();
        System.out.println("Made it!!!");

        submitButton.setOnAction(submitted -> {
            String name = nameField.getText();
            String num = numberField.getText();
            String email = emailField.getText();
            String address = addressField.getText();

            try {

                Contact contact = new Contact(name, num, email, address);
                dbHelper.save(contact);
                confirmationText.setText("Contact added:\n" + contact);

            } catch (SQLException e){
                Alert alert = new Alert(javafx.scene.control.Alert.AlertType.ERROR);
                alert.setContentText(e.getMessage());
                alert.setTitle("Database Error");
                alert.showAndWait();
                e.printStackTrace();
            } catch (IllegalArgumentException e){
                Alert alert = new Alert(javafx.scene.control.Alert.AlertType.ERROR);
                alert.setContentText(e.getMessage());
                alert.setTitle("Data Validation Error");
                alert.showAndWait();
                e.printStackTrace();
            }
        });

        layout.getChildren().addAll(
                new Label("Name:"), nameField,
                new Label("Number:"), numberField,
                new Label("Email:"), emailField,
                new Label("Address:"), addressField,
                submitButton,
                confirmationText
        );

    }

    public VBox getView() {
        return layout;
    }
}



