package com.helb.helbhotel;

import com.helb.helbhotel.config.ConfigStore;
import com.helb.helbhotel.config.RoomAssigner;
import com.helb.helbhotel.model.Reservation;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class ReservationItemViewController {

    public VBox reservationItem;
    @FXML
    private Label guestLabel;

    @FXML
    private Label roomLabel;

    @FXML
    private Button refreshButton;

    public void setPotentialAssign(RoomAssigner.PotentialAssign potentialAssign) {

        Reservation reservation = potentialAssign.getReservation();
        ConfigStore.Room  room = potentialAssign.getRoom();

        reservationItem.setStyle(reservationItem.getStyle() + String.format("-fx-background-color: %s;", ConfigStore.getRoomColor(room.getRoomTypeCode())));
        // Example format: "A. Anderson\nA1L"
        String labelText = reservation.getFirstName().charAt(0) + ". " + reservation.getLastName();
        guestLabel.setText(labelText);

        roomLabel.setText(room.getName());

        // You can also set action for the refreshButton if needed
        refreshButton.setOnAction(e -> {
            System.out.println("Refresh clicked for: " + reservation.getFirstName() + " " + reservation.getLastName());
        });
    }
}
