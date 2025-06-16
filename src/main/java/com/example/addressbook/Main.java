package com.example.addressbook;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Arrays;


public class Main extends Application {
    private final static String CONN_STRING = "jdbc:mysql://localhost:3306/address_book";
    public static Connection connection;

    @Override
    public void start(Stage primaryStage) {

        TextInputDialog userDialog = new TextInputDialog();
        userDialog.setTitle("Database Login");
        userDialog.setHeaderText("Enter database username");

        userDialog.showAndWait().ifPresent(username -> {
            Dialog<String> passwordDialog = new Dialog<>();
            passwordDialog.setTitle("Database Login");
            passwordDialog.setHeaderText("Enter Password for "+username);
            PasswordField passwordField = new PasswordField();
            passwordDialog.getDialogPane().setContent(passwordField);
            passwordDialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

            passwordDialog.setResultConverter(dialogButton -> {
                if (dialogButton == ButtonType.OK) {
                    return passwordField.getText();
                }
                return null;
            });

            //Change the below to keep the window open if the password is incorrect

            passwordDialog.showAndWait().ifPresent(password -> {
                try{
                    Connection connection = DriverManager.getConnection(
                        CONN_STRING, username, password);
                    AddContactView view = new AddContactView(connection);

                    Scene scene = new Scene(view.getView(), 500, 350);
                    primaryStage.setTitle("Add Contact");
                    primaryStage.setScene(scene);
                    primaryStage.show();

                    //remove logged password once logged in
                    Arrays.fill(password.toCharArray(), ' ');
                    System.out.println("Inside try");
                } catch (SQLException e) {
                    Alert alert = new Alert(javafx.scene.control.Alert.AlertType.ERROR);
                    alert.setContentText("Access Denied. Application Closing");
                    alert.setTitle("Database Error");
                    alert.showAndWait();
                    e.printStackTrace();
                    e.printStackTrace();

                }

                primaryStage.setOnCloseRequest(e->{
                    try {
                        connection.close();
                        System.out.println("connection closed");
                    } catch (Exception exc) {
                        System.err.println("couldn't close connection");
                    }
                });
            });
        });


        System.out.println("Outside try");

    }


    public static void main(String[] args){
        launch(args);
    }
}