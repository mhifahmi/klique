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

    @FXML private DatePicker startDatePicker;
    @FXML private DatePicker endDatePicker;
    @FXML private Button btnCari;
    @FXML private Button btnReset;
    @FXML private TableView<Antrian> tableRiwayat;
    @FXML private TableColumn<Antrian, Void> colNo;
    @FXML private TableColumn<Antrian, String> colNamaPasien;
    @FXML private TableColumn<Antrian, String> colNik;
    @FXML private TableColumn<Antrian, String> colTanggal;
    @FXML private TableColumn<Antrian, String> colDokter;
    @FXML private TableColumn<Antrian, String> colStatus;

    private final AntrianDao antrianDao = new AntrianDao();
    private final ObservableList<Antrian> riwayatList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        colNo.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setText(null);
                } else {
                    setText(String.valueOf(getIndex() + 1));
                }
            }
        });
        colNo.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(null));

        colNamaPasien.setCellValueFactory(new PropertyValueFactory<>("namaPasien"));
        colNik.setCellValueFactory(new PropertyValueFactory<>("nik"));
        colTanggal.setCellValueFactory(new PropertyValueFactory<>("tanggal"));
        colDokter.setCellValueFactory(cell -> {
            String nama = cell.getValue().getNamaDokter();
            return new ReadOnlyObjectWrapper<>(nama != null ? nama : "-");
        });
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));

        // Set date pickers constraints
        startDatePicker.setDayCellFactory(picker -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                setDisable(empty || date.isAfter(LocalDate.now()));
            }
        });

        endDatePicker.setDisable(true); // disable by default
        endDatePicker.setDayCellFactory(picker -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                LocalDate start = startDatePicker.getValue();
                setDisable(empty || start == null || date.isBefore(start));
            }
        });

        // Refresh endDatePicker when startDate changes
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

        tableRiwayat.setItems(riwayatList);
        tampilkanSemuaRiwayat();
    }

    @FXML
    public void cariRiwayat() {
        LocalDate start = startDatePicker.getValue();
        LocalDate end = endDatePicker.getValue();

        if (start != null && end != null && !start.isAfter(end)) {
            List<Antrian> data = antrianDao.getRiwayatByTanggalRange(start.toString(), end.toString());
            riwayatList.setAll(data);
        } else if (start != null && end == null) {
            List<Antrian> data = antrianDao.getRiwayatByTanggal(start.toString());
            riwayatList.setAll(data);
        } else {
            tampilkanSemuaRiwayat();
        }
    }

    @FXML
    public void resetRiwayat() {
        startDatePicker.setValue(null);
        endDatePicker.setValue(null);
        endDatePicker.setDisable(true);
        tampilkanSemuaRiwayat();
    }

    private void tampilkanSemuaRiwayat() {
        List<Antrian> data = antrianDao.getSemuaRiwayat();
        riwayatList.setAll(data);
    }
}
