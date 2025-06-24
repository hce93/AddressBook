package com.example.addressbook.util;

import com.example.addressbook.controller.ViewManager;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

import java.io.IOException;

public class ViewManagerHelper {

    public static FXMLLoader getLoader(String location){
        return new FXMLLoader(ViewManager.class.getResource(location));
    }

    public static Scene refreshScene(String fxmlLocation) throws IOException {
        FXMLLoader loader = getLoader(fxmlLocation);
        return refreshScene(loader);
    }

    public static Scene refreshScene(FXMLLoader loader) throws IOException{
        Parent root = loader.load();
        return new Scene(root);
    }

}
