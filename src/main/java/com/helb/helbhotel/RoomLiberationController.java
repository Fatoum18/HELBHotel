package com.helb.helbhotel;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import com.helb.helbhotel.config.ConfigStore;

public class RoomLiberationController {

    @FXML
    private Label roomNumberLabel;

    @FXML
    private Button releaseButton;

    private ConfigStore.Room room;

    @FXML
    public void initialize() {
        // Initialisation basique
    }

    /**
     * Configure la chambre à libérer
     */
    public void setRoom(ConfigStore.Room room) {
        this.room = room;

        // Mettre à jour le label pour afficher le numéro de chambre
        if (roomNumberLabel != null) {
            roomNumberLabel.setText("Chambre " + room.getName());
        }
    }

    /**
     * Méthode appelée lorsqu'on clique sur le bouton "Libérer la chambre"
     */
    @FXML
    public void handleReleaseRoom() {
        if (room != null) {
            // Libérer la chambre en mettant son statut à "non occupé"
            ConfigStore.updateRoomStatus(room.getName(), false);

            System.out.println("Chambre " + room.getName() + " libérée");

            // Fermer la fenêtre
            closeWindow();
        }
    }

    /**
     * Ferme la fenêtre
     */
    private void closeWindow() {
        // Utiliser soit roomNumberLabel soit releaseButton pour obtenir la fenêtre
        if (roomNumberLabel != null && roomNumberLabel.getScene() != null) {
            Stage stage = (Stage) roomNumberLabel.getScene().getWindow();
            stage.close();
        } else if (releaseButton != null && releaseButton.getScene() != null) {
            Stage stage = (Stage) releaseButton.getScene().getWindow();
            stage.close();
        }
    }
}