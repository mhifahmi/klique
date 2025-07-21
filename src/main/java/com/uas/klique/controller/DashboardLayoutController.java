package com.uas.klique.controller;

import com.uas.klique.model.Users;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class DashboardLayoutController {

    @FXML private StackPane contentPane;

    @FXML private Label userNameLabel;
    @FXML private Label userRoleLabel;

    @FXML private Button btnDashboard;
    @FXML private Button btnAntrian;
    @FXML private Button btnPasien;
    @FXML private Button btnRuangan;
    @FXML private Button btnRiwayatAntrian;

    public void setUserInfo(Users user) {
        userNameLabel.setText(user.getNama());
        userRoleLabel.setText(user.getRole());
    }

    public void initialize() {
        loadView("/com/uas/klique/dashboard-view.fxml");
        setActiveButton(btnDashboard);
//        loadView("/com/uas/klique/dashboard-antrian-view.fxml");
//        setActiveButton(btnAntrian);
    }

    public void loadDashboard() {
        loadView("/com/uas/klique/dashboard-view.fxml");
        setActiveButton(btnDashboard);
    }

    public void loadAntrian() {
        loadView("/com/uas/klique/dashboard-antrian-view.fxml");
        setActiveButton(btnAntrian);
    }

    public void loadPasien() {
        loadView("/com/uas/klique/dashboard-pasien-view.fxml");
        setActiveButton(btnPasien);
    }

    public void loadRuangan() {
        loadView("/com/uas/klique/dashboard-ruangan-view.fxml");
        setActiveButton(btnRuangan);
    }

    public void loadRiwayatAntrian() {
        loadView("/com/uas/klique/dashboard-riwayat-antrian-view.fxml");
        setActiveButton(btnRiwayatAntrian);
    }

    public void logout() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/uas/klique/main-view.fxml"));
            Scene scene = new Scene(loader.load());

            Stage stage = (Stage) contentPane.getScene().getWindow();
            stage.setMaximized(true);
            stage.setScene(scene);
            stage.setTitle("Login - Klinik Hoyong Damang");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void loadView(String path) {
        try {
            Node view = FXMLLoader.load(getClass().getResource(path));
            contentPane.getChildren().setAll(view);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setActiveButton(Button activeButton) {
        List<Button> navButtons = List.of(btnDashboard, btnAntrian, btnPasien, btnRuangan, btnRiwayatAntrian);
        for (Button btn : navButtons) {
            if (btn.equals(activeButton)) {
                btn.getStyleClass().add("active");
            } else {
                btn.getStyleClass().removeIf(s -> s.equals("active"));
            }
        }
    }

}
