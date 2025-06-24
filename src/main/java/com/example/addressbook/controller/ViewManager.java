package com.example.addressbook.controller;

import com.example.addressbook.model.Contact;
import com.example.addressbook.model.Group;
import com.example.addressbook.util.DatabaseHelper;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;

import static com.example.addressbook.util.ViewManagerHelper.getLoader;
import static com.example.addressbook.util.ViewManagerHelper.refreshScene;

public class ViewManager {

    private static Stage primaryStage;
    private static Connection conn;
    private static Scene contactsScene;
    private static Scene addContactScene;
    private static Scene editContactScene;
    private static Scene groupsScene;
    private static Scene addGroupScene;
    private static Scene editGroupScene;

    private static DatabaseHelper dbHelper = new DatabaseHelper();
    public static void setStage(Stage stage){primaryStage=stage;}
    public static void setConn(Connection connection) {conn = connection;}
    public static Connection getConnection() {return conn;}
    public static DatabaseHelper getDbHelper() {return dbHelper;}

    public static void loginView() throws IOException{
        Scene loginScene = refreshScene("/com/example/addressbook/LoginView.fxml");
        primaryStage.setScene(loginScene);
        primaryStage.setTitle("Database Login");
    }

    public static void refreshContactsScene() throws IOException{
        contactsScene = refreshScene("/com/example/addressbook/ContactsView.fxml");
    }
    public static void contactsView() throws IOException {
        if (contactsScene==null) {
            refreshContactsScene();
        }
        primaryStage.setScene(contactsScene);
        primaryStage.setHeight(500);
        primaryStage.setWidth(500);
        primaryStage.setTitle("Contacts");
    }

    public static void refreshAddContactsScene() throws IOException{
        addContactScene = refreshScene("/com/example/addressbook/AddContactView.fxml");
    }

    public static void addContactsView() throws IOException{
        if (addContactScene==null) {
            refreshAddContactsScene();
        }
        primaryStage.setScene(addContactScene);
        primaryStage.setTitle("Add Contacts");
    }

    public static void editContactsView(Contact contact) throws IOException{
        FXMLLoader loader = getLoader("/com/example/addressbook/EditContactView.fxml");
        editContactScene = refreshScene(loader);
        EditContactController view = loader.getController();
        view.setContactToEdit(contact);
        primaryStage.setScene(editContactScene);
        primaryStage.setTitle("Edit Contact");
    }

    public static void refreshGroupsScene() throws IOException{
        groupsScene = refreshScene("/com/example/addressbook/GroupsView.fxml");
    }
    public static void groupsView() throws IOException{
        if (groupsScene==null) {
            refreshGroupsScene();
        }
        primaryStage.setScene(groupsScene);
        primaryStage.setTitle("Groups");
    }

    public static void refreshAddGroupsScene() throws IOException{
        addGroupScene = refreshScene("/com/example/addressbook/AddGroupView.fxml");
    }

    public static void addGroupsView() throws IOException{
        if (addGroupScene==null){
            refreshAddGroupsScene();
        }
        primaryStage.setScene(addGroupScene);
        primaryStage.setTitle("Add Groups");
    }

    public static void editGroupView(Group group) throws IOException{
        FXMLLoader loader = getLoader("/com/example/addressbook/EditGroupView.fxml").load();
        editGroupScene = refreshScene(loader);
        EditGroupController view = loader.getController();
        view.setGroupToEdit(group);
        primaryStage.setScene(editGroupScene);
        primaryStage.setTitle("Edit Groups");
    }
}
