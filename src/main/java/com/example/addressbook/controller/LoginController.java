package com.example.addressbook.controller;

import com.example.addressbook.util.AlertManager;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.ResourceBundle;

public class LoginController implements Initializable {

    public void initialize(URL url, ResourceBundle rb) {}
    private final static String CONN_STRING = "jdbc:mysql://localhost:3306/address_book";
    @FXML
    private TextField usernameField;
    @FXML private PasswordField pwField;

    @FXML
    private void login(){
        String username = usernameField.getText();
        String password = pwField.getText();

        try {
            Connection connection = DriverManager.getConnection(
                    CONN_STRING, username, password);
            ViewManager.setConn(connection);
            ViewManager.contactsView();
            //remove logged password once logged in
            Arrays.fill(password.toCharArray(), ' ');

        } catch (SQLException e) {
            AlertManager.createErrorAlert(
                    "Access Denied. Please try again","Database Error");
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }
        pwField.clear();
    }
}
