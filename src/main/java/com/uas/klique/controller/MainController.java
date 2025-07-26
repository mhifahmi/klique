package com.uas.klique.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;

public class MainController {

    // Fungsi ini dipanggil ketika pengguna memilih "Lihat Antrian" pada menu utama
    @FXML
    private void handleLihatAntrian(MouseEvent event) {
        try {
            // Memuat file antrian-view.fxml sebagai tampilan baru
            Parent antrianView = FXMLLoader.load(getClass().getResource("/com/uas/klique/antrian-view.fxml"));

            // Mendapatkan stage dari event (klik)
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            // Menampilkan tampilan baru di stage yang sama
            stage.setScene(new Scene(antrianView));
            stage.setMaximized(true);
            stage.centerOnScreen();
            stage.setTitle("Lihat Antrian - Klique");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace(); // Menampilkan error jika FXML gagal dimuat
        }
    }

    // Fungsi ini dipanggil ketika pengguna memilih "Login" pada menu utama
    @FXML
    private void handleLogin(MouseEvent event) {
        try {
            // Memuat file login-view.fxml sebagai tampilan login
            Parent loginView = FXMLLoader.load(getClass().getResource("/com/uas/klique/login-view.fxml"));

            // Mendapatkan stage dari event (klik)
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            // Menampilkan tampilan login di stage yang sama
            stage.setScene(new Scene(loginView));
            stage.setMaximized(true);
            stage.centerOnScreen();
            stage.setTitle("Login - Klique");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace(); // Menampilkan error jika FXML gagal dimuat
        }
    }
}
