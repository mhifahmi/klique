package com.uas.klique.controller;

import com.uas.klique.dao.PasienDao;
import com.uas.klique.model.Pasien;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.util.Callback;

import java.util.List;

public class DashboardPasienController {

    // ======== FXML Field Bindings ========
    @FXML private TextField namaField, nikField, alamatField, tglLahirField, jkField, telpField;
    @FXML private TableView<Pasien> tablePasien;
    @FXML private TableColumn<Pasien, Integer> colNo;
    @FXML private TableColumn<Pasien, String> colNama, colNik, colTelp, colTglLahir, colAksi;

    // ======== Data & DAO ========
    private ObservableList<Pasien> pasienList = FXCollections.observableArrayList();
    private final PasienDao pasienDao = new PasienDao();
    private Pasien selectedPasien = null; // Digunakan saat edit

    public void initialize() {
        // Set kolom berdasarkan properti model
        colNo.setCellValueFactory(new PropertyValueFactory<>("id"));
        colNama.setCellValueFactory(new PropertyValueFactory<>("nama"));
        colNik.setCellValueFactory(new PropertyValueFactory<>("nik"));
        colTelp.setCellValueFactory(new PropertyValueFactory<>("noTelepon"));
        colTglLahir.setCellValueFactory(new PropertyValueFactory<>("tanggalLahir"));
        colAksi.setCellFactory(getActionCellFactory());

        // Atur lebar kolom agar proporsional
        colNo.prefWidthProperty().bind(tablePasien.widthProperty().multiply(0.05));
        colNama.prefWidthProperty().bind(tablePasien.widthProperty().multiply(0.28));
        colNik.prefWidthProperty().bind(tablePasien.widthProperty().multiply(0.22));
        colTelp.prefWidthProperty().bind(tablePasien.widthProperty().multiply(0.13));
        colTglLahir.prefWidthProperty().bind(tablePasien.widthProperty().multiply(0.12));
        colAksi.prefWidthProperty().bind(tablePasien.widthProperty().multiply(0.20));

        loadData(); // Ambil data awal dari database
    }

    private void loadData() {
        pasienList.clear();
        pasienList.addAll(pasienDao.getAll());
        tablePasien.setItems(pasienList);
    }

    public void tambahPasien() {
        if (selectedPasien == null) {
            // Tambah baru
            Pasien p = new Pasien();
            p.setNama(namaField.getText());
            p.setNik(nikField.getText());
            p.setAlamat(alamatField.getText());
            p.setTanggalLahir(tglLahirField.getText());
            p.setJenisKelamin(jkField.getText());
            p.setNoTelepon(telpField.getText());

            if (pasienDao.insert(p)) {
                clearForm();
                loadData();
            } else {
                showAlert("Gagal menyimpan data pasien.");
            }
        } else {
            // Edit data lama
            selectedPasien.setNama(namaField.getText());
            selectedPasien.setNik(nikField.getText());
            selectedPasien.setAlamat(alamatField.getText());
            selectedPasien.setTanggalLahir(tglLahirField.getText());
            selectedPasien.setJenisKelamin(jkField.getText());
            selectedPasien.setNoTelepon(telpField.getText());

            if (pasienDao.update(selectedPasien)) {
                clearForm();
                loadData();
                selectedPasien = null; // reset setelah update
            } else {
                showAlert("Gagal mengupdate data pasien.");
            }
        }
    }


    // Membersihkan semua input field
    private void clearForm() {
        namaField.clear();
        nikField.clear();
        alamatField.clear();
        tglLahirField.clear();
        jkField.clear();
        telpField.clear();
        selectedPasien = null;
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Menyusun dialog detail dengan format monospaced
    private void showPasienDetail(Pasien pasien) {
        Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle("Detail Pasien");
        dialog.setHeaderText("Informasi Lengkap Pasien");

        StringBuilder sb = new StringBuilder();
        sb.append(String.format("%-16s: %s%n", "Nama", pasien.getNama()));
        sb.append(String.format("%-16s: %s%n", "NIK", pasien.getNik()));
        sb.append(String.format("%-16s: %s%n", "Alamat", pasien.getAlamat()));
        sb.append(String.format("%-16s: %s%n", "Tanggal Lahir", pasien.getTanggalLahir()));
        sb.append(String.format("%-16s: %s%n", "Jenis Kelamin", pasien.getJenisKelamin()));
        sb.append(String.format("%-16s: %s%n", "No. Telepon", pasien.getNoTelepon()));

        TextArea textArea = new TextArea(sb.toString());
        textArea.setEditable(false);
        textArea.setWrapText(false);
        textArea.setStyle("-fx-font-family: 'monospace'; -fx-font-size: 12px;");
        textArea.setMaxWidth(Double.MAX_VALUE);
        textArea.setMaxHeight(Double.MAX_VALUE);

        dialog.getDialogPane().setContent(textArea);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        dialog.showAndWait();
    }

    private Callback<TableColumn<Pasien, String>, TableCell<Pasien, String>> getActionCellFactory() {
        return param -> new TableCell<>() {
            final Button btnDetail = new Button("Detail");
            final Button btnEdit = new Button("Edit");
            final Button btnHapus = new Button("Hapus");
            final HBox container = new HBox(10, btnDetail, btnEdit, btnHapus);

            {
                // Tampilan tombol dan styling
                container.setAlignment(Pos.CENTER);
                container.setPrefWidth(Double.MAX_VALUE);

                // styling button
                btnDetail.getStyleClass().add("btn-detail");
                btnEdit.getStyleClass().add("btn-edit");
                btnHapus.getStyleClass().add("btn-delete");

                for (Button b : List.of(btnDetail, btnEdit, btnHapus)) {
                    b.setPrefWidth(70); // atau gunakan setMaxWidth agar lebih fleksibel
                }

                // tombol detail
                btnDetail.setOnAction(e -> {
                    Pasien pasien = getTableView().getItems().get(getIndex());
                    showPasienDetail(pasien);
                });

                // tombol edit
                btnEdit.setOnAction(e -> {
                    Pasien pasien = getTableView().getItems().get(getIndex());
                    selectedPasien = pasien;
                    namaField.setText(pasien.getNama());
                    nikField.setText(pasien.getNik());
                    alamatField.setText(pasien.getAlamat());
                    tglLahirField.setText(pasien.getTanggalLahir());
                    jkField.setText(pasien.getJenisKelamin());
                    telpField.setText(pasien.getNoTelepon());
                });

                // tombol hapus
                btnHapus.setOnAction(e -> {
                    Pasien pasien = getTableView().getItems().get(getIndex());
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Konfirmasi Hapus");
                    alert.setHeaderText("Yakin ingin menghapus pasien ini?");
                    alert.setContentText("Data akan dihapus permanen dari sistem.");

                    ButtonType ya = new ButtonType("Ya", ButtonBar.ButtonData.OK_DONE);
                    ButtonType batal = new ButtonType("Batal", ButtonBar.ButtonData.CANCEL_CLOSE);
                    alert.getButtonTypes().setAll(ya, batal);

                    alert.showAndWait().ifPresent(type -> {
                        if (type == ya && pasienDao.delete(pasien.getId())) {
                            loadData();
                            showInfo("Data pasien berhasil dihapus.");
                        }
                    });
                });
            }

            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : container);
            }
        };
    }

    // notifikasi berhasil
    private void showInfo(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Informasi");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
