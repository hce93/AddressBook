package com.example.addressbook.controller;

import com.example.addressbook.model.Contact;
import com.example.addressbook.model.Group;
import com.example.addressbook.util.DatabaseHelper;
import com.example.addressbook.view.*;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.IOException;
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
    private Parent root;

    private static DatabaseHelper dbHelper = new DatabaseHelper();

    public static void setStage(Stage stage){primaryStage=stage;}
    public static void setConn(Connection connection) {conn = connection;}
    public static Connection getConnection() {return conn;}
    public static DatabaseHelper getDbHelper() {return dbHelper;}


    public static void loginView() {
        LoginView login = new LoginView();
        primaryStage.setScene(createScene(login.getLoginView()));
        primaryStage.setTitle("Database Login");
    }

    public static void refreshContactsView(){
        contactsView = new ContactsView();
    }
    public static void contactsView() throws IOException {
        FXMLLoader loader = new FXMLLoader(ViewManager.class.getResource("/com/example/addressbook/ContactsView.fxml"));
        Parent root = loader.load();
        primaryStage.setScene(new Scene(root));
        primaryStage.setHeight(500);
        primaryStage.setWidth(500);
        primaryStage.setTitle("Contacts");
    }

    public static void refreshAddContactsView(){
        addContactView = new AddContactView();
    }

    public static void addContactsView() throws IOException{
        FXMLLoader loader = new FXMLLoader(ViewManager.class.getResource("/com/example/addressbook/AddContactView.fxml"));
        Parent root = loader.load();
        primaryStage.setScene(new Scene(root));
        primaryStage.setTitle("Add Contacts");
    }

    public static void editContactsView(Contact contact) throws IOException{
        FXMLLoader loader = new FXMLLoader(ViewManager.class.getResource("/com/example/addressbook/EditContactView.fxml"));
        Parent root = loader.load();
        EditContactView view = loader.getController();
        view.setContactToEdit(contact);
        primaryStage.setScene(new Scene(root));
        primaryStage.setTitle("Edit Contact");
    }

    public static void refreshGroupsView(){
        groupsView = new GroupsView();
    }
    public static void groupsView() throws IOException{
        FXMLLoader loader = new FXMLLoader(ViewManager.class.getResource("/com/example/addressbook/GroupsView.fxml"));
        Parent root = loader.load();
        primaryStage.setScene(new Scene(root));
        primaryStage.setTitle("Groups");
    }
    public static void addGroupsView() throws IOException{

        FXMLLoader loader = new FXMLLoader(ViewManager.class.getResource("/com/example/addressbook/AddGroupView.fxml"));
        Parent root = loader.load();
        primaryStage.setScene(new Scene(root));
        primaryStage.setTitle("Add Groups");
    }

    public static void editGroupView(Group group) throws IOException{
        FXMLLoader loader = new FXMLLoader(ViewManager.class.getResource("/com/example/addressbook/EditGroupView.fxml"));
        Parent root = loader.load();
        EditGroupView view = loader.getController();
        view.setGroupToEdit(group);
        primaryStage.setScene(new Scene(root));
        primaryStage.setTitle("Edit Groups");
    }

    private static Scene createScene(GridPane view){
        BorderPane root = new BorderPane();
        root.setCenter(view);
        Scene scene = new Scene(root);
        return scene;
    }


}
