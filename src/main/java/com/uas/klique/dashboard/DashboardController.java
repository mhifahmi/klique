package com.uas.klique.dashboard;

import javafx.fxml.FXML;
import javafx.scene.text.Text;
import javafx.scene.image.ImageView;

public class DashboardController {

    // Statistik teks
    @FXML private Text totalPasienText;
    @FXML private Text pasienDalamAntrianText;
    @FXML private Text pasienDipanggilText;
    @FXML private Text pasienDilewatiText;
    @FXML private Text ruanganTersediaText;
    @FXML private Text antrianTerakhirText;

    public void initialize() {
        // Data statis (sementara)
        updateDashboard(
                25,    // total pasien hari ini
                12,    // dalam antrian
                9,     // sudah dipanggil
                3,     // dilewati
                "2/3", // ruangan tersedia
                "19"   // nomor terakhir dipanggil
        );
    }

    private void updateDashboard(int total, int antrian, int dipanggil, int dilewati, String ruang, String antrianTerakhir) {
        totalPasienText.setText(String.valueOf(total));
        pasienDalamAntrianText.setText(String.valueOf(antrian));
        pasienDipanggilText.setText(String.valueOf(dipanggil));
        pasienDilewatiText.setText(String.valueOf(dilewati));
        ruanganTersediaText.setText(ruang);
        antrianTerakhirText.setText(antrianTerakhir);
    }
}
