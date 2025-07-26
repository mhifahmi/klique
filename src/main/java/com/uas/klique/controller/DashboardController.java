package com.uas.klique.controller;

import com.uas.klique.dao.AntrianDao;
import com.uas.klique.dao.PasienDao;
import com.uas.klique.dao.RuanganDao;
import javafx.fxml.FXML;
import javafx.scene.text.Text;

import java.time.LocalDate;

public class DashboardController {

    // Komponen UI yang menampilkan statistik dashboard
    @FXML private Text totalPasienText;
    @FXML private Text pasienDalamAntrianText;
    @FXML private Text pasienDipanggilText;
    @FXML private Text pasienDilewatiText;
    @FXML private Text ruanganTersediaText;
    @FXML private Text antrianTerakhirText;

    // DAO untuk akses database
    private final AntrianDao antrianDao = new AntrianDao();
    private final PasienDao pasienDao = new PasienDao();
    private final RuanganDao ruanganDao = new RuanganDao();

    // Dipanggil saat FXML diload pertama kali
    public void initialize() {
        String today = LocalDate.now().toString(); // Tanggal hari ini

        // Mengambil data statistik antrian berdasarkan status dan tanggal
        int totalPasienHariIni = antrianDao.countTotalPasienHariIni(today);
        int dalamAntrian = antrianDao.countByStatusAndTanggal("Menunggu", today);
        int sudahDipanggil = antrianDao.countByStatusAndTanggal("Selesai", today);
        int dilewati = antrianDao.countByStatusAndTanggal("Terlewat", today);

        // Informasi ketersediaan ruangan
        int ruanganTersedia = ruanganDao.countByStatus("Tersedia");
        int totalRuangan = ruanganDao.countAll();

        // Nomor antrian tertinggi yang telah digunakan
        int nomorAntrianTerakhir = antrianDao.getMaxNomorAntrianByTanggal(today);

        // Update tampilan dashboard
        updateDashboard(
                totalPasienHariIni,
                dalamAntrian,
                sudahDipanggil,
                dilewati,
                ruanganTersedia + "/" + totalRuangan,
                nomorAntrianTerakhir == -1 ? "-" : String.valueOf(nomorAntrianTerakhir)
        );
    }

    // Fungsi untuk menampilkan data ke UI
    private void updateDashboard(int total, int antrian, int dipanggil, int dilewati, String ruang, String antrianTerakhir) {
        totalPasienText.setText(String.valueOf(total));
        pasienDalamAntrianText.setText(String.valueOf(antrian));
        pasienDipanggilText.setText(String.valueOf(dipanggil));
        pasienDilewatiText.setText(String.valueOf(dilewati));
        ruanganTersediaText.setText(ruang);
        antrianTerakhirText.setText(antrianTerakhir);
    }
}
