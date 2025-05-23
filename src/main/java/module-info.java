module com.helb.helbhotel {
    requires static lombok;
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;


    opens com.helb.helbhotel to javafx.fxml;
    exports com.helb.helbhotel;
    exports com.helb.helbhotel.config;
    opens com.helb.helbhotel.config to javafx.fxml;
}