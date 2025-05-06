package com.helb.helbhotel;
import com.helb.helbhotel.config.ConfigStore;
import com.helb.helbhotel.config.ReservationLoader;
import com.helb.helbhotel.config.RoomAssigner;
import com.helb.helbhotel.model.AssignmentMode;
import com.helb.helbhotel.model.Reservation;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

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
    private ComboBox<String> assignmentComboBox;

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }
    @FXML
    private ListView<RoomAssigner.PotentialAssign> reservationListView;

    private List<RoomAssigner.PotentialAssign> validReservations; // loaded previously


    @FXML
    public void initialize() {

        // Initialize room type buttons
        initializeRoomTypeButtons();

        // Initialize floors
        initializeFloorComboBox();

        initializeAssignmentMode();

        List<Reservation> reservations =  ReservationLoader.loadValidReservations();
        if(reservations!=null){
            validReservations = RoomAssigner.assignRooms(reservations,AssignmentMode.RANDOM_ASSIGNMENT,ConfigStore.getAllRooms());
            initializeReservations();
        }

    }

    private void initializeReservations() {
        reservationListView.setCellFactory(new Callback<>() {
            @Override
            public ListCell<RoomAssigner.PotentialAssign> call(ListView<RoomAssigner.PotentialAssign> listView) {
                return new ListCell<>() {
                    @Override
                    protected void updateItem(RoomAssigner.PotentialAssign reservation, boolean empty) {
                        super.updateItem(reservation, empty);

                        if (empty || reservation == null) {
                            setGraphic(null);
                        } else {
                            try {
                                FXMLLoader loader = new FXMLLoader(getClass().getResource("reservation_item_view.fxml"));
                                HBox hBox = loader.load();
                                ReservationItemViewController controller = loader.getController();
                                controller.setPotentialAssign(reservation, new ReservationDialogListener() {
                                    @Override
                                    public void onReservationClose() {
                                        updateRoomPanel();
                                    }
                                });

                                setGraphic(hBox);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                };
            }
        });

        // Load reservations
        reservationListView.getItems().setAll(validReservations);
    }
    public void initializeAssignmentMode() {
        ObservableList<String> assignmentLabels = FXCollections.observableArrayList();
        for (AssignmentMode mode : AssignmentMode.values()) {
            assignmentLabels.add(mode.getLabel());
        }
        assignmentComboBox.setItems(assignmentLabels);
        assignmentComboBox.getSelectionModel().selectFirst();

        assignmentComboBox.setOnAction(event -> {
            String selectedLabel = assignmentComboBox.getSelectionModel().getSelectedItem();
            AssignmentMode selectedMode = AssignmentMode.fromLabel(selectedLabel);
            System.out.println("Selected mode code: " + selectedMode); // <- You get ENUM here
            List<Reservation> reservations =  ReservationLoader.loadValidReservations();
            if(reservations!=null){
                validReservations = RoomAssigner.assignRooms(reservations,selectedMode,ConfigStore.getAllRooms());
                reservationListView.getItems().setAll(validReservations);
            }
        });
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
        try {
            // Load the FXML file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("room-button.fxml"));
            StackPane roomPane = loader.load();

            // Get references to the components
            Label roomLabel = (Label) loader.getNamespace().get("roomLabel");

            // Set background color based on room type
            roomPane.setStyle(roomPane.getStyle() + String.format("-fx-background-color: %s;", ConfigStore.getRoomColor(room)));

            roomLabel.setText(room.getName());

            // Add click handler to open room-liberation.fxml
            roomPane.setOnMouseClicked(event -> {
                try {
                    // Load the room-liberation.fxml file
                    FXMLLoader liberationLoader = new FXMLLoader(getClass().getResource("room-liberation.fxml"));
                    Parent root = liberationLoader.load();

                    // Create a new stage for the liberation window
                    Stage liberationStage = new Stage();
                    liberationStage.initModality(Modality.APPLICATION_MODAL); // Block input to other windows
                    liberationStage.initOwner(roomPane.getScene().getWindow()); // Set owner window
                    liberationStage.setTitle("Libération de la chambre " + room.getName());

                    // Set the scene
                    Scene scene = new Scene(root);
                    liberationStage.setScene(scene);

                    // Pass the room data to the controller
                    RoomLiberationController controller = liberationLoader.getController();
                    controller.setRoom(room);

                    // Show the window
                    liberationStage.showAndWait();
                    updateRoomPanel();

                } catch (IOException e) {
                    e.printStackTrace();
                    System.err.println("Impossible de charger la vue de libération de chambre: " + e.getMessage());
                }
            });

            return roomPane;
        } catch (IOException e) {
            e.printStackTrace();
            // Fallback to programmatic creation if FXML loading fails
            StackPane fallbackPane = new StackPane();
            fallbackPane.setPrefSize(60, 60);
            fallbackPane.getChildren().add(new Label("Error loading room"));
            return fallbackPane;
        }
    }

    public void handleVerifyCode(ActionEvent event) {
        try {

            Parent root = FXMLLoader.load(Objects.requireNonNull(MainApplication.class.getResource("verification-code.fxml")));

            Stage dialogStage = new Stage();
            dialogStage.initModality(Modality.APPLICATION_MODAL);
            dialogStage.initOwner(((Node) event.getSource()).getScene().getWindow());

            dialogStage.setTitle("Verification code");
            dialogStage.setScene(new Scene(root));

            dialogStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Impossible de charger la vue de vérification de code: " + e.getMessage());
        }
    }

}