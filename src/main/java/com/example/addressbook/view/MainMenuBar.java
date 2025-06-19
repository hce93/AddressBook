package com.example.addressbook.view;

import com.example.addressbook.controller.ViewManager;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;

public class MainMenuBar {

    public MenuBar getMenu(){
        Menu m = new Menu("Menu");

        MenuItem m1 = new MenuItem("Contacts");
        m1.setOnAction(actionEvent -> ViewManager.contactsView());

        MenuItem m2 = new MenuItem("Add Contacts");
        m2.setOnAction(actionEvent -> ViewManager.addContactsView());

        MenuItem m3 = new MenuItem("Groups");
        m3.setOnAction(actionEvent -> ViewManager.groupsView());

        MenuItem m4 = new MenuItem("Add Groups");
        m4.setOnAction(actionEvent -> ViewManager.addGroupsView());

        m.getItems().add(m1);
        m.getItems().add(m2);
        m.getItems().add(m3);
        m.getItems().add(m4);
        MenuBar mb = new MenuBar();
        mb.getMenus().add(m);

        return mb;
    }
}
