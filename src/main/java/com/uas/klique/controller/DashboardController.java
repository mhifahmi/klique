package com.uas.klique.controller;

import com.uas.klique.dao.AntrianDao;
import com.uas.klique.dao.DokterDao;
import com.uas.klique.dao.PasienDao;
import com.uas.klique.dao.RuanganDao;
import javafx.fxml.FXML;
import javafx.scene.text.Text;

import java.time.LocalDate;

public class DashboardController {

    @FXML private Text totalPasienText;
    @FXML private Text pasienDalamAntrianText;
    @FXML private Text pasienDipanggilText;
    @FXML private Text pasienDilewatiText;
    @FXML private Text ruanganTersediaText;
    @FXML private Text antrianTerakhirText;

    private final AntrianDao antrianDao = new AntrianDao();
    private final PasienDao pasienDao = new PasienDao();
    private final RuanganDao ruanganDao = new RuanganDao();

    public void initialize() {
        String today = LocalDate.now().toString();

        int totalPasienHariIni = antrianDao.countTotalPasienHariIni(today);
        int dalamAntrian = antrianDao.countByStatusAndTanggal("Menunggu", today);
        int sudahDipanggil = antrianDao.countByStatusAndTanggal("Selesai", today);
        int dilewati = antrianDao.countByStatusAndTanggal("Terlewat", today);
        int ruanganTersedia = ruanganDao.countByStatus("Tersedia");
        int totalRuangan = ruanganDao.countAll();
        int nomorAntrianTerakhir = antrianDao.getMaxNomorAntrianByTanggal(today);

        updateDashboard(
                totalPasienHariIni,
                dalamAntrian,
                sudahDipanggil,
                dilewati,
                ruanganTersedia + "/" + totalRuangan,
                nomorAntrianTerakhir == -1 ? "-" : String.valueOf(nomorAntrianTerakhir)
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
