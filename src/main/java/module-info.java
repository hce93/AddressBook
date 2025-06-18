module com.example.addressbook {
    requires javafx.controls;
    requires javafx.fxml;

    requires java.sql;


    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.bootstrapfx.core;
    requires java.desktop;

    opens com.example.addressbook to javafx.fxml;
    exports com.example.addressbook;
    exports com.example.addressbook.view;
    opens com.example.addressbook.view to javafx.fxml;
    exports com.example.addressbook.model;
    opens com.example.addressbook.model to javafx.fxml;
    exports com.example.addressbook.util;
    opens com.example.addressbook.util to javafx.fxml;
    exports com.example.addressbook.controller;
    opens com.example.addressbook.controller to javafx.fxml;
}