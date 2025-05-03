package com.helb.helbhotel;

import com.helb.helbhotel.config.ConfigStore;
import com.helb.helbhotel.config.RoomAssigner;
import com.helb.helbhotel.model.Reservation;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class ReservationItemViewController {

    public VBox reservationItem;
    @FXML
    private Label guestLabel;

    @FXML
    private Label roomLabel;

    @FXML
    private Button refreshButton;

    private RoomAssigner.PotentialAssign potentialAssign;

    public void setPotentialAssign(RoomAssigner.PotentialAssign potentialAssign) {
        this.potentialAssign = potentialAssign;

        Reservation reservation = potentialAssign.getReservation();
        ConfigStore.Room room = potentialAssign.getRoom();

        reservationItem.setStyle(reservationItem.getStyle() + String.format("-fx-background-color: %s;", ConfigStore.getRoomColor(room.getRoomTypeCode())));
        // Example format: "A. Anderson\nA1L"
        String labelText = reservation.getFirstName().charAt(0) + ". " + reservation.getLastName();
        guestLabel.setText(labelText);

        roomLabel.setText(room.getName());

        // You can also set action for the refreshButton if needed
        refreshButton.setOnAction(e -> {
            System.out.println("Refresh clicked for: " + reservation.getFirstName() + " " + reservation.getLastName());
        });

        // Ajouter un gestionnaire d'événements pour le clic sur le VBox
        reservationItem.setOnMouseClicked(this::handleReservationItemClick);
    }

    private void handleReservationItemClick(MouseEvent event) {
        try {
            // Charger la vue room-reservation.fxml avec le nouveau contrôleur
            FXMLLoader loader = new FXMLLoader(getClass().getResource("room-reservation.fxml"));
            Parent root = loader.load();

            // Récupérer le contrôleur spécifique à la réservation et passer les informations
            RoomReservationController controller = loader.getController();
            controller.setReservationData(potentialAssign);

            // Créer une nouvelle fenêtre modale
            Stage reservationStage = new Stage();
            reservationStage.initModality(Modality.APPLICATION_MODAL);
            reservationStage.initOwner(reservationItem.getScene().getWindow());

            // Définir le titre avec le nom du client
            Reservation reservation = potentialAssign.getReservation();
            reservationStage.setTitle("Réservation pour " +
                    reservation.getFirstName() + " " +
                    reservation.getLastName());

            // Configurer et afficher la scène
            Scene scene = new Scene(root);
            reservationStage.setScene(scene);
            reservationStage.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Impossible de charger la vue de réservation de chambre: " + e.getMessage());
        }
    }
}