package com.uas.klique;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;

public class MainController {

    @FXML
    private void handleLihatAntrian(MouseEvent event) {
        try {
            Parent antrianView = FXMLLoader.load(getClass().getResource("/com/uas/klique/antrian-view.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(antrianView, 1280, 720));
            stage.setTitle("Lihat Antrian - Klique");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleLogin(MouseEvent event) {
        try {
            Parent loginView = FXMLLoader.load(getClass().getResource("/com/uas/klique/login-view.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(loginView, 1280, 720));
            stage.setTitle("Login - Klique");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
