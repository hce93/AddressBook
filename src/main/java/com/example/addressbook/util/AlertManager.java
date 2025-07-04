package com.example.addressbook.util;

import com.example.addressbook.model.Contact;
import com.example.addressbook.model.Group;
import javafx.application.Platform;
import javafx.scene.control.Alert;

import java.sql.SQLException;

public class AlertManager {

    public static void generateSaveDataError(SQLException e, Contact contact){
        if (!Platform.isFxApplicationThread()) return;

        String message = e.getMessage().toLowerCase();
        String baseMessage = "%s: %s already exists in your contacts";
        String alertMessage;
        if (message.contains("name")){
            alertMessage=String.format(baseMessage,"Name", contact.getName());
        } else if (message.contains("number")) {
            alertMessage=String.format(baseMessage,"Number", contact.getNumber());
        } else if (message.contains("email")) {
            alertMessage=String.format(baseMessage,"Email", contact.getEmail());
        } else if (message.contains("Address")) {
            alertMessage=String.format(baseMessage,"Address", contact.getAddress());
        } else {
            alertMessage="An error has occurred when saving the data. Please try again";
        }
        createErrorAlert(alertMessage, "Error Saving Contact");
        e.printStackTrace();
    }

    public static void generateSaveDataError(SQLException e, Group group){
        if (!Platform.isFxApplicationThread()) return;

        String message = e.getMessage().toLowerCase();
        String baseMessage = "%s: %s already exists in your groups";
        String alertMessage;
        if (message.contains("name")){
            alertMessage=String.format(baseMessage,"Name", group.getName());
        } else {
            alertMessage="An error has occurred when saving the data. Please try again";
        }
        createErrorAlert(alertMessage, "Error Saving Group");
        e.printStackTrace();
    }

    public static void createErrorAlert(String message, String title){
        if (!Platform.isFxApplicationThread()) return;

        Alert alert = new Alert(javafx.scene.control.Alert.AlertType.ERROR);
        alert.setContentText(message);
        alert.setTitle(title);
        alert.showAndWait();
    }
}
