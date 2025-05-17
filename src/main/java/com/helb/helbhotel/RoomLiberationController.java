package com.helb.helbhotel;

import com.helb.helbhotel.config.DiscountCodeGenerator;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import com.helb.helbhotel.config.ConfigStore;

import java.io.IOException;
import java.util.Objects;

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
    public void handleReleaseRoom(ActionEvent event) {
        if (room != null) {
            // Libérer la chambre en mettant son statut à "non occupé"
            ConfigStore.updateRoomStatus(room.getName(), false);

            System.out.println("Chambre " + room.getName() + " libérée");

            handleRate(event);
            String roomType = room.getRoomTypeCode();
            int percent = 0;
            if(roomType.equalsIgnoreCase("B")){
                percent = 50;
            }else if(roomType.equalsIgnoreCase("E")){
                percent = 25;
            }else if(roomType.equalsIgnoreCase("L")){
                percent = 100;
            }
           String code = DiscountCodeGenerator.generateCode(percent);
            showAlert(code);
            System.out.println("code : "+code);

            // Fermer la fenêtre
            closeWindow();
        }
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Ticket");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
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

    public void handleRate(ActionEvent event) {
        try {

            Parent root = FXMLLoader.load(Objects.requireNonNull(MainApplication.class.getResource("note-hotel.fxml")));

            Stage dialogStage = new Stage();
            dialogStage.initModality(Modality.APPLICATION_MODAL);
            dialogStage.initOwner(((Node) event.getSource()).getScene().getWindow());

            dialogStage.setTitle("Give Rate");
            dialogStage.setScene(new Scene(root));

            dialogStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}