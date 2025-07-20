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

    // Header
    @FXML private Text headerDate;

    // Next Queue
    @FXML private Text nextQueueNumber;
    @FXML private Text nextQueueName;
    @FXML private Label nextQueueDoctor;

    // Room Statuses
    @FXML private Text room1Status;
    @FXML private Text room1Doctor;
    @FXML private Text room2Status;
    @FXML private Text room2Doctor;
    @FXML private Text room3Status;
    @FXML private Text room3Doctor;

    // Queue Stats
    @FXML private Text waitingQueueCount;
    @FXML private Text missedQueueCount;
    @FXML private Text emptyQueueCount;

    @FXML private TableView<Antrian> antrianTable;
    @FXML private TableColumn<Antrian, String> colNomor;
    @FXML private TableColumn<Antrian, String> colPasien;
    @FXML private TableColumn<Antrian, String> colDokter;
    @FXML private TableColumn<Antrian, String> colStatus;


    private final AntrianService antrianService = new AntrianService();
    private final RuanganService ruanganService = new RuanganService();

    public void initialize() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE, dd/MM/yyyy", new Locale("id", "ID"));
        headerDate.setText(LocalDate.now().format(formatter));

        // Ambil antrian selanjutnya
        Antrian next = antrianService.getAntrianSelanjutnya();
        if (next != null) {
            updateNextQueue(
                    String.format("%02d", next.getNomorAntrian()),
                    next.getNamaPasien(),
                    next.getNamaDokter() != null ? "Dr. " + next.getNamaDokter() : "-"
            );
        }

        // Dummy ruangan (masih manual)
        List<Ruangan> ruanganList = ruanganService.getAllRuangan();
        if (ruanganList.size() >= 3) {
            updateRoomStatus(ruanganList.get(0), room1Status, room1Doctor);
            updateRoomStatus(ruanganList.get(1), room2Status, room2Doctor);
            updateRoomStatus(ruanganList.get(2), room3Status, room3Doctor);
        }

        updateQueueStats(
                (int) (antrianService.countByStatus("Menunggu") + antrianService.countByStatus("Terlewat")),
                (int) antrianService.countByStatus("Terlewat"),
                (int) antrianService.countByStatus("Selesai")
        );

        colNomor.setCellValueFactory(data -> new SimpleStringProperty(
                String.format("%02d", data.getValue().getNomorAntrian())));
        colPasien.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getNamaPasien()));

        colNomor.prefWidthProperty().bind(antrianTable.widthProperty().multiply(0.2));
        colPasien.prefWidthProperty().bind(antrianTable.widthProperty().multiply(0.8));

        antrianTable.getItems().setAll(antrianService.getMenungguAntrianHariIni());
    }

    private void updateNextQueue(String nomor, String nama, String dokter) {
        nextQueueNumber.setText(nomor);
        nextQueueName.setText(nama);
        nextQueueDoctor.setText(dokter);
    }

    private void updateRoomStatus(Ruangan r, Text statusText, Text doctorText) {
        statusText.setText(r.getNamaRuangan() + " | " + r.getStatus());
        doctorText.setText(r.getNamaDokter() != null ? "Dr. " + r.getNamaDokter() : "-");
    }

    private void updateQueueStats(int menunggu, int terlewat, int kosong) {
        waitingQueueCount.setText(String.format("%02d", menunggu));
        missedQueueCount.setText(String.format("%02d", terlewat));
        emptyQueueCount.setText(String.format("%02d", kosong));
    }
}
