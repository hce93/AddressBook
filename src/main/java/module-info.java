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
}