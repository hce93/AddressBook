package com.example.addressbook;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Arrays;

public class LoginView {
    private final static String CONN_STRING = "jdbc:mysql://localhost:3306/address_book";
    public GridPane getLoginView(){
        return getLoginGrid();
    }

    private GridPane getLoginGrid(){
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        Label userName = new Label("User Name:");
        grid.add(userName, 0, 1);

        TextField userTextField = new TextField();
        userTextField.setPromptText("username");
        grid.add(userTextField, 1, 1);

        Label pw = new Label("Password:");
        grid.add(pw, 0, 2);

        PasswordField pwBox = new PasswordField();
        pwBox.setPromptText("password");
        grid.add(pwBox, 1, 2);

        Button btn = new Button("Login");
        btn.setDefaultButton(true);

        btn.setOnAction(event -> {
            String username = userTextField.getText();
            String password = pwBox.getText();

            try {
                Connection connection = DriverManager.getConnection(
                        CONN_STRING, username, password);
                ViewManager.setConn(connection);
                ViewManager.contactsView();
                //remove logged password once logged in
                Arrays.fill(password.toCharArray(), ' ');

            } catch (SQLException e) {
                Alert alert = new Alert(javafx.scene.control.Alert.AlertType.ERROR);
                alert.setContentText("Access Denied. Please try again");
                alert.setTitle("Database Error");
                alert.showAndWait();
                e.printStackTrace();
                e.printStackTrace();
            }
            pwBox.clear();
        });
        grid.add(btn, 1,3);
        return grid;
    }
}
