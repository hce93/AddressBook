package com.example.addressbook.view;

import com.example.addressbook.controller.ViewManager;
import com.example.addressbook.model.Group;
import com.example.addressbook.util.AlertManager;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class GroupsView implements Initializable {
    private List<Group> groups;
    private List<Group> searchedGroups;
    @FXML
    private TextField searchField;
    @FXML private HBox filtersBox;
    @FXML private VBox groupsBox;
    @FXML private Label noGroupsLabel;
    @FXML private ToggleButton sortButton;
    private boolean sortAtoZ = true;

    public void initialize(URL url, ResourceBundle rb) {
        try{
            groups = ViewManager.getDbHelper().getGroups().stream()
                    .sorted(Comparator.comparing(group->group.getName().toLowerCase()))
                    .collect(Collectors.toList());
            searchedGroups = new ArrayList<>(groups);
        } catch (SQLException e){
            e.printStackTrace();
        }
        buildGroups();
    }

    @FXML
    private void onSortClicked(){
        Collections.reverse(groups);
        Collections.reverse(searchedGroups);
        if (sortButton.isSelected()) {
            sortButton.setText("Z->A");
        } else {
            sortButton.setText("A->Z");
        }
        buildGroups();
    }

    @FXML
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
            noGroupsLabel.setText("You have no groups set up");
        } else {
            noGroupsLabel.setText("");
            for (Group group : searchedGroups) {
                VBox newGroup = new VBox();
                Label name = new Label(group.getName());

                Button deleteButton = new Button("Delete");
                deleteButton.setOnAction(deleteEvent -> {
                    try {
                        if (ViewManager.getDbHelper().deleteGroup(group.getId())) {
                            groups.remove(group);
                            searchedGroups.removeIf(group1 -> (group1==group));
                            // refresh contacts view so group is removed from contacts
                            ViewManager.refreshContactsScene();
                            ViewManager.refreshAddContactsScene();
                            buildGroups();
                        }
                    } catch (SQLException e) {
                        AlertManager.createErrorAlert(
                                "Unable to delete group: " + name, "Database Error");
                        e.printStackTrace();
                    } catch (IOException e){
                        e.printStackTrace();
                    }
                });

                Button editButton = new Button("Edit");
                editButton.setOnAction(editEvent -> {
                    try {
                        ViewManager.editGroupView(group);
                    }catch (IOException e) {
                        e.printStackTrace();
                    }
                });

                newGroup.getChildren().addAll(
                        name,
                        editButton,
                        deleteButton);
                groupsBox.getChildren().add(newGroup);
            }
        }
    }
}
