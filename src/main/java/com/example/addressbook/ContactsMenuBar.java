package com.example.addressbook;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;

public class ContactsMenuBar {

    public MenuBar getMenu(){
        Menu m = new Menu("Menu");
        MenuItem m1 = new MenuItem("Contacts");
        m1.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                ViewManager.contactsView();
            }
        });
        MenuItem m2 = new MenuItem("Add Contacts");
        m2.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                ViewManager.addContactsView();
//                AddContactView view = new AddContactView(conn);
//                root.setCenter(view.getView());
            }
        });

        m.getItems().add(m1);
        m.getItems().add(m2);
        MenuBar mb = new MenuBar();
        mb.getMenus().add(m);

        return mb;
    }
}
