package com.example.addressbook.view;

import com.example.addressbook.controller.ViewManager;
import com.example.addressbook.model.Group;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.sql.SQLException;
import java.util.List;

public class GroupsView {
    private List<Group> groups;

    private VBox layout;

    public GroupsView(){
        try{
            groups = ViewManager.getDbHelper().getGroups();
        } catch (SQLException e){
            e.printStackTrace();
        }

        layout = new VBox(10);
        layout.setPadding(new Insets(20));
        buildGroups();
    }

    private void buildGroups(){

        if (groups.isEmpty()){
            Label emptyLabel = new Label("You have no groups set up");
            layout.getChildren().add(emptyLabel);
        } else {
            for (Group group : groups) {
                VBox newGroup = new VBox();
                Label name = new Label(group.getName());

//                Button deleteButton = new Button("Delete");
//                deleteButton.setOnAction(deleteEvent -> {
//                    try {
//                        if (ViewManager.getDbHelper().deleteGroup(group)) {
//                            ViewManager.contactsView();
//                        }
//                    } catch (SQLException e) {
//                        AlertManager.createErrorAlert(
//                                "Unable to delete group: " + name, "Database Error");
//                        e.printStackTrace();
//                    }
//                });

//                Button editButton = new Button("Edit");
//                editButton.setOnAction(editEvent -> {
//                    ViewManager.editContactsView(contact);
//                });

                newGroup.getChildren().addAll(name);
                layout.getChildren().add(newGroup);
            }
        }
    }
    public VBox getView() {
        return layout;
    }
}
