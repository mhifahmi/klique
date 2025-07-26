package com.uas.klique.controller;

import com.uas.klique.dao.DokterDao;
import com.uas.klique.dao.PasienDao;
import com.uas.klique.dao.RuanganDao;
import com.uas.klique.model.Antrian;
import com.uas.klique.model.Ruangan;
import com.uas.klique.service.AntrianService;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class DashboardAntrianController {

    @FXML private TextField fieldNamaPasien;               // Input nama pasien untuk ditambahkan ke antrian
    @FXML private TableView<Antrian> tableAntrian;         // Tabel utama menampilkan data antrian
    @FXML private TableColumn<Antrian, Void> colNo;        // Kolom nomor urut
    @FXML private TableColumn<Antrian, String> colNama, colStatus, colRuang, colAksi;
    @FXML private TableColumn<Antrian, Integer> colNomor;
    @FXML private ListView<String> listPasienSuggestion;   // Menampilkan saran nama pasien (autocomplete)
    @FXML private TextField searchAntrianField;            // Kolom pencarian
    @FXML private Text textTerakhirDipanggilNomor, textTerakhirDipanggilNama, textAntrianSelanjutnya, textAntrianTerlewat;

    private final AntrianService antrianService = new AntrianService(); // Service utama untuk logika antrian
    private final PasienDao pasienDao = new PasienDao();                // DAO pasien
    private final RuanganDao ruanganDao = new RuanganDao();             // DAO ruangan

    private Integer selectedPasienId = null;        // ID pasien yang dipilih dari autocomplete
    private Integer selectedDokterId = null;        // (tidak digunakan saat ini)
    private Boolean isRuangTersedia = null;         // Status apakah ada ruangan yang tersedia

    private ObservableList<Antrian> masterAntrianList = FXCollections.observableArrayList();
    private FilteredList<Antrian> filteredAntrianList;

    @FXML
    public void initialize() {
        searchPasienProcess(); // Menyusun logika autocomplete pasien

        // Inisialisasi kolom
        colNo.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(Void item, boolean empty) { // Menampilkan urutan baris
                super.updateItem(item, empty);
                if (empty) {
                    setText(null);
                } else {
                    setText(String.valueOf(getIndex() + 1));
                }
            }
        });
        colNo.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(null));
        // Ambil nilai properti dari model Antrian
        colNama.setCellValueFactory(new PropertyValueFactory<>("namaPasien"));
        colNomor.setCellValueFactory(new PropertyValueFactory<>("nomorAntrian"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        colRuang.setCellValueFactory(new PropertyValueFactory<>("namaDokter"));

        // Kolom aksi: tombol dinamis berdasarkan status antrian
        colAksi.setCellFactory(tc -> new TableCell<>() {
            private final Button btn = new Button();

            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                    return;
                }

                Antrian antrian = getTableView().getItems().get(getIndex());
                String status = antrian.getStatus();

                btn.setMaxWidth(Double.MAX_VALUE); // full-width
                btn.setStyle("-fx-alignment: center;"); // opsional gaya tambahan
                setContentDisplay(ContentDisplay.GRAPHIC_ONLY); // agar button full cell
                setAlignment(Pos.CENTER); // tombol tetap di tengah cell
                btn.getStyleClass().add("table-button");

                switch (status) {
                    case "Menunggu" -> {
                        btn.setText("Panggil");
                        btn.setDisable(!isRuangTersedia);
                        btn.setOnAction(e -> {
                            if (isRuangTersedia) ubahDipanggil(antrian);
                        });
                    }
                    case "Dipanggil" -> {
                        btn.setText("Dilayani");
                        btn.setDisable(false);
                        btn.setOnAction(e -> ubahDilayani(antrian));
                    }
                    case "Dilayani" -> {
                        btn.setText("Selesai");
                        btn.setDisable(false);
                        btn.setOnAction(e -> ubahSelesai(antrian));
                    }
                    case "Selesai" -> {
                        btn.setText("Selesai");
                        btn.setDisable(true);
                        btn.setOnAction(null);
                    }
                    case "Terlewat" -> {
                        btn.setText("Panggil ulang");
                        btn.setDisable(!isRuangTersedia);
                        btn.setOnAction(e -> {
                            if (isRuangTersedia) ubahDipanggil(antrian);
                        });
                    }
                }

                setGraphic(btn);
            }
        });

        // Atur lebar kolom secara proporsional
        colNo.prefWidthProperty().bind(tableAntrian.widthProperty().multiply(0.05));
        colNama.prefWidthProperty().bind(tableAntrian.widthProperty().multiply(0.30));
        colNomor.prefWidthProperty().bind(tableAntrian.widthProperty().multiply(0.148));
        colStatus.prefWidthProperty().bind(tableAntrian.widthProperty().multiply(0.15));
        colRuang.prefWidthProperty().bind(tableAntrian.widthProperty().multiply(0.15));
        colAksi.prefWidthProperty().bind(tableAntrian.widthProperty().multiply(0.20));

        refreshTable(); // Isi data tabel dan statistik
    }

    private void refreshTable() {
        // Ambil semua antrian hari ini
        List<Antrian> list = antrianService.getAntrianHariIni();
        masterAntrianList.setAll(list);
        filteredAntrianList = new FilteredList<>(masterAntrianList, p -> true);
        tableAntrian.setItems(filteredAntrianList);

        // Menentukan status antrian untuk kartu statistik
        Antrian terakhirDipanggil = list.stream()
                .filter(a -> "Dipanggil".equals(a.getStatus()))
                .findFirst().orElse(null);

        Antrian antrianSelanjutnya = list.stream()
                .filter(a -> "Menunggu".equals(a.getStatus()))
                .findFirst().orElse(null);

        Antrian antrianTerlewat = list.stream()
                .filter(a -> "Terlewat".equals(a.getStatus()))
                .findFirst().orElse(null);

        // Update teks tampilan UI
        textTerakhirDipanggilNomor.setText(terakhirDipanggil != null ? String.valueOf(terakhirDipanggil.getNomorAntrian()) : "—");
        textTerakhirDipanggilNama.setText(terakhirDipanggil != null ? terakhirDipanggil.getNamaPasien() : "—");

        textAntrianSelanjutnya.setText(antrianSelanjutnya != null ? String.valueOf(antrianSelanjutnya.getNomorAntrian()) : "—");
        textAntrianTerlewat.setText(antrianTerlewat != null ? String.valueOf(antrianTerlewat.getNomorAntrian()) : "—");

        // Table
        masterAntrianList.setAll(antrianService.getAntrianHariIni());
        filteredAntrianList = new FilteredList<>(masterAntrianList, p -> true);

        // Atur listener filter pencarian
        searchAntrianField.textProperty().addListener((obs, oldVal, newVal) -> {
            filteredAntrianList.setPredicate(antrian -> {
                // Filter berdasarkan nama atau nomor
                if (newVal == null || newVal.isBlank()) return true;
                String filter = newVal.toLowerCase();

                return antrian.getNamaPasien().toLowerCase().contains(filter) ||
                        String.valueOf(antrian.getNomorAntrian()).contains(filter);
            });
        });

        isRuangTersedia = ruanganDao.adaRuanganTersedia();
        tableAntrian.setItems(filteredAntrianList);
    }

    @FXML
    public void tambahAntrian() {
        if (selectedPasienId == null) {
            showAlert("Harap pilih pasien dari daftar.");
            return;
        }

        int idPasien = selectedPasienId;
        Integer idDokter = selectedDokterId; // Bisa null

        // Ambil nomor antrian terakhir hari ini
        int nomorBaru = antrianService.getNextNomorAntrianHariIni();

        // Buat entitas Antrian
        Antrian antrian = new Antrian();
        antrian.setNomorAntrian(nomorBaru);
        antrian.setIdPasien(idPasien);
        antrian.setIdRuangan(0); // Auto assign backend
        antrian.setStatus("Menunggu");
        antrian.setTanggal(LocalDate.now().toString());

        // Simpan ke DB
        antrianService.simpanAntrianBaru(antrian);

        // Reset input
        selectedPasienId = null;
        selectedDokterId = null;
        fieldNamaPasien.clear();
        refreshTable();
    }

    public void searchPasienProcess(){
        fieldNamaPasien.textProperty().addListener((obs, oldText, newText) -> {
            if (newText.length() < 2) {
                listPasienSuggestion.setVisible(false);
                return;
            }

            // Cari nama + ID
            Map<Integer, String> suggestions = pasienDao.searchNamaPasienWithId(newText);
            if (!suggestions.isEmpty()) {
                listPasienSuggestion.getItems().setAll(suggestions.values());
                listPasienSuggestion.setUserData(suggestions);
                listPasienSuggestion.setVisible(true);
            } else {
                listPasienSuggestion.setVisible(false);
            }
        });

        listPasienSuggestion.setOnMouseClicked(e -> {
            String selectedName = listPasienSuggestion.getSelectionModel().getSelectedItem();
            if (selectedName != null) {
                @SuppressWarnings("unchecked")
                Map<Integer, String> map = (Map<Integer, String>) listPasienSuggestion.getUserData();
                for (Map.Entry<Integer, String> entry : map.entrySet()) {
                    if (entry.getValue().equals(selectedName)) {
                        selectedPasienId = entry.getKey();
                        break;
                    }
                }
                fieldNamaPasien.setText(selectedName);
                listPasienSuggestion.setVisible(false);
            }
        });
    }

    private void showAlert(String msg) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Peringatan");
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }

    private void ubahDipanggil(Antrian antrian) {
        // Set semua antrian yang sedang "Dipanggil" menjadi "Terlewat"
        // Tetapkan ruangan baru untuk antrian yang akan dipanggil
        // Update status & refresh

        // Cari ruangan yang tersedia
        Ruangan ruanganTersedia = ruanganDao.getRuanganTersedia();
        if (ruanganTersedia == null) {
            System.out.println("Tidak ada ruangan yang tersedia.");
            return; // tombol akan tetap disabled jika logika disesuaikan di CellFactory
        }

        // Semua antrian "Dipanggil" lainnya → "Terlewat"
        for (Antrian a : tableAntrian.getItems()) {
            if (a.getStatus().equals("Dipanggil") && a.getId() != antrian.getId()) {
                a.setStatus("Terlewat");
                antrianService.updateTerlewat(a.getId());
            }
        }

        // Assign ruangan
        antrian.setIdRuangan(ruanganTersedia.getId());
        antrian.setStatus("Dipanggil");
        antrianService.updateStatusDanRuangan(antrian.getId(), "Dipanggil", ruanganTersedia.getId());

        refreshTable();
    }

    private void ubahDilayani(Antrian antrian) {
        // Set status menjadi "Dilayani"
        // Update dokter_id dan status ruangan ke "Konsultasi"

        antrian.setStatus("Dilayani");
        antrianService.updateStatus(antrian.getId(), "Dilayani");

        // Update dokter_id saat mulai konsultasi
        Ruangan ruangan = ruanganDao.getById(antrian.getIdRuangan());
        if (ruangan != null) {
            antrianService.updateDokterId(antrian.getId(), ruangan.getIdDokter());
        }

        // Ubah status ruangan ke "Konsultasi"
        if (antrian.getIdRuangan() != 0) {
            ruanganDao.updateStatusRuangan(antrian.getIdRuangan(), "Konsultasi");
        }

        refreshTable();
    }


    private void ubahSelesai(Antrian antrian) {
        // Set status menjadi "Selesai"
        // Ubah ruangan menjadi "Tersedia"
        // Panggil antrian berikutnya (jika ada)

        antrian.setStatus("Selesai");
        antrianService.updateStatus(antrian.getId(), "Selesai");
        if (antrian.getIdRuangan() != 0) {
            ruanganDao.updateStatusRuangan(antrian.getIdRuangan(), "Tersedia");
        }
        panggilAntrianBerikutnyaSetelahSelesai();
        refreshTable();
    }

    @FXML
    private void panggilAntrianSelanjutnya() {
        // Ambil antrian dengan status "Menunggu" pertama, lalu ubah ke "Dipanggil"

        List<Antrian> list = masterAntrianList.stream()
                .filter(a -> "Menunggu".equals(a.getStatus()))
                .sorted(Comparator.comparingInt(Antrian::getNomorAntrian))
                .toList();

        if (!list.isEmpty()) {
            ubahDipanggil(list.get(0));
        }
    }

    @FXML
    private void panggilAntrianTerlewat() {
        // Ambil antrian dengan status "Terlewat" pertama, lalu ubah ke "Dipanggil"

        List<Antrian> list = masterAntrianList.stream()
                .filter(a -> "Terlewat".equals(a.getStatus()))
                .sorted(Comparator.comparingInt(Antrian::getNomorAntrian))
                .toList();

        if (!list.isEmpty()) {
            ubahDipanggil(list.get(0));
        }
    }

    private void panggilAntrianBerikutnyaSetelahSelesai() {
        // Prioritaskan antrian "Terlewat", lalu "Menunggu"

        List<Antrian> terlewat = masterAntrianList.stream()
                .filter(a -> "Terlewat".equals(a.getStatus()))
                .sorted(Comparator.comparingInt(Antrian::getNomorAntrian))
                .toList();

        if (!terlewat.isEmpty()) {
            ubahDipanggil(terlewat.get(0));
            return;
        }

        List<Antrian> menunggu = masterAntrianList.stream()
                .filter(a -> "Menunggu".equals(a.getStatus()))
                .sorted(Comparator.comparingInt(Antrian::getNomorAntrian))
                .toList();

        if (!menunggu.isEmpty()) {
            ubahDipanggil(menunggu.get(0));
        }
    }


}
