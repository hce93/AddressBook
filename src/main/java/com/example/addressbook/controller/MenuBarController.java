package com.example.addressbook.controller;

import com.example.addressbook.controller.ViewManager;
import javafx.fxml.FXML;

import java.io.IOException;

public class MenuBarController {
    @FXML
    private void loadContactsView() throws IOException {
        ViewManager.contactsView();
    }
    @FXML
    private void loadAddContactsView() throws IOException{
        ViewManager.addContactsView();
    }
    @FXML
    private void loadGroupsView() throws IOException{
        ViewManager.groupsView();
    }

    @FXML
    private void addGroupView() throws IOException{
        ViewManager.addGroupsView();
    }
}
