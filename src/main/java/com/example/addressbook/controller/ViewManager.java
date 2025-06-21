package com.example.addressbook.controller;

import com.example.addressbook.model.Contact;
import com.example.addressbook.model.Group;
import com.example.addressbook.util.DatabaseHelper;
import com.example.addressbook.view.*;
import javafx.scene.Scene;
import javafx.scene.control.MenuBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.Connection;

public class ViewManager {

    private static Stage primaryStage;
    private static Connection conn;
    private static ContactsView contactsView;
    private static AddContactView addContactView;
    private static EditContactView editContactView;
    private static GroupsView groupsView;
    private static AddGroupView addGroupView;
    private static EditGroupView editGroupView;

    private static MenuBar mainMenuBar = new MainMenuBar().getMenu();
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

    public static void refreshContactsView(){
        contactsView = new ContactsView();
    }
    public static void contactsView(){
        if (contactsView==null){
            refreshContactsView();
        }

        primaryStage.setScene(createScene(contactsView.getView()));
        primaryStage.setHeight(500);
        primaryStage.setWidth(500);
        primaryStage.setTitle("Contacts");
    }

    public static void refreshAddContactsView(){
        addContactView = new AddContactView();
    }

    public static void addContactsView(){
        if (addContactView==null){
            refreshAddContactsView();
        }
        primaryStage.setScene(createScene(addContactView.getView()));
        primaryStage.setTitle("Add Contacts");
    }

    public static void editContactsView(Contact contact){
        if(editContactView==null){
            editContactView = new EditContactView(contact);
        }
        primaryStage.setScene(createScene(editContactView.getView()));
        primaryStage.setTitle("Edit Contact");
    }

    public static void refreshGroupsView(){
        groupsView = new GroupsView();
    }
    public static void groupsView(){
        if (groupsView==null){
            refreshGroupsView();
        }
        primaryStage.setScene(createScene(groupsView.getView()));
        primaryStage.setTitle("Groups");
    }
    public static void addGroupsView(){
        if (addGroupView==null){
            addGroupView = new AddGroupView();
        }
        primaryStage.setScene(createScene(addGroupView.getView()));
        primaryStage.setTitle("Add Groups");
    }

    public static void editGroupView(Group group){
        if (editGroupView==null){
            editGroupView = new EditGroupView(group);
        }
        primaryStage.setScene(createScene(editGroupView.getView()));
        primaryStage.setTitle("Edit Groups");
    }

    private static Scene createScene(GridPane view){
        BorderPane root = new BorderPane();
        root.setCenter(view);
        Scene scene = new Scene(root);
        return scene;
    }

    private static Scene createScene(VBox view){
        BorderPane root = new BorderPane();
        ScrollPane scrollPane = new ScrollPane(view);
        scrollPane.setFitToWidth(true);
        root.setCenter(scrollPane);
        root.setTop(getMainMenuBar());
        Scene scene = new Scene(root);
        return scene;
    }
}
