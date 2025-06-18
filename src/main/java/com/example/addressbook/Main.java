package com.example.addressbook;

import com.example.addressbook.controller.ViewManager;
import javafx.application.Application;
import javafx.stage.Stage;


public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        ViewManager.setStage(primaryStage);
        ViewManager.loginView();
        primaryStage.show();
    }

    public static void main(String[] args){
        launch(args);
    }
}