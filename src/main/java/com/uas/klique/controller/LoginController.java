package com.uas.klique.controller;

import com.uas.klique.dao.UserDao;
import com.uas.klique.model.Users;
import com.uas.klique.util.Session;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.event.ActionEvent;

public class LoginController {

    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;

    @FXML
    public void handleLogin(ActionEvent event) {
        String username = usernameField.getText();
        String password = passwordField.getText();

        UserDao dao = new UserDao();
        Users user = dao.login(username, password);

        if (user != null) {
            Session.setCurrentUser(user);

            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/uas/klique/dashboard-layout-view.fxml"));
                Parent dashboardView = loader.load(); // load() hanya sekali
                DashboardLayoutController controller = loader.getController();
                controller.setUserInfo(user);

                Scene scene = new Scene(dashboardView, 1440, 768);

                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.setScene(scene);
                stage.setTitle("Dashboard - Klinik Hoyong Damang");
                stage.show();
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Login Gagal");
            alert.setHeaderText("Username atau Password salah");
            alert.setContentText("Silakan coba lagi dengan data yang benar.");
            alert.showAndWait();
        }
    }

}
