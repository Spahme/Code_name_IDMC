package codename.idmc.ui.view.carte;

import codename.idmc.app.Interfaces.Carte;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

public class CarteViewController {

    @FXML
    private StackPane rootCard;

    @FXML
    private Label labelMot;

    private Carte carteModele;

    public void initialiserCarte(Carte carte) {
        this.carteModele = carte;
        rafraichirAffichage();
    }

    private void rafraichirAffichage() {
        if (carteModele == null) {
            return;
        }

        labelMot.setText(carteModele.getContenu());

        if (carteModele.isRetournee()) {
            rootCard.setStyle(
                "-fx-background-color: " + carteModele.getCouleurHex() + ";" +
                "-fx-border-color: black;" +
                "-fx-border-width: 1;" +
                "-fx-background-radius: 8;" +
                "-fx-border-radius: 8;"
            );

            if ("#212121".equalsIgnoreCase(carteModele.getCouleurHex())
                    || "#1E88E5".equalsIgnoreCase(carteModele.getCouleurHex())) {
                labelMot.setTextFill(Color.WHITE);
            } else {
                labelMot.setTextFill(Color.BLACK);
            }
        } else {
            rootCard.setStyle(
                "-fx-background-color: white;" +
                "-fx-border-color: black;" +
                "-fx-border-width: 1;" +
                "-fx-background-radius: 8;" +
                "-fx-border-radius: 8;"
            );
            labelMot.setTextFill(Color.BLACK);
        }
    }
}