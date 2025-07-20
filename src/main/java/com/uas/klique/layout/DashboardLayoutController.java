package com.uas.klique.layout;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.StackPane;
import javafx.scene.Node;

public class DashboardLayoutController {

    @FXML private StackPane contentPane;

    public void initialize() {
        loadView("/com/uas/klique/dashboard/dashboard-view.fxml");
    }

    public void loadDashboard() {
        loadView("/com/uas/klique/dashboard/dashboard-view.fxml");
    }

    public void loadAntrian() {
        loadView("/com/uas/klique/antrian/AntrianView.fxml");
    }

    public void loadPasien() {
        loadView("/com/uas/klique/pasien/PasienView.fxml");
    }

    public void loadRuangan() {
        loadView("/com/uas/klique/ruangan/RuanganView.fxml");
    }

    public void logout() {
        System.out.println("Log out clicked");
        // TODO: kembali ke login screen
    }

    private void loadView(String path) {
        try {
            Node view = FXMLLoader.load(getClass().getResource(path));
            contentPane.getChildren().setAll(view);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
