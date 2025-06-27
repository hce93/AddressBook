package com.example.addressbook.controller;

import com.example.addressbook.TestHelper;
import com.example.addressbook.model.Contact;
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
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import static org.junit.Assert.assertEquals;
import static org.testfx.api.FxAssert.verifyThat;
import static org.testfx.matcher.control.TextInputControlMatchers.hasText;

public class ContactFormControllerTest extends ApplicationTest {
    private static Connection conn;
    private static DatabaseHelper dbHelper;
    private ContactFormController controller;
    private String name = "Test Contact";
    private String number = "09124356879";
    private String email = "test@test.com";
    private String address = "Test Address";


    @BeforeClass
    public static void setup() throws Exception{
        conn = TestHelper.setupDatabaseConnection();
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
    public void testNewFormSubmission() throws Exception{
        clickOn("#nameField").write(name);
        clickOn("#numberField").write(number);
        clickOn("#emailField").write(email);
        clickOn("#addressField").write(address);
        controller.submitFormForTesting();

        String sql = "SELECT * FROM address WHERE name = 'Test Contact'";
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        String returnedName = "";
        String returnedNumber = "";
        String returnedEmail = "";
        String returnedAddress = "";
        if (rs.next()){
            returnedName = rs.getString("name");
            returnedNumber = rs.getString("number");
            returnedEmail = rs.getString("email");
            returnedAddress = rs.getString("address");
        }
        assertEquals(name, returnedName);
        assertEquals(number, returnedNumber);
        assertEquals(email, returnedEmail);
        assertEquals(address, returnedAddress);
    }

    @Test
    public void testEditFormSubmission() throws Exception {
        String sql = "INSERT INTO address (name, number, email, address) VALUES (?, ?, ?, ?)";
        try(PreparedStatement stmt = ViewManager.getConnection().prepareStatement(
                sql,Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, name);
            stmt.setString(2, number);
            stmt.setString(3, email);
            stmt.setString(4, address);
            stmt.executeUpdate();
        }

        String updatedName = "Test Contact 2";
        clickOn("#nameField").write(updatedName);
        clickOn("#numberField").write(number);
        clickOn("#emailField").write(email);
        clickOn("#addressField").write(address);

        Contact originalContact = new Contact(name, number, email, address);;
        controller.setContactToEdit(originalContact);

        controller.submitFormForTesting();

        sql = "SELECT * FROM address WHERE name = 'Test Contact 2'";
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        String returnedName = "";

        if (rs.next()){
            returnedName = rs.getString("name");
        }
        assertEquals(updatedName, returnedName);
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