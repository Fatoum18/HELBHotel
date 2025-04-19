package com.helb.helbhotel;

import com.helb.helbhotel.config.ConfigStore;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MainApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("main-interface.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Helb-Hotel");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        ConfigParse.load();
        launch();
    }
}