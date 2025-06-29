package com.example.addressbook.controller;

import com.example.addressbook.util.AlertManager;
import com.example.addressbook.util.DatabaseConfig;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;

public class LoginController implements Initializable {

    public void initialize(URL url, ResourceBundle rb) {}
    @FXML private TextField usernameField;
    @FXML private PasswordField pwField;

    @FXML
    private void login(){
        String username = usernameField.getText();
        String password = pwField.getText();
        pwField.clear();
        String connStr = "jdbc:mysql://"+ DatabaseConfig.HOST+":"+DatabaseConfig.PORT+"/";
        try {

            Connection connection = DriverManager.getConnection(
                    connStr+DatabaseConfig.DB_NAME, username, password);
            ViewManager.setConn(connection);
            ViewManager.contactsView();
            //remove logged password once logged in
            password="";

        } catch (SQLException e) {
            if (e.getMessage().contains("Unknown database")) {
                // create a new db named address_book then create connection with new db
                try {
                    String sql = "CREATE DATABASE ";
                    Connection connection = DriverManager.getConnection(
                            connStr, username, password
                    );
                    Statement stmt = connection.createStatement();
                    stmt.executeUpdate(sql+DatabaseConfig.DB_NAME);
                    connection.close();
                    try {
                        Connection connection2 = DriverManager.getConnection(
                                connStr+DatabaseConfig.DB_NAME, username, password);
                        createTableStatements(connection2);
                        ViewManager.setConn(connection2);
                        ViewManager.contactsView();
                    } catch (Exception e3) {
                        e3.printStackTrace();
                        AlertManager.createErrorAlert("Could not connect after creating DB", "Database Error");
                    }
                } catch (SQLException e2){
                    System.out.println("Error creating Database");
                    e2.printStackTrace();
                }
            } else {
                AlertManager.createErrorAlert(
                        "Access Denied. Please try again", "Database Error");
                e.printStackTrace();
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    private void createTableStatements(Connection conn) {
        try {
            Statement stmt = conn.createStatement();


            try {
                stmt.executeUpdate(DatabaseConfig.createAddress);
            } catch (SQLException e) {
                System.out.println("Failed ot  create address table");
                e.printStackTrace();
            }

            try {
                stmt.executeUpdate(DatabaseConfig.createGroups);
            } catch (SQLException e) {
                System.out.println("Failed ot  create groups table");
                e.printStackTrace();
            }

            try {
                stmt.executeUpdate(DatabaseConfig.createAddressGroups);
            } catch (SQLException e){
                System.out.println("Failed to create address groups table");
                e.printStackTrace();
            }

        } catch (SQLException e){
            System.out.println("Connection failed");
            e.printStackTrace();
        }
    }
}
