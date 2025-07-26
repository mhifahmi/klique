package com.uas.klique.controller;

import com.uas.klique.model.Antrian;
import com.uas.klique.model.Ruangan;
import com.uas.klique.service.AntrianService;
import com.uas.klique.service.RuanganService;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.text.Text;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

public class AntrianController {

    // ====== Komponen UI Header ======
    @FXML private Text headerDate;

    // ====== Komponen UI Antrian Selanjutnya ======
    @FXML private Text nextQueueNumber;
    @FXML private Text nextQueueName;
    @FXML private Label nextQueueDoctor;

    // ====== Komponen UI Status Tiap Ruangan ======
    @FXML private Text room1Status;
    @FXML private Text room1Doctor;
    @FXML private Text room2Status;
    @FXML private Text room2Doctor;
    @FXML private Text room3Status;
    @FXML private Text room3Doctor;

    // ====== Komponen UI Statistik Antrian ======
    @FXML private Text waitingQueueCount;
    @FXML private Text missedQueueCount;
    @FXML private Text emptyQueueCount;

    // ====== Tabel Antrian Hari Ini ======
    @FXML private TableView<Antrian> antrianTable;
    @FXML private TableColumn<Antrian, String> colNomor;
    @FXML private TableColumn<Antrian, String> colPasien;

    // ====== Service Layer ======
    private final AntrianService antrianService = new AntrianService();
    private final RuanganService ruanganService = new RuanganService();

    public void initialize() {
        // Format dan tampilkan tanggal hari ini di header
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE, dd/MM/yyyy", new Locale("id", "ID"));
        headerDate.setText(LocalDate.now().format(formatter));

        // Ambil dan tampilkan data antrian selanjutnya (jika ada)
        Antrian next = antrianService.getAntrianSelanjutnya();
        if (next != null) {
            updateNextQueue(
                    String.format("%02d", next.getNomorAntrian()),
                    next.getNamaPasien(),
                    next.getNamaDokter() != null ? "Dr. " + next.getNamaDokter() : "-"
            );
        }

        // Ambil dan tampilkan status 3 ruangan teratas
        List<Ruangan> ruanganList = ruanganService.getAllRuangan();
        if (ruanganList.size() >= 3) {
            updateRoomStatus(ruanganList.get(0), room1Status, room1Doctor);
            updateRoomStatus(ruanganList.get(1), room2Status, room2Doctor);
            updateRoomStatus(ruanganList.get(2), room3Status, room3Doctor);
        }

        // Tampilkan statistik jumlah antrian berdasarkan status
        updateQueueStats(
                (int) (antrianService.countByStatus("Menunggu") + antrianService.countByStatus("Terlewat")),
                (int) antrianService.countByStatus("Terlewat"),
                (int) antrianService.countByStatus("Selesai")
        );

        // Inisialisasi kolom tabel: Nomor Antrian dan Nama Pasien
        colNomor.setCellValueFactory(data ->
                new SimpleStringProperty(String.format("%02d", data.getValue().getNomorAntrian()))
        );
        colPasien.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getNamaPasien())
        );

        // Atur lebar kolom tabel secara proporsional
        colNomor.prefWidthProperty().bind(antrianTable.widthProperty().multiply(0.2));
        colPasien.prefWidthProperty().bind(antrianTable.widthProperty().multiply(0.8));

        // Tampilkan daftar antrian "Menunggu" hari ini
        antrianTable.getItems().setAll(antrianService.getMenungguAntrianHariIni());
    }

    // Fungsi untuk memperbarui tampilan panel "Antrian Selanjutnya"
    private void updateNextQueue(String nomor, String nama, String dokter) {
        nextQueueNumber.setText(nomor);
        nextQueueName.setText(nama);
        nextQueueDoctor.setText(dokter);
    }

    // Fungsi untuk memperbarui status ruangan (nama dan dokter)
    private void updateRoomStatus(Ruangan r, Text statusText, Text doctorText) {
        statusText.setText(r.getNamaRuangan() + " | " + r.getStatus());
        doctorText.setText(r.getNamaDokter() != null ? "Dr. " + r.getNamaDokter() : "-");
    }

    // Fungsi untuk memperbarui statistik jumlah antrian
    private void updateQueueStats(int menunggu, int terlewat, int kosong) {
        waitingQueueCount.setText(String.format("%02d", menunggu));
        missedQueueCount.setText(String.format("%02d", terlewat));
        emptyQueueCount.setText(String.format("%02d", kosong));
    }
}
