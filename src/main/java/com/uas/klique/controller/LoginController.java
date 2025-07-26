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

    // Input field untuk username dan password yang dihubungkan dengan elemen FXML
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;

    // Fungsi yang dipanggil saat tombol Login diklik
    @FXML
    public void handleLogin(ActionEvent event) {
        // Mengambil nilai dari input field
        String username = usernameField.getText();
        String password = passwordField.getText();

        // Membuat instance DAO untuk memproses autentikasi user
        UserDao dao = new UserDao();
        Users user = dao.login(username, password); // Melakukan pengecekan login

        // Jika login berhasil
        if (user != null) {
            // Menyimpan informasi user ke dalam sesi (session)
            Session.setCurrentUser(user);

            try {
                // Memuat layout dashboard setelah login berhasil
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/uas/klique/dashboard-layout-view.fxml"));
                Parent dashboardView = loader.load(); // Memuat tampilan dashboard

                // Mengakses controller dashboard untuk mengatur info user
                DashboardLayoutController controller = loader.getController();
                controller.setUserInfo(user); // Menampilkan nama/role user yang login

                // Menampilkan tampilan dashboard
                Scene scene = new Scene(dashboardView);
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.setMaximized(true); // Maksimalkan jendela
                stage.centerOnScreen();
                stage.setScene(scene);
                stage.setTitle("Dashboard - Klinik Hoyong Damang"); // Judul jendela
                stage.show();
            } catch (Exception e) {
                e.printStackTrace(); // Menampilkan error jika gagal memuat tampilan dashboard
            }

        } else {
            // Jika login gagal, tampilkan pesan kesalahan
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Login Gagal");
            alert.setHeaderText("Username atau Password salah");
            alert.setContentText("Silakan coba lagi dengan data yang benar.");
            alert.showAndWait();
        }
    }
}
