package com.example.addressbook.controller;

import com.example.addressbook.TestHelper;
import com.example.addressbook.util.DatabaseHelper;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.testfx.framework.junit.ApplicationTest;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import static org.junit.Assert.*;

public class GroupFormControllerTest extends ApplicationTest {
    private static Connection conn;
    private static DatabaseHelper dbHelper;
    private GroupFormController controller;
    private String name = "Test Group";

    @BeforeClass
    public static void setup() throws Exception{
        conn = TestHelper.setupDatabaseConnection();
        ViewManager.setConn(conn);
        dbHelper = ViewManager.getDbHelper();
    }

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader  = new FXMLLoader(getClass().getResource("/com/example/addressbook/GroupFormView.fxml"));
        Parent root = loader.load();
        controller = loader.getController();
        ViewManager.setStage(stage);

        stage.setScene(new Scene(root));
        stage.show();
        stage.toFront();
    }

    @Test
    public void testNewFormSubmission() throws Exception{
        clickOn("#nameField").write(name);
        controller.submitFormForTesting();

        String sql = "SELECT * FROM contact_groups WHERE name = 'Test Group'";
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        String returnedName = "";
        if (rs.next()){
            returnedName = rs.getString("name");
        }
        assertEquals(name, returnedName);
    }

    @After
    public void rollbackAfterEachTest() throws Exception {
        conn.rollback();
    }

    @AfterClass
    public static void tearDown() throws Exception {
        conn.close();
    }

}