package com.example.addressbook;

import com.example.addressbook.controller.ViewManager;
import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;


public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        ViewManager.setStage(primaryStage);
        try {
            ViewManager.loginView();
        } catch (IOException e){
            e.printStackTrace();
        }
        primaryStage.show();
    }

    public static void main(String[] args){
        launch(args);
    }
}