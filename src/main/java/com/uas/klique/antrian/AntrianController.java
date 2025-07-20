package com.uas.klique.antrian;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

public class AntrianController {

    @FXML private TextField fieldNamaPasien;
    @FXML private TextField fieldDokter;
    @FXML private TableView<AntrianData> tableAntrian;

    private final ObservableList<AntrianData> dataAntrian = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        // Setup kolom tabel
        TableColumn<AntrianData, Integer> colNo = new TableColumn<>("No");
        colNo.setCellValueFactory(new PropertyValueFactory<>("no"));

        TableColumn<AntrianData, String> colNama = new TableColumn<>("Nama Pengunjung");
        colNama.setCellValueFactory(new PropertyValueFactory<>("nama"));

        TableColumn<AntrianData, Integer> colNomor = new TableColumn<>("Nomor Antrian");
        colNomor.setCellValueFactory(new PropertyValueFactory<>("nomor"));

        TableColumn<AntrianData, String> colStatus = new TableColumn<>("Status");
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));

        TableColumn<AntrianData, String> colRuang = new TableColumn<>("Ruang");
        colRuang.setCellValueFactory(new PropertyValueFactory<>("ruang"));

        TableColumn<AntrianData, String> colAksi = new TableColumn<>("Aksi");
        colAksi.setCellValueFactory(new PropertyValueFactory<>("aksi"));

        tableAntrian.getColumns().addAll(colNo, colNama, colNomor, colStatus, colRuang, colAksi);

        // Data Dummy
        dataAntrian.addAll(
                new AntrianData(1, "Asep Bahri", 17, "Terlewat", "-", "Panggil Ulang"),
                new AntrianData(2, "Roger Sumatra", 19, "Konsultasi", "K-1", "Selesai"),
                new AntrianData(3, "Trimurti Subakja", 20, "Menunggu", "-", "Panggil"),
                new AntrianData(4, "Adit Tias Dijayanti", 21, "Menunggu", "K-1", "Panggil"),
                new AntrianData(5, "Rafly Moch. Bahari", 16, "Selesai", "K3", "Selesai")
        );

        tableAntrian.setItems(dataAntrian);
    }

    @FXML
    public void tambahAntrian() {
        String nama = fieldNamaPasien.getText().trim();
        String dokter = fieldDokter.getText().trim();
        if (!nama.isEmpty()) {
            int nomor = 100 + dataAntrian.size(); // Simulasi nomor antrian
            dataAntrian.add(new AntrianData(dataAntrian.size() + 1, nama, nomor, "Menunggu", "-", "Panggil"));
            fieldNamaPasien.clear();
            fieldDokter.clear();
        }
    }

    // Model
    public static class AntrianData {
        private final Integer no;
        private final String nama;
        private final Integer nomor;
        private final String status;
        private final String ruang;
        private final String aksi;

        public AntrianData(Integer no, String nama, Integer nomor, String status, String ruang, String aksi) {
            this.no = no;
            this.nama = nama;
            this.nomor = nomor;
            this.status = status;
            this.ruang = ruang;
            this.aksi = aksi;
        }

        public Integer getNo() { return no; }
        public String getNama() { return nama; }
        public Integer getNomor() { return nomor; }
        public String getStatus() { return status; }
        public String getRuang() { return ruang; }
        public String getAksi() { return aksi; }
    }
}
