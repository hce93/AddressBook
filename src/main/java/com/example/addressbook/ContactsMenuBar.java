package com.example.addressbook;

import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;

public class ContactsMenuBar {

    public MenuBar getMenu(){
        Menu m = new Menu("Menu");

        MenuItem m1 = new MenuItem("Contacts");
        m1.setOnAction(actionEvent -> ViewManager.contactsView());

        MenuItem m2 = new MenuItem("Add Contacts");
        m2.setOnAction(actionEvent -> ViewManager.addContactsView());

        m.getItems().add(m1);
        m.getItems().add(m2);
        MenuBar mb = new MenuBar();
        mb.getMenus().add(m);

        return mb;
    }
}
