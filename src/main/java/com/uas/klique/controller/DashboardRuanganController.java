package com.uas.klique.controller;

import com.uas.klique.dao.DokterDao;
import com.uas.klique.dao.RuanganDao;
import com.uas.klique.model.Dokter;
import com.uas.klique.model.Ruangan;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DashboardRuanganController {

    // Tabel Ruangan
    @FXML private TableView<Ruangan> tableRuangan;
    @FXML private TableColumn<Ruangan, Integer> colNo;
    @FXML private TableColumn<Ruangan, String> colNamaRuangan, colNamaDokter, colStatus, colTglLahir, colAksi;

    // Tabel Dokter
    @FXML private TableView<Dokter> tableDokter;
    @FXML private TableColumn<Dokter, Integer> colNoDokter;
    @FXML private TableColumn<Dokter, String> colNamaDokterTable, colNikDokter, colTelpDokter, colTglLahirDokter;

    // ComboBox input form
    @FXML private ComboBox<String> comboStatus;
    @FXML private ComboBox<String> comboDokter;

    // Data
    private final ObservableList<Ruangan> ruanganList = FXCollections.observableArrayList();
    private final ObservableList<Dokter> dokterList = FXCollections.observableArrayList();
    private final Map<String, Integer> dokterMap = new HashMap<>(); // Map label ke ID

    private final RuanganDao ruanganDao = new RuanganDao();
    private final DokterDao dokterDao = new DokterDao();

    private Ruangan selectedRuangan = null; // Menyimpan ruangan yang sedang diedit

    @FXML
    public void initialize() {
        setupDokterCombo();
        setupStatusCombo();
        setupRuanganTable();
        setupDokterTable();
        loadData();
    }

    private void setupStatusCombo() {
        comboStatus.getItems().addAll("Tersedia", "Istirahat");
    }

    private void setupDokterCombo() {
        List<Dokter> list = dokterDao.getAll();
        for (Dokter d : list) {
            String label = d.getNama() + " - " + d.getNik();
            comboDokter.getItems().add(label);
            dokterMap.put(label, d.getId());
        }
    }

    // Konfigurasi tabel ruangan
    private void setupRuanganTable() {
        // Kolom No urut
        colNo.setCellFactory(col -> new TableCell<>() {
            @Override protected void updateItem(Integer item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? null : String.valueOf(getIndex() + 1));
            }
        });
        colNo.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(null));

        // Kolom properti ruangan
        colNamaRuangan.setCellValueFactory(new PropertyValueFactory<>("namaRuangan"));
        colNamaDokter.setCellValueFactory(new PropertyValueFactory<>("namaDokter"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));

        // Kolom tanggal lahir dokter diambil manual berdasarkan idDokter
        colTglLahir.setCellValueFactory(cellData -> {
            Dokter dokter = dokterDao.getById(cellData.getValue().getIdDokter());
            return new ReadOnlyObjectWrapper<>(dokter != null ? dokter.getTanggalLahir() : "-");
        });

        // Kolom Aksi edit
        colAksi.setCellFactory(col -> new TableCell<>() {
            final Button editBtn = new Button("Edit");

            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    editBtn.setOnAction(e -> {
                        selectedRuangan = getTableView().getItems().get(getIndex());

                        // Pre-select data ruangan ke dalam ComboBox
                        Dokter dokter = dokterDao.getById(selectedRuangan.getIdDokter());
                        String labelDokter = selectedRuangan.getNamaDokter() + " - " + dokter.getNik();
                        comboDokter.getSelectionModel().select(labelDokter);
                        comboStatus.getSelectionModel().select(selectedRuangan.getStatus());
                    });

                    setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
                    setAlignment(Pos.CENTER);
                    editBtn.getStyleClass().add("btn-edit");
                    setGraphic(editBtn);
                }
            }
        });

        // Lebar kolom disesuaikan agar proporsional
        colNo.prefWidthProperty().bind(tableRuangan.widthProperty().multiply(0.05));
        colNamaRuangan.prefWidthProperty().bind(tableRuangan.widthProperty().multiply(0.10));
        colNamaDokter.prefWidthProperty().bind(tableRuangan.widthProperty().multiply(0.45));
        colStatus.prefWidthProperty().bind(tableRuangan.widthProperty().multiply(0.15));
        colTglLahir.prefWidthProperty().bind(tableRuangan.widthProperty().multiply(0.15));
        colAksi.prefWidthProperty().bind(tableRuangan.widthProperty().multiply(0.096));
    }

    // Konfigurasi tabel dokter
    private void setupDokterTable() {
        colNoDokter.setCellFactory(col -> new TableCell<>() {
            @Override protected void updateItem(Integer item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? null : String.valueOf(getIndex() + 1));
            }
        });
        colNoDokter.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(null));

        colNamaDokterTable.setCellValueFactory(new PropertyValueFactory<>("nama"));
        colNikDokter.setCellValueFactory(new PropertyValueFactory<>("nik"));
        colTelpDokter.setCellValueFactory(new PropertyValueFactory<>("noTelepon"));
        colTglLahirDokter.setCellValueFactory(new PropertyValueFactory<>("tanggalLahir"));

        colNoDokter.prefWidthProperty().bind(tableDokter.widthProperty().multiply(0.05));
        colNamaDokterTable.prefWidthProperty().bind(tableDokter.widthProperty().multiply(0.30));
        colNikDokter.prefWidthProperty().bind(tableDokter.widthProperty().multiply(0.25));
        colTelpDokter.prefWidthProperty().bind(tableDokter.widthProperty().multiply(0.20));
        colTglLahirDokter.prefWidthProperty().bind(tableDokter.widthProperty().multiply(0.196));
    }

    // Mengambil dan menampilkan data dari database
    private void loadData() {
        ruanganList.setAll(ruanganDao.getAllWithDokter());
        tableRuangan.setItems(ruanganList);

        dokterList.setAll(dokterDao.getAll());
        tableDokter.setItems(dokterList);
    }

    // Menyimpan perubahan ruangan
    @FXML
    public void updateRuangan() {
        if (selectedRuangan == null) {
            showAlert("Silakan pilih ruangan yang akan diedit terlebih dahulu.");
            return;
        }

        String selectedDokterLabel = comboDokter.getSelectionModel().getSelectedItem();
        String newStatus = comboStatus.getSelectionModel().getSelectedItem();

        if (selectedDokterLabel == null || newStatus == null || newStatus.isBlank()) {
            showAlert("Dokter dan status tidak boleh kosong.");
            return;
        }

        int newIdDokter = dokterMap.get(selectedDokterLabel);

        // Validasi: pastikan dokter tidak bertugas di lebih dari satu ruangan
        for (Ruangan r : ruanganList) {
            if (r.getId() != selectedRuangan.getId() && r.getIdDokter() == newIdDokter) {
                showAlert("Dokter ini sudah ditugaskan di ruangan lain.");
                return;
            }
        }

        // Update data dan simpan
        selectedRuangan.setIdDokter(newIdDokter);
        selectedRuangan.setStatus(newStatus);

        boolean updated = ruanganDao.update(selectedRuangan);
        if (updated) {
            loadData(); // Refresh data
            selectedRuangan = null;
            comboDokter.getSelectionModel().clearSelection();
            comboStatus.getSelectionModel().clearSelection();
            showInfo("Data ruangan berhasil diperbarui.");
        } else {
            showAlert("Gagal memperbarui data ruangan.");
        }
    }

    private void showAlert(String msg) {
        new Alert(Alert.AlertType.ERROR, msg, ButtonType.OK).showAndWait();
    }

    private void showInfo(String msg) {
        new Alert(Alert.AlertType.INFORMATION, msg, ButtonType.OK).showAndWait();
    }
}
