package com.example.bdsqltester.scenes;

import com.example.bdsqltester.HelloApplication;
import com.example.bdsqltester.datasources.DataSourceManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private ComboBox<String> roleComboBox;

    private int userId;

    public int getUserId() {
        return userId;
    }

    @FXML
    void initialize() {
        roleComboBox.getItems().addAll("User", "Admin Cabang", "Admin Pusat");
        roleComboBox.setValue("User");
    }

    private boolean verifyCredentials(String username, String password, String role) throws SQLException {
        try (Connection c = DataSourceManager.getUserConnection()) {
            PreparedStatement stmt = c.prepareStatement("SELECT * FROM users WHERE username = ? AND role = ?");
            stmt.setString(1, username);
            stmt.setString(2, role.toLowerCase());

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String dbPassword = rs.getString("password_hash");
                if (dbPassword.equals(password)) {
                    this.userId = rs.getInt("user_id");
                    return true;
                }
            }
        }
        return false;
    }

    @FXML
    void handleLogin(ActionEvent event) {
        String username = usernameField.getText();
        String password = passwordField.getText();
        String role = roleComboBox.getValue();

        try {
            if (verifyCredentials(username, password, role)) {
                HelloApplication app = HelloApplication.getApplicationInstance();
                app.setUserId(this.userId);

                FXMLLoader loader;

                switch (role) {
                    case "User":
                        loader = new FXMLLoader(HelloApplication.class.getResource("user-view.fxml"));
                        app.getPrimaryStage().setTitle("User View");
                        break;
                    case "Admin Cabang":
                        loader = new FXMLLoader(HelloApplication.class.getResource("branch-admin-view.fxml"));
                        app.getPrimaryStage().setTitle("Admin Cabang View");
                        break;
                    case "Admin Pusat":
                        loader = new FXMLLoader(HelloApplication.class.getResource("central-admin-view.fxml"));
                        app.getPrimaryStage().setTitle("Admin Pusat View");
                        break;
                    default:
                        throw new IllegalStateException("Unexpected role: " + role);
                }

                Scene scene = new Scene(loader.load());
                app.getPrimaryStage().setScene(scene);
                app.getPrimaryStage().sizeToScene();

            } else {
                showAlert(Alert.AlertType.ERROR, "Login Failed", "Invalid Credentials", "Please check your username and password.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Database Error", "Connection Failed", "Could not connect to the database.");
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Load Error", "Scene Failed", "Could not load the next scene.");
        }
    }

    private void showAlert(Alert.AlertType type, String title, String header, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
