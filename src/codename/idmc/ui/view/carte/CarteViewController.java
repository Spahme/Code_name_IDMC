package codename.idmc.ui.view.carte;

import codename.idmc.app.Interfaces.Carte;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

public class CarteViewController {

    @FXML
    private StackPane faceCachee;

    @FXML
    private Pane fondCouleur;

    @FXML
    private BorderPane carteRoot;

    @FXML
    private Label motLabel;

    private Carte carteModele;
    private CardClickListener clickListener;

    public void initialiserCarte(Carte carte) {
        this.carteModele = carte;
        rafraichirAffichage();
    }

    public void setClickListener(CardClickListener clickListener) {
        this.clickListener = clickListener;
    }

    @FXML
    private void auClicSurCarte(MouseEvent event) {
        if (carteModele == null) {
            return;
        }

        if (clickListener != null) {
            clickListener.onCardClicked(carteModele);
        }
    }

    public void rafraichirAffichage() {
        if (carteModele == null) {
            motLabel.setText("Aucune carte");
            faceCachee.setVisible(false);
            return;
        }

        motLabel.setText(carteModele.getContenu());

        if (carteModele.isRetournee()) {
            afficherCarteRetournee();
        } else {
            afficherCarteNormale();
        }
    }

    private void afficherCarteNormale() {
        faceCachee.setVisible(false);

        carteRoot.setStyle(
            "-fx-background-color: white;" +
            "-fx-border-color: black;" +
            "-fx-border-width: 1;" +
            "-fx-background-radius: 6;" +
            "-fx-border-radius: 6;"
        );

        motLabel.setTextFill(Color.BLACK);
    }

    private void afficherCarteRetournee() {
        String couleurHex = carteModele.getCouleurHex();

        faceCachee.setVisible(true);

        fondCouleur.setStyle(
            "-fx-background-color: " + couleurHex + ";" +
            "-fx-background-radius: 6;" +
            "-fx-border-radius: 6;"
        );

        carteRoot.setStyle(
            "-fx-background-color: " + couleurHex + ";" +
            "-fx-border-color: black;" +
            "-fx-border-width: 1;" +
            "-fx-background-radius: 6;" +
            "-fx-border-radius: 6;"
        );

        if ("#212121".equalsIgnoreCase(couleurHex) || "#1E88E5".equalsIgnoreCase(couleurHex)) {
            motLabel.setTextFill(Color.WHITE);
        } else {
            motLabel.setTextFill(Color.BLACK);
        }
    }
}