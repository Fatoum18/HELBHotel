package com.helb.helbhotel;

import com.helb.helbhotel.config.DiscountCodeGenerator;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;


public class VerificationCodeController {

    @FXML
    private TextField codeTextField;

    @FXML
    private Label statusLabel;

    @FXML
    private Label tickLabel;

    @FXML
    private Label crossLabel;

    @FXML
    public void initialize() {
        tickLabel.setVisible(false);
        crossLabel.setVisible(false);
    }

    @FXML
    public void onCheckCode() {
        String code = codeTextField.getText().trim();
        Integer discount = DiscountCodeGenerator.decode(code);

        if (discount != null) {
            statusLabel.setText("Code valide: " + discount + "% de réduction");
            tickLabel.setVisible(true);
            crossLabel.setVisible(false);
        } else {
            statusLabel.setText("Code invalide !");
            tickLabel.setVisible(false);
            crossLabel.setVisible(true);
        }
    }
}
