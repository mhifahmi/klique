module com.uas.klique {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    // Tambahan eksternal UI libraries
    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;

    // Open and export packages
    opens com.uas.klique to javafx.fxml;
    opens com.uas.klique.util to javafx.fxml;
    opens com.uas.klique.dashboard to javafx.fxml;

    exports com.uas.klique;
    exports com.uas.klique.dashboard;
}
