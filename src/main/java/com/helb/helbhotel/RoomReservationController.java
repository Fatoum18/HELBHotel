package com.helb.helbhotel;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import com.helb.helbhotel.config.RoomAssigner;
import com.helb.helbhotel.model.Reservation;
import com.helb.helbhotel.config.ConfigStore;

import java.util.regex.Pattern;

public class RoomReservationController {

    @FXML
    private TextField propositionField;

    @FXML
    private Button confirmButton;

    private RoomAssigner.PotentialAssign potentialAssign;

    private final Pattern roomPattern = Pattern.compile("^([A-Za-z])(\\d+)([A-Za-z])$");

    @FXML
    public void initialize() {
        // Initialisation basique

        // Ajout d'un gestionnaire pour le bouton de confirmation
        confirmButton.setOnAction(event -> {
            String roomProposition = propositionField.getText();
            if (roomProposition != null && !roomProposition.isEmpty()) {
                confirmReservation(roomProposition);
            }
        });

        propositionField.textProperty().addListener((observableValue, oldValue, newValue) -> {
            boolean isValid = roomPattern.matcher(newValue).matches();

            confirmButton.setDisable(!isValid);
        });

        confirmButton.setDisable(true);
    }

    /**
     * Définit les données de réservation à utiliser dans cette fenêtre
     */
    public void setReservationData(RoomAssigner.PotentialAssign potentialAssign) {
        this.potentialAssign = potentialAssign;

        if (potentialAssign != null) {
            // Pré-remplir le champ de proposition avec la chambre actuellement assignée
            ConfigStore.Room room = potentialAssign.getRoom();
            if (room != null) {
                propositionField.setText(room.getName());
            }
        }
    }

    /**
     * Confirme la réservation avec la chambre proposée
     */
    private void confirmReservation(String roomProposition) {
        if (potentialAssign != null) {
            Reservation reservation = potentialAssign.getReservation();
            System.out.println("Confirmation de réservation pour: " +
                    reservation.getFirstName() + " " +
                    reservation.getLastName() +
                    " dans la chambre: " + roomProposition);
                // A3B

//            Pattern pattern = Pattern.compile("^([A-Za-z]).*")


            // TODO: Implémenter la logique pour confirmer la réservation dans la base de données

            // Fermer la fenêtre
            confirmButton.getScene().getWindow().hide();
        }
    }
}