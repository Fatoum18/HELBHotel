package com.helb.helbhotel;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import com.helb.helbhotel.config.RoomAssigner;
import com.helb.helbhotel.model.Reservation;
import com.helb.helbhotel.config.ConfigStore;
import javafx.stage.Stage;

import java.util.regex.Pattern;

public class RoomReservationController {

    @FXML
    private TextField propositionField;

    @FXML
    private Button confirmButton;

    private RoomAssigner.PotentialAssign potentialAssign;

    private final Pattern roomPattern = Pattern.compile("^([A-Za-z])(\\d+)([A-Za-z])$");

    private RoomAssigner.PotentialAssign resultData = null;
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
            if(isValid){
              ConfigStore.Room room =  ConfigStore.findRoomByCode(newValue);
                // si la chambre est trouver et elle n'est pas occupper
                confirmButton.setDisable(room == null || room.isBusy());
            }else{
                confirmButton.setDisable(true);
            }

        });

        confirmButton.setDisable(true);
    }

    public RoomAssigner.PotentialAssign getResultData(){
        return  resultData;
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

            // Confirmation de la reservation de la chambre
            ConfigStore.updateRoomStatus(roomProposition,true);
            resultData = potentialAssign;

            // Fermer la fenêtre
            ((Stage)confirmButton.getScene().getWindow()).close();
        }
    }
}