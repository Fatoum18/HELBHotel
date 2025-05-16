module test.com.helb.helbhotel {

    requires static org.junit.jupiter.api;
    requires javafx.controls;
    requires javafx.fxml;
    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires com.helb.helbhotel;


    opens  test.com.helb.helbhotel to org.junit.platform.commons;

    exports test.com.helb.helbhotel;

}