package com.uas.klique.controller;

import com.uas.klique.model.Users;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class DashboardLayoutController {

    // Area utama untuk menampilkan konten dari setiap tombol navigasi
    @FXML private StackPane contentPane;

    // Label untuk menampilkan nama dan role pengguna yang login
    @FXML private Label userNameLabel;
    @FXML private Label userRoleLabel;

    // Tombol navigasi sidebar
    @FXML private Button btnDashboard;
    @FXML private Button btnAntrian;
    @FXML private Button btnPasien;
    @FXML private Button btnRuangan;
    @FXML private Button btnRiwayatAntrian;

    // Dipanggil dari LoginController setelah login berhasil
    public void setUserInfo(Users user) {
        userNameLabel.setText(user.getNama()); // Tampilkan nama user
        userRoleLabel.setText(user.getRole()); // Tampilkan peran user
    }

    // Dipanggil otomatis saat FXML diload
    public void initialize() {
        loadView("/com/uas/klique/dashboard-view.fxml"); // Tampilkan dashboard default
        setActiveButton(btnDashboard); // Tandai tombol dashboard sebagai aktif
    }

    // Fungsi untuk memuat tampilan Dashboard
    public void loadDashboard() {
        loadView("/com/uas/klique/dashboard-view.fxml");
        setActiveButton(btnDashboard);
    }

    // Fungsi untuk memuat tampilan Antrian
    public void loadAntrian() {
        loadView("/com/uas/klique/dashboard-antrian-view.fxml");
        setActiveButton(btnAntrian);
    }

    // Fungsi untuk memuat tampilan Data Pasien
    public void loadPasien() {
        loadView("/com/uas/klique/dashboard-pasien-view.fxml");
        setActiveButton(btnPasien);
    }

    // Fungsi untuk memuat tampilan Status Ruangan
    public void loadRuangan() {
        loadView("/com/uas/klique/dashboard-ruangan-view.fxml");
        setActiveButton(btnRuangan);
    }

    // Fungsi untuk memuat tampilan Riwayat Antrian
    public void loadRiwayatAntrian() {
        loadView("/com/uas/klique/dashboard-riwayat-antrian-view.fxml");
        setActiveButton(btnRiwayatAntrian);
    }

    // Fungsi Logout: kembali ke tampilan awal aplikasi
    public void logout() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/uas/klique/main-view.fxml"));
            Scene scene = new Scene(loader.load());

            Stage stage = (Stage) contentPane.getScene().getWindow();
            stage.setMaximized(true);
            stage.centerOnScreen();
            stage.setScene(scene);
            stage.setTitle("Login - Klinik Hoyong Damang");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Fungsi pemuat tampilan ke dalam StackPane contentPane
    private void loadView(String path) {
        try {
            Node view = FXMLLoader.load(getClass().getResource(path));
            contentPane.getChildren().setAll(view); // Ganti isi konten tengah
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Fungsi untuk menandai tombol navigasi yang aktif
    private void setActiveButton(Button activeButton) {
        List<Button> navButtons = List.of(btnDashboard, btnAntrian, btnPasien, btnRuangan, btnRiwayatAntrian);
        for (Button btn : navButtons) {
            if (btn.equals(activeButton)) {
                btn.getStyleClass().add("active"); // Tambahkan class active
            } else {
                btn.getStyleClass().removeIf(s -> s.equals("active")); // Hapus class active dari yang lain
            }
        }
    }
}
