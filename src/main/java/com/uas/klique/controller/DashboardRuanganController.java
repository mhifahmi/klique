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

    @FXML private TableColumn<Ruangan, String> colNamaRuangan, colNamaDokter, colStatus, colTglLahir, colAksi;
    @FXML private TableColumn<Ruangan, Integer> colNo;

    @FXML private TableView<Ruangan> tableRuangan;

    @FXML private TableColumn<Dokter, Integer> colNoDokter;
    @FXML private TableColumn<Dokter, String> colNamaDokterTable, colNikDokter, colTelpDokter, colTglLahirDokter;
    @FXML private TableView<Dokter> tableDokter;

    @FXML private ComboBox<String> comboStatus;
    @FXML private ComboBox<String> comboDokter;

    private final ObservableList<Ruangan> ruanganList = FXCollections.observableArrayList();
    private final ObservableList<Dokter> dokterList = FXCollections.observableArrayList();

    private final RuanganDao ruanganDao = new RuanganDao();
    private final DokterDao dokterDao = new DokterDao();

    private final Map<String, Integer> dokterMap = new HashMap<>();
    private Ruangan selectedRuangan = null;

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

    private void setupRuanganTable() {
        colNo.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(Integer item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? null : String.valueOf(getIndex() + 1));
            }
        });
        colNo.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(null));

        colNamaRuangan.setCellValueFactory(new PropertyValueFactory<>("namaRuangan"));
        colNamaDokter.setCellValueFactory(new PropertyValueFactory<>("namaDokter"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        colTglLahir.setCellValueFactory(cellData -> {
            Dokter dokter = dokterDao.getById(cellData.getValue().getIdDokter());
            return new ReadOnlyObjectWrapper<>(dokter != null ? dokter.getTanggalLahir() : "-");
        });

        colAksi.setCellFactory(col -> new TableCell<>() {
            final Button editBtn = new Button("Edit");

            @Override
            protected void updateItem(String item, boolean empty) {
                editBtn.setMaxWidth(Double.MAX_VALUE); // full-width
                editBtn.setStyle("-fx-alignment: center;"); // opsional gaya tambahan
                setContentDisplay(ContentDisplay.GRAPHIC_ONLY); // agar button full cell
                setAlignment(Pos.CENTER); // tombol tetap di tengah cell
                editBtn.getStyleClass().add("btn-edit");

                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    editBtn.setOnAction(e -> {
                        selectedRuangan = getTableView().getItems().get(getIndex());
                        String labelDokter = selectedRuangan.getNamaDokter() + " - " + dokterDao.getById(selectedRuangan.getIdDokter()).getNik();
                        comboDokter.getSelectionModel().select(labelDokter);
                        comboStatus.getSelectionModel().select(selectedRuangan.getStatus());
                    });
                    setGraphic(editBtn);
                }
            }
        });

        // Bind column widths
        colNo.prefWidthProperty().bind(tableRuangan.widthProperty().multiply(0.05));
        colNamaRuangan.prefWidthProperty().bind(tableRuangan.widthProperty().multiply(0.10));
        colNamaDokter.prefWidthProperty().bind(tableRuangan.widthProperty().multiply(0.45));
        colStatus.prefWidthProperty().bind(tableRuangan.widthProperty().multiply(0.15));
        colTglLahir.prefWidthProperty().bind(tableRuangan.widthProperty().multiply(0.15));
        colAksi.prefWidthProperty().bind(tableRuangan.widthProperty().multiply(0.096));
    }

    private void setupDokterTable() {
        colNoDokter.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(Integer item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? null : String.valueOf(getIndex() + 1));
            }
        });
        colNoDokter.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(null));

        colNamaDokterTable.setCellValueFactory(new PropertyValueFactory<>("nama"));
        colNikDokter.setCellValueFactory(new PropertyValueFactory<>("nik"));
        colTelpDokter.setCellValueFactory(new PropertyValueFactory<>("noTelepon"));
        colTglLahirDokter.setCellValueFactory(new PropertyValueFactory<>("tanggalLahir"));

        // Bind column widths
        colNoDokter.prefWidthProperty().bind(tableDokter.widthProperty().multiply(0.05));
        colNamaDokterTable.prefWidthProperty().bind(tableDokter.widthProperty().multiply(0.30));
        colNikDokter.prefWidthProperty().bind(tableDokter.widthProperty().multiply(0.25));
        colTelpDokter.prefWidthProperty().bind(tableDokter.widthProperty().multiply(0.20));
        colTglLahirDokter.prefWidthProperty().bind(tableDokter.widthProperty().multiply(0.196));
    }

    private void loadData() {
        ruanganList.setAll(ruanganDao.getAllWithDokter());
        tableRuangan.setItems(ruanganList);

        dokterList.setAll(dokterDao.getAll());
        tableDokter.setItems(dokterList);
    }

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

        // Validasi apakah dokter sudah digunakan di ruangan lain
        for (Ruangan r : ruanganList) {
            if (r.getId() != selectedRuangan.getId() && r.getIdDokter() == newIdDokter) {
                showAlert("Dokter ini sudah ditugaskan di ruangan lain.");
                return;
            }
        }

        selectedRuangan.setIdDokter(newIdDokter);
        selectedRuangan.setStatus(newStatus);

        boolean updated = ruanganDao.update(selectedRuangan);
        if (updated) {
            loadData();
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
