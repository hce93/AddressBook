package com.example.addressbook.controller;

import com.example.addressbook.util.DatabaseHelper;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.testfx.framework.junit.ApplicationTest;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Properties;

import static org.junit.Assert.assertEquals;
import static org.testfx.api.FxAssert.verifyThat;
import static org.testfx.matcher.control.TextInputControlMatchers.hasText;

public class ContactFormControllerTest extends ApplicationTest {
    private static Connection conn;
    private static DatabaseHelper dbHelper;
    private ContactFormController controller;

    private static Properties loadDbProperties() throws IOException {
        Properties props = new Properties();
        try(FileInputStream fis = new FileInputStream("config/test.properties")){
            props.load(fis);
        }
        return props;
    }

    @BeforeClass
    public static void setup() throws Exception{
        Properties dbProps = loadDbProperties();
        String url = dbProps.getProperty("db.url");
        String username = dbProps.getProperty("db.user");
        String password = dbProps.getProperty("db.password");

        conn = DriverManager.getConnection(
                url, username, password);
        conn.setAutoCommit(false);
        ViewManager.setConn(conn);
        dbHelper = ViewManager.getDbHelper();
        }


    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader  = new FXMLLoader(getClass().getResource("/com/example/addressbook/ContactFormView.fxml"));
        Parent root = loader.load();

        controller = loader.getController();
        controller.setDbHelper(dbHelper);
        ViewManager.setStage(stage);

        stage.setScene(new Scene(root));
        stage.show();
        stage.toFront();
    }

    @Test
    public void testEnterName() {
        clickOn("#nameField").write("Test");
        verifyThat("#nameField", hasText("Test"));
    }

    @Test
    public void testFormSubmission() throws Exception{
        clickOn("#nameField").write("Test Contact");
        clickOn("#numberField").write("09124356879");
        clickOn("#emailField").write("test@test.com");
        clickOn("#addressField").write("Test Address");
        controller.submitFormForTesting();

        String sql = "SELECT * FROM address WHERE name = 'Test Contact'";
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        String returnedName = "";
        if (rs.next()){
            returnedName = rs.getString("name");
        }
        assertEquals("Test Contact", returnedName);
    }

    @AfterClass
    public static void tearDown() throws Exception {
        conn.rollback(); // Rollback changes
        conn.close();
    }

}