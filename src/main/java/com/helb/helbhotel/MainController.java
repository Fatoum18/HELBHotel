package com.helb.helbhotel;

import com.helb.helbhotel.config.ConfigStore;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MainController {

    @FXML
    public ComboBox<String> floorComboBox;
    @FXML
    public HBox roomTypeButtonsContainer;
    @FXML
    private Label welcomeText;

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }
    @FXML
    public void initialize() {
        // Initialize room type buttons
        // Initialize floors
        updateFloorSelection();
        initializeRoomTypeButtons();

    }


    private void initializeRoomTypeButtons() {
        roomTypeButtonsContainer.getChildren().clear();
        roomTypeButtonsContainer.setSpacing(0); // No spacing between elements

        for (ConfigStore.RoomType roomType : ConfigStore.getRoomTypes()) {
            // Create the container HBox with room type color (no border)
            HBox typeContainer = new HBox();
            typeContainer.setStyle(String.format(
                    "-fx-background-color: %s; -fx-background-radius: 0;",
                    roomType.getColor()
            ));

            // Calculate width based on name length
            double containerWidth = 60 + (roomType.getName().length() * 10);
            typeContainer.setPrefWidth(containerWidth);
            typeContainer.setPrefHeight(roomTypeButtonsContainer.getPrefHeight()); // Match parent height

            // Create the label (takes 3/4 width, full height)
            Label typeLabel = new Label(roomType.getName());
            typeLabel.setMaxHeight(Double.MAX_VALUE); // Expand to full height
            typeLabel.setPrefWidth(containerWidth * 0.75);
            typeLabel.setStyle(
                    "-fx-background-color: white; " +
                            "-fx-border-color: black; " +
                            "-fx-border-radius: 0; " +
                            "-fx-background-radius: 0; " +
                            "-fx-padding: 0 5 0 5;" // Left/right padding only
            );
            typeLabel.setAlignment(Pos.CENTER);

            // Make label fill available vertical space
            VBox.setVgrow(typeLabel, Priority.ALWAYS);

            // Add label to container
            typeContainer.getChildren().add(typeLabel);


            roomTypeButtonsContainer.getChildren().add(typeContainer);
        }
    }

    private void updateFloorSelection() {
        List<ConfigStore.Floor> floors = ConfigStore.getFloors();
        if (!floors.isEmpty()) {
            List<String> floorStrings = floors.stream().map(ConfigStore.Floor::getFullName).collect(Collectors.toList());
            floorComboBox.setItems(FXCollections.observableArrayList(floorStrings));
            floorComboBox.getSelectionModel().selectFirst();

        }
    }
}