package com.helb.helbhotel;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.Button;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

public class NoteHotelController {

    @FXML private Label star1;
    @FXML private Label star2;
    @FXML private Label star3;
    @FXML private Label star4;
    @FXML private Label star5;
    @FXML private Label ratingText;
    @FXML private TextArea commentArea;
    @FXML private Button saveButton;
    @FXML private Button cancelButton;

    private int rating = 0;
    private boolean isRatingSelected = false;

    @FXML
    public void initialize() {
        // Initialize any required setup
    }

    @FXML
    private void handleStarHover(javafx.scene.input.MouseEvent event) {
        if (!isRatingSelected) {
            Label hoveredStar = (Label) event.getSource();
            resetStars();

            if (hoveredStar == star1) {
                star1.setText("★");
                star1.setStyle("-fx-text-fill: #ffcc00; -fx-font-size: 40px;");
            } else if (hoveredStar == star2) {
                fillStars(2);
            } else if (hoveredStar == star3) {
                fillStars(3);
            } else if (hoveredStar == star4) {
                fillStars(4);
            } else if (hoveredStar == star5) {
                fillStars(5);
            }
        }
    }

    @FXML
    private void handleStarExit() {
        if (!isRatingSelected) {
            resetStars();
        }
    }

    @FXML
    private void handleStarClick(javafx.scene.input.MouseEvent event) {
        Label clickedStar = (Label) event.getSource();
        resetStars();
        isRatingSelected = true;

        if (clickedStar == star1) {
            rating = 1;
            fillStars(1);
            ratingText.setText("Poor");
        } else if (clickedStar == star2) {
            rating = 2;
            fillStars(2);
            ratingText.setText("Fair");
        } else if (clickedStar == star3) {
            rating = 3;
            fillStars(3);
            ratingText.setText("Good");
        } else if (clickedStar == star4) {
            rating = 4;
            fillStars(4);
            ratingText.setText("Very Good");
        } else if (clickedStar == star5) {
            rating = 5;
            fillStars(5);
            ratingText.setText("Excellent");
        }
    }

    private void fillStars(int count) {
        for (int i = 1; i <= count; i++) {
            Label star = getStarLabel(i);
            star.setText("★");
            star.setStyle("-fx-text-fill: #ffcc00; -fx-font-size: 40px;");
        }
    }

    private void resetStars() {
        for (int i = 1; i <= 5; i++) {
            Label star = getStarLabel(i);
            star.setText("☆");
            star.setStyle("-fx-text-fill: #d3d3d3; -fx-font-size: 40px;");
        }
    }

    private Label getStarLabel(int starNumber) {
        switch (starNumber) {
            case 2: return star2;
            case 3: return star3;
            case 4: return star4;
            case 5: return star5;
            default: return star1;
        }
    }

    @FXML
    private void handleSave() {
        if (rating == 0) {
            showAlert("Rating Required", "Please select a rating before submitting.");
            return;
        }

        String comment = commentArea.getText();

        // Here you would typically send this data to your backend
        System.out.println("Rating: " + rating);
        System.out.println("Comment: " + comment);

        showAlert("Thank You", "Thank you for your feedback!");
        closeWindow();
    }

    @FXML
    private void handleCancel() {
        closeWindow();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void closeWindow() {
        if (saveButton != null && saveButton.getScene() != null) {
            Stage stage = (Stage) saveButton.getScene().getWindow();
            stage.close();
        }
    }
}