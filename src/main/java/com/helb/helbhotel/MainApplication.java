package com.helb.helbhotel;

import com.helb.helbhotel.config.ConfigParse;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
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

        try {

            ConfigParse.load();

            launch(args);
        } catch (IllegalArgumentException e) {

            System.err.println("Erreur de configuration: " + e.getMessage());


            Platform.runLater(() -> {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erreur de configuration");
                alert.setHeaderText("Impossible de démarrer l'application");
                alert.setContentText("Le fichier de configuration .hconfig contient des erreurs:\n\n" + e.getMessage());

                alert.showAndWait();
                Platform.exit();
            });
        } catch (IOException e) {
            // Erreur de fichier
            System.err.println("Erreur de lecture du fichier: " + e.getMessage());

            // Lancer une interface minimale pour afficher un message d'erreur
            // puis se terminer
            Platform.runLater(() -> {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erreur de fichier");
                alert.setHeaderText("Impossible de démarrer l'application");
                alert.setContentText("Erreur lors de la lecture du fichier .hconfig:\n\n" + e.getMessage());

                alert.showAndWait();
                Platform.exit();
            });
        }
    }
}