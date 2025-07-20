module com.uas.klique {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires java.sql;

    opens com.uas.klique to javafx.fxml;
    opens com.uas.klique.controller to javafx.fxml;
    opens com.uas.klique.model to javafx.base;
    exports com.uas.klique;
}