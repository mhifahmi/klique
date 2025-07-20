package com.uas.klique;

import com.uas.klique.util.DbUtil;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainApplication extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        try {
            Class.forName("org.sqlite.JDBC");
            System.out.println("✅ SQLite JDBC driver loaded");
        } catch (ClassNotFoundException e) {
            System.out.println("❌ Driver not found!");
            e.printStackTrace();
        }

        DbUtil.initializeDatabase();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/uas/klique/main-view.fxml"));
        Scene scene = new Scene(loader.load(), 1280, 720);
        stage.setTitle("Klique - Sistem Antrian Klinik");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
