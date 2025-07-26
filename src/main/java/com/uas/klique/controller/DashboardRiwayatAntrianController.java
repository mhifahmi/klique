package com.uas.klique.controller;

import com.uas.klique.dao.AntrianDao;
import com.uas.klique.model.Antrian;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.time.LocalDate;
import java.util.List;

public class DashboardRiwayatAntrianController {

    // Komponen UI dari FXML
    @FXML private DatePicker startDatePicker;
    @FXML private DatePicker endDatePicker;
    @FXML private TextField fieldCariNama;
    @FXML private Button btnCari;
    @FXML private Button btnReset;
    @FXML private TableView<Antrian> tableRiwayat;
    @FXML private TableColumn<Antrian, Void> colNo;
    @FXML private TableColumn<Antrian, String> colNamaPasien;
    @FXML private TableColumn<Antrian, String> colNik;
    @FXML private TableColumn<Antrian, String> colTanggal;
    @FXML private TableColumn<Antrian, String> colDokter;
    @FXML private TableColumn<Antrian, String> colStatus;

    // DAO untuk akses database
    private final AntrianDao antrianDao = new AntrianDao();
    // List untuk menampung data antrian yang ditampilkan di tabel
    private final ObservableList<Antrian> riwayatList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        // Inisialisasi kolom No dengan index baris
        colNo.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setText(null);
                } else {
                    setText(String.valueOf(getIndex() + 1)); // Tampilkan index mulai dari 1
                }
            }
        });
        colNo.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(null)); // Placeholder, karena sudah diisi di cellFactory

        // Bind properti dari model ke kolom tabel
        colNamaPasien.setCellValueFactory(new PropertyValueFactory<>("namaPasien"));
        colNik.setCellValueFactory(new PropertyValueFactory<>("pasienNik"));
        colTanggal.setCellValueFactory(new PropertyValueFactory<>("tanggal"));
        colDokter.setCellValueFactory(cell -> {
            String nama = cell.getValue().getNamaDokter();
            return new ReadOnlyObjectWrapper<>(nama != null ? nama : "-"); // Tampilkan "-" jika dokter null
        });
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));

        // Atur validasi tanggal di startDatePicker agar tidak bisa memilih tanggal di masa depan
        startDatePicker.setDayCellFactory(picker -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                setDisable(empty || date.isAfter(LocalDate.now()));
            }
        });

        // Nonaktifkan endDatePicker awalnya
        endDatePicker.setDisable(true);
        // Validasi tanggal akhir agar tidak sebelum tanggal mulai
        endDatePicker.setDayCellFactory(picker -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                LocalDate start = startDatePicker.getValue();
                setDisable(empty || start == null || date.isBefore(start));
            }
        });

        // Jika tanggal mulai berubah, reset dan atur validasi untuk tanggal akhir
        startDatePicker.valueProperty().addListener((obs, oldVal, newVal) -> {
            endDatePicker.setValue(null);
            endDatePicker.setDisable(newVal == null);
            endDatePicker.setDayCellFactory(picker -> new DateCell() {
                @Override
                public void updateItem(LocalDate date, boolean empty) {
                    super.updateItem(date, empty);
                    setDisable(empty || newVal == null || date.isBefore(newVal));
                }
            });
        });

        // Listener untuk field pencarian nama pasien
        fieldCariNama.textProperty().addListener((obs, oldVal, newVal) -> cariRiwayat());

        // Set data awal tabel
        tableRiwayat.setItems(riwayatList);
        tampilkanSemuaRiwayat();
    }

    @FXML
    public void cariRiwayat() {
        // Ambil nilai filter tanggal dan nama
        LocalDate start = startDatePicker.getValue();
        LocalDate end = endDatePicker.getValue();
        String filterNama = fieldCariNama.getText() != null ? fieldCariNama.getText().toLowerCase() : "";

        List<Antrian> data;

        // Filter berdasarkan tanggal range jika keduanya dipilih dan valid
        if (start != null && end != null && !start.isAfter(end)) {
            data = antrianDao.getRiwayatByTanggalRange(start.toString(), end.toString());
        }
        // Filter hanya berdasarkan tanggal mulai
        else if (start != null) {
            data = antrianDao.getRiwayatByTanggal(start.toString());
        }
        // Jika tidak ada filter tanggal, ambil semua data
        else {
            data = antrianDao.getSemuaRiwayat();
        }

        // Filter berdasarkan nama jika diisi
        if (!filterNama.isBlank()) {
            data = data.stream()
                    .filter(a -> a.getNamaPasien().toLowerCase().contains(filterNama))
                    .toList();
        }

        // Tampilkan hasil filter di tabel
        riwayatList.setAll(data);
    }

    @FXML
    public void resetRiwayat() {
        // Reset semua filter
        startDatePicker.setValue(null);
        endDatePicker.setValue(null);
        endDatePicker.setDisable(true);
        fieldCariNama.clear();
        tampilkanSemuaRiwayat();
    }

    // Tampilkan seluruh data riwayat tanpa filter
    private void tampilkanSemuaRiwayat() {
        List<Antrian> data = antrianDao.getSemuaRiwayat();
        riwayatList.setAll(data);
    }
}
