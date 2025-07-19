package com.uas.klique;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.text.Text;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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

    public void initialize() {
        // Set tanggal hari ini
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE, dd/MM/yyyy", new Locale("id", "ID"));
        headerDate.setText(LocalDate.now().format(formatter));

        // Dummy data
        updateNextQueue("08", "Beni Samson", "Dr. Jonathan Joestar");

        updateRoomStatus("Ruang K-1 | Tersedia", "Dr. Jonathan Joestar", room1Status, room1Doctor);
        updateRoomStatus("Ruang K-2 | Istirahat", "-", room2Status, room2Doctor);
        updateRoomStatus("Ruang K-3 | Konsultasi", "Dr. Timothy Selamet", room3Status, room3Doctor);

        updateQueueStats(9, 6, 10);
    }

    private void updateNextQueue(String nomor, String nama, String dokter) {
        nextQueueNumber.setText(nomor);
        nextQueueName.setText(nama);
        nextQueueDoctor.setText(dokter);
    }

    private void updateRoomStatus(String status, String dokter, Text statusText, Text doctorText) {
        statusText.setText(status);
        doctorText.setText(dokter);
    }

    private void updateQueueStats(int menunggu, int terlewat, int kosong) {
        waitingQueueCount.setText(String.format("%02d", menunggu));
        missedQueueCount.setText(String.format("%02d", terlewat));
        emptyQueueCount.setText(String.format("%02d", kosong));
    }
}
