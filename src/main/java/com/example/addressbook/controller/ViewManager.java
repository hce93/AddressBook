package com.example.addressbook.controller;

import com.example.addressbook.model.Contact;
import com.example.addressbook.util.DatabaseHelper;
import com.example.addressbook.view.*;
import javafx.scene.Scene;
import javafx.scene.control.MenuBar;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.Connection;

public class ViewManager {

    private static Stage primaryStage;
    private static Connection conn;

    private static MenuBar mainMenuBar = new ContactsMenuBar().getMenu();
    private static DatabaseHelper dbHelper = new DatabaseHelper();

    public static void setStage(Stage stage){primaryStage=stage;}
    public static void setConn(Connection connection) {conn = connection;}
    public static Connection getConnection() {return conn;}
    public static DatabaseHelper getDbHelper() {return dbHelper;}
    public static MenuBar getMainMenuBar() {return mainMenuBar;}


    public static void loginView() {
        LoginView login = new LoginView();
        primaryStage.setScene(createScene(login.getLoginView()));
        primaryStage.setTitle("Database Login");
    }

    public static void contactsView(){
        ContactsView view = new ContactsView();
        primaryStage.setScene(createScene(view.getView()));
        primaryStage.setHeight(500);
        primaryStage.setWidth(500);
        primaryStage.setTitle("Contacts");
    }

    public static void addContactsView(){
        AddContactView view = new AddContactView();
        primaryStage.setScene(createScene(view.getView()));
        primaryStage.setTitle("Add Contacts");
    }

    public static void editContactsView(Contact contact){
        EditContactView view = new EditContactView(contact);
        primaryStage.setScene(createScene(view.getView()));
        primaryStage.setTitle("Edit Contact");
    }

    private static Scene createScene(GridPane view){
        BorderPane root = new BorderPane();
        root.setCenter(view);
        Scene scene = new Scene(root);
        return scene;
    }

    private static Scene createScene(VBox view){
        BorderPane root = new BorderPane();
        root.setCenter(view);
        root.setTop(getMainMenuBar());
        Scene scene = new Scene(root);
        return scene;
    }
}
