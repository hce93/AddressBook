package com.example.addressbook;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
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
        BorderPane root = new BorderPane();
        GridPane loginGrid = getLoginGrid(primaryStage, root);

        root.setCenter(loginGrid);
        Scene scene = new Scene(root);
        primaryStage.setTitle("Database Login");
        primaryStage.setScene(scene);
        primaryStage.show();

    }

    private GridPane getLoginGrid(Stage primaryStage, BorderPane root){
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
                AddContactView view = new AddContactView(connection);

                root.setTop(getMenu(root, connection));
                root.setCenter(view.getView());
                primaryStage.setTitle("Add Contact");

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

    public MenuBar getMenu(BorderPane root, Connection conn){
        Menu m = new Menu("Menu");
        MenuItem m1 = new MenuItem("Contacts");
        m1.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                ContactsView contactView = new ContactsView(conn);
                root.setCenter(contactView.getView());
            }
        });
        MenuItem m2 = new MenuItem("Add Contacts");
        m2.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                AddContactView view = new AddContactView(conn);
                root.setCenter(view.getView());
            }
        });

        m.getItems().add(m1);
        m.getItems().add(m2);
        MenuBar mb = new MenuBar();
        mb.getMenus().add(m);

        return mb;
    }


    public static void main(String[] args){
        launch(args);
    }
}