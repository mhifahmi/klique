package com.uas.klique.controller;

import com.uas.klique.dao.PasienDao;
import com.uas.klique.model.Pasien;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.util.Callback;

public class DashboardPasienController {

    @FXML private TextField namaField, nikField, alamatField, tglLahirField, jkField, telpField;
    @FXML private TableView<Pasien> tablePasien;
    @FXML private TableColumn<Pasien, Integer> colNo;
    @FXML private TableColumn<Pasien, String> colNama, colNik, colTelp, colTglLahir, colAksi;

    private ObservableList<Pasien> pasienList = FXCollections.observableArrayList();
    private final PasienDao pasienDao = new PasienDao();

    private Pasien selectedPasien = null;

    public void initialize() {
        colNo.setCellValueFactory(new PropertyValueFactory<>("id"));
        colNama.setCellValueFactory(new PropertyValueFactory<>("nama"));
        colNik.setCellValueFactory(new PropertyValueFactory<>("nik"));
        colTelp.setCellValueFactory(new PropertyValueFactory<>("noTelepon"));
        colTglLahir.setCellValueFactory(new PropertyValueFactory<>("tanggalLahir"));
        colAksi.setCellFactory(getActionCellFactory());

        // Bind column widths
        colNo.prefWidthProperty().bind(tablePasien.widthProperty().multiply(0.05));
        colNama.prefWidthProperty().bind(tablePasien.widthProperty().multiply(0.28));
        colNik.prefWidthProperty().bind(tablePasien.widthProperty().multiply(0.22));
        colTelp.prefWidthProperty().bind(tablePasien.widthProperty().multiply(0.13));
        colTglLahir.prefWidthProperty().bind(tablePasien.widthProperty().multiply(0.12));
        colAksi.prefWidthProperty().bind(tablePasien.widthProperty().multiply(0.20));


        loadData();
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

    private void showPasienDetail(Pasien pasien) {
        Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle("Detail Pasien");
        dialog.setHeaderText("Informasi Lengkap Pasien");

        // Konten
        String content =
                "Nama: " + pasien.getNama() + "\n" +
                        "NIK: " + pasien.getNik() + "\n" +
                        "Alamat: " + pasien.getAlamat() + "\n" +
                        "Tanggal Lahir: " + pasien.getTanggalLahir() + "\n" +
                        "Jenis Kelamin: " + pasien.getJenisKelamin() + "\n" +
                        "No. Telepon: " + pasien.getNoTelepon();

        dialog.getDialogPane().setContent(new Label(content));
        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        dialog.showAndWait();
    }

    // Lalu ubah bagian getActionCellFactory() untuk memanggil dialog tersebut
    private Callback<TableColumn<Pasien, String>, TableCell<Pasien, String>> getActionCellFactory() {
        return param -> new TableCell<>() {
            final Button btnDetail = new Button("Detail");
            final Button btnEdit = new Button("Edit");
            final Button btnHapus = new Button("Hapus");
            final HBox pane = new HBox(5, btnDetail, btnEdit, btnHapus);

            {
                btnDetail.setOnAction(e -> {
                    Pasien pasien = getTableView().getItems().get(getIndex());
                    showPasienDetail(pasien);
                });

                // opsional untuk btnEdit dan btnHapus nanti
            }

            {
                btnEdit.setOnAction(e -> {
                    Pasien pasien = getTableView().getItems().get(getIndex());
                    selectedPasien = pasien; // simpan yang sedang diedit

                    // Isi field form dengan data pasien yang akan diedit
                    namaField.setText(pasien.getNama());
                    nikField.setText(pasien.getNik());
                    alamatField.setText(pasien.getAlamat());
                    tglLahirField.setText(pasien.getTanggalLahir());
                    jkField.setText(pasien.getJenisKelamin());
                    telpField.setText(pasien.getNoTelepon());
                });
            }

            {
                btnHapus.setOnAction(e -> {
                    Pasien pasien = getTableView().getItems().get(getIndex());

                    // Konfirmasi sebelum hapus
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Konfirmasi Hapus");
                    alert.setHeaderText("Yakin ingin menghapus pasien ini?");
                    alert.setContentText("Data akan dihapus permanen dari sistem.");

                    ButtonType yesButton = new ButtonType("Ya", ButtonBar.ButtonData.OK_DONE);
                    ButtonType cancelButton = new ButtonType("Batal", ButtonBar.ButtonData.CANCEL_CLOSE);
                    alert.getButtonTypes().setAll(yesButton, cancelButton);

                    alert.showAndWait().ifPresent(type -> {
                        if (type == yesButton) {
                            if (pasienDao.delete(pasien.getId())) {
                                loadData();
                                showInfo("Data pasien berhasil dihapus.");
                            } else {
                                showAlert("Gagal menghapus data pasien.");
                            }
                        }
                    });
                });
            }

            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(pane);
                }
            }
        };
    }

    private void showInfo(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Informasi");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
