package com.uas.klique;

import com.uas.klique.db.Seeder;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.IOException;

public class KliqueApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
//        Seeder.seed();
        // Register font
        Font.loadFont(getClass().getResource("/com/uas/klique/fonts/Poppins-Regular.ttf").toExternalForm(), 12);
        FXMLLoader fxmlLoader = new FXMLLoader(KliqueApplication.class.getResource("main-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1440, 768);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}