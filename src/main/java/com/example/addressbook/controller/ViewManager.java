package com.example.addressbook.controller;

import com.example.addressbook.model.Contact;
import com.example.addressbook.model.Group;
import com.example.addressbook.util.DatabaseHelper;
import com.example.addressbook.view.EditContactView;
import com.example.addressbook.view.EditGroupView;
import com.example.addressbook.view.LoginView;
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
    private static Scene contactsScene;
    private static Scene addContactScene;
    private static Scene editContactScene;
    private static Scene groupsScene;
    private static Scene addGroupScene;
    private static Scene editGroupScene;
    private Parent root;

    private static DatabaseHelper dbHelper = new DatabaseHelper();

    public static void setStage(Stage stage){primaryStage=stage;}
    public static void setConn(Connection connection) {conn = connection;}
    public static Connection getConnection() {return conn;}
    public static DatabaseHelper getDbHelper() {return dbHelper;}

    public static FXMLLoader getLoader(String location){
        return new FXMLLoader(ViewManager.class.getResource(location));
    }

    public static Scene refreshScene(String fxmlLocation) throws IOException{
        FXMLLoader loader = getLoader(fxmlLocation);
        return refreshScene(loader);
    }

    public static Scene refreshScene(FXMLLoader loader) throws IOException{
        Parent root = loader.load();
        return new Scene(root);
    }

    public static void loginView() {
        LoginView login = new LoginView();
        primaryStage.setScene(createScene(login.getLoginView()));
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
        EditContactView view = loader.getController();
        view.setContactToEdit(contact);
        editContactScene = refreshScene(loader);
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
        FXMLLoader loader = getLoader("/com/example/addressbook/EditGroupView.fxml");
        EditGroupView view = loader.getController();
        view.setGroupToEdit(group);
        editGroupScene = refreshScene(loader);
        primaryStage.setScene(editGroupScene);
        primaryStage.setTitle("Edit Groups");
    }

    private static Scene createScene(GridPane view){
        BorderPane root = new BorderPane();
        root.setCenter(view);
        Scene scene = new Scene(root);
        return scene;
    }


}
