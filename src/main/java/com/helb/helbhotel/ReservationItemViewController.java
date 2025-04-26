package com.helb.helbhotel;

import com.helb.helbhotel.model.Reservation;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class ReservationItemViewController {

    @FXML
    private Label reservationLabel;

    @FXML
    private Button refreshButton;

    public void setReservation(Reservation reservation) {
        // Example format: "A. Anderson\nA1L"
        String labelText = reservation.getFirstName().charAt(0) + ". " + reservation.getLastName();
        reservationLabel.setText(labelText);

        // You can also set action for the refreshButton if needed
        refreshButton.setOnAction(e -> {
            System.out.println("Refresh clicked for: " + reservation.getFirstName() + " " + reservation.getLastName());
        });
    }
}
