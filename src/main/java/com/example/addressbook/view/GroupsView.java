package com.example.addressbook.view;

import com.example.addressbook.controller.ViewManager;
import com.example.addressbook.model.Group;
import com.example.addressbook.util.AlertManager;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class GroupsView {
    private List<Group> groups;
    private List<Group> searchedGroups;
    private VBox layout;
    private VBox groupsBox = new VBox();
    private TextField searchField;
    private boolean sortAtoZ = true;

    public GroupsView(){
        try{
            groups = ViewManager.getDbHelper().getGroups().stream()
                    .sorted(Comparator.comparing(Group::getName))
                    .collect(Collectors.toList());
            searchedGroups = new ArrayList<>(groups);
        } catch (SQLException e){
            e.printStackTrace();
        }

        layout = new VBox(10);
        layout.setPadding(new Insets(20));
        buildGroups();
        layout.getChildren().addAll(getSearchBar(), getSortButton(), groupsBox);
    }

    private ToggleButton getSortButton(){
        ToggleButton sortButton = new ToggleButton("A->Z");
        sortButton.setOnAction(pressed -> {
            Collections.reverse(groups);
            Collections.reverse(searchedGroups);
            if (sortButton.isSelected()) {
                sortButton.setText("Z->A");
            } else {
                sortButton.setText("A->Z");
            }
            buildGroups();
        });
        return sortButton;
    }

    private HBox getSearchBar(){
        HBox searchBox = new HBox();
        searchField = new TextField();
        Button searchButton = new Button("Search");
        searchButton.setOnAction(search -> {
            searchGroups();
        });
        searchButton.setDefaultButton(true);
        searchBox.getChildren().addAll(searchField, searchButton);
        return searchBox;
    }

    private void searchGroups(){
        String searchText = searchField.getText();
        if (!searchText.isBlank()) {
            Pattern pattern = Pattern.compile(searchText, Pattern.CASE_INSENSITIVE);
            searchedGroups = groups.stream()
                    .filter(group -> pattern.matcher(group.getName()).find())
                    .collect(Collectors.toList());
        } else {
            searchedGroups=new ArrayList<>(groups);
        }
        buildGroups();
    }

    private void buildGroups(){
        groupsBox.getChildren().clear();
        if (searchedGroups.isEmpty()){
            Label emptyLabel = new Label("You have no groups set up");
            groupsBox.getChildren().add(emptyLabel);

        } else {
            for (Group group : searchedGroups) {
                VBox newGroup = new VBox();
                Label name = new Label(group.getName());

                Button deleteButton = new Button("Delete");
                deleteButton.setOnAction(deleteEvent -> {
                    try {
                        if (ViewManager.getDbHelper().deleteGroup(group.getId())) {
                            ViewManager.groupsView();
                        }
                    } catch (SQLException e) {
                        AlertManager.createErrorAlert(
                                "Unable to delete group: " + name, "Database Error");
                        e.printStackTrace();
                    }
                });

                Button editButton = new Button("Edit");
                editButton.setOnAction(editEvent -> {
                    ViewManager.editGroupView(group);
                });

                newGroup.getChildren().addAll(
                        name,
                        editButton,
                        deleteButton);
                groupsBox.getChildren().add(newGroup);
            }
        }
    }
    public VBox getView() {
        return layout;
    }
}
