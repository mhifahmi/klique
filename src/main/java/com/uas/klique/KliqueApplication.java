package com.uas.klique;

import com.uas.klique.db.Seeder;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.IOException;

public class KliqueApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        // Menyisipkan data awal ke database jika dibutuhkan (sementara dinonaktifkan)
        // Seeder.seed();

        // Memuat font kustom (Poppins) dari resource agar digunakan di seluruh aplikasi
        Font.loadFont(getClass().getResource("/com/uas/klique/fonts/Poppins-Regular.ttf").toExternalForm(), 12);

        // Memuat file tampilan awal (FXML)
        FXMLLoader fxmlLoader = new FXMLLoader(KliqueApplication.class.getResource("main-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());

        // Mengatur tampilan jendela aplikasi
        stage.setMaximized(true); // Membuka dalam mode layar penuh
        stage.centerOnScreen(); // membuat konten ada di tengah
        stage.setTitle("Klique!"); // Judul jendela aplikasi (dapat diubah sesuai kebutuhan)
        stage.setScene(scene);    // Menetapkan scene utama
        stage.show();             // Menampilkan jendela
    }

    public static void main(String[] args) {
        launch(); // Menjalankan aplikasi JavaFX
    }
}
