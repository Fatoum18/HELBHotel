package com.helb.helbhotel;

import com.helb.helbhotel.config.ConfigStore;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.skin.TextInputControlSkin;
import javafx.scene.layout.*;

public class MainController {

    @FXML
    public ComboBox<ConfigStore.Floor> floorComboBox;
    @FXML
    public HBox roomTypeButtonsContainer;

    @FXML
    public Pane roomPanel;
    @FXML
    private Label welcomeText;

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }
    @FXML
    public void initialize() {

        // Initialize room type buttons
        initializeRoomTypeButtons();

        // Initialize floors
        initializeFloorComboBox();



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



    private void initializeFloorComboBox() {
        // Create observable list of floors
        ObservableList<ConfigStore.Floor> floors = FXCollections.observableArrayList(ConfigStore.getFloors());

        // Set items directly using Floor objects
        floorComboBox.setItems(floors);

        // Set custom cell factory to display full name
        floorComboBox.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(ConfigStore.Floor floor, boolean empty) {
                super.updateItem(floor, empty);
                if (empty || floor == null) {
                    setText(null);
                } else {
                    setText(floor.getFullName()); // Display full name
                }
            }
        });

        // Set custom button cell to show full name when not expanded
        floorComboBox.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(ConfigStore.Floor floor, boolean empty) {
                super.updateItem(floor, empty);
                if (empty || floor == null) {
                    setText(null);
                } else {
                    setText(floor.getFullName()); // Display full name
                }
            }
        });

        // Add listener that triggers room panel update
        floorComboBox.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldVal, newVal) -> updateRoomPanel()
        );

        // Select first floor by default
        if (!floors.isEmpty()) {
            floorComboBox.getSelectionModel().selectFirst();
        }
    }



    private void updateRoomPanel() {
        roomPanel.getChildren().clear();
        ConfigStore.Floor selectedFloor = floorComboBox.getValue();


        if (selectedFloor != null) {
            // Find the selected floor
            ConfigStore.Floor floor = ConfigStore.getFloors().stream()
                    .filter(f -> f.getName().equals(selectedFloor.getName()))
                    .findFirst()
                    .orElse(null);
            if (floor != null) {
                VBox rowsContainer = new VBox(20); // 20px spacing between rows
                rowsContainer.setLayoutX(30);
                rowsContainer.setLayoutY(20);

                HBox currentRow = new HBox(30); // 30px spacing between rooms
                int roomsInCurrentRow = 0;

                for (ConfigStore.Room room : floor.getRooms()) {
                    // Create room button
                    Pane roomButton = createRoomButton(room);

                    // Add to current row
                    currentRow.getChildren().add(roomButton);
                    roomsInCurrentRow++;

                    // If row is full (3 rooms), add to container and start new row
                    if (roomsInCurrentRow >= 4) {
                        rowsContainer.getChildren().add(currentRow);
                        currentRow = new HBox(30);
                        roomsInCurrentRow = 0;
                    }
                }

                // Add the last row if it has any rooms
                if (!currentRow.getChildren().isEmpty()) {
                    rowsContainer.getChildren().add(currentRow);
                }

                roomPanel.getChildren().add(rowsContainer);
            }
        }
    }

    private Pane createRoomButton(ConfigStore.Room room) {
        // Create the room display
        StackPane roomPane = new StackPane();
        roomPane.setPrefSize(60, 60); // Fixed size for rooms

        // Set background color based on room type
        ConfigStore.getRoomTypes().stream()
                .filter(rt -> rt.getCode().equals(room.getRoomTypeCode()))
                .findFirst()
                .ifPresent(roomType -> {
                    roomPane.setStyle(String.format(
                            "-fx-background-color: %s; " +
                                    "-fx-border-color: black; " +
                                    "-fx-border-radius: 10; " +
                                    "-fx-background-radius: 10;",
                            roomType.getColor()
                    ));
                });

        String roomType = room.getRoomTypeCode();
        String roomNumber ="Z";
        if(!roomType.equals("Z")){
          roomNumber =  room.getFloorPrefix() + room.getRoomNumber()+ roomType;
        }
        // Add room number text
        Label roomLabel = new Label(roomNumber);
        roomLabel.setStyle("-fx-font-weight: bold;");
        roomPane.getChildren().add(roomLabel);

        // Add click handler
        roomPane.setOnMouseClicked(event -> {
            // Handle room selection here
            System.out.println("Selected room: " + room);
        });

        return roomPane;
    }
}