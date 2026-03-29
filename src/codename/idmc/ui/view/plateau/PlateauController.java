package codename.idmc.ui.view.plateau;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import codename.idmc.app.Interfaces.Partie;
import codename.idmc.app.Interfaces.Carte;
import codename.idmc.app.Interfaces.CouleurCarte;
import codename.idmc.app.Interfaces.Plateau;
import codename.idmc.ui.common.RemoteCursorOverlay;
import codename.idmc.ui.view.Sauvegarde.SauvegarderPartiController;
import codename.idmc.ui.view.carte.CarteViewController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import codename.idmc.app.Interfaces.CouleurEquipe;

public class PlateauController implements Initializable {

    @FXML
    private Pane cursorOverlayContainer;

    @FXML
    private GridPane plateauPrincipal;

    @FXML
    private GridPane plateauMEspionGrand;

    @FXML
    private GridPane plateauMEspionMini;

    @FXML
    private ImageView xCloseMiniPlateau;

    @FXML
    private TextArea chatArea;

    @FXML
    private TextField chatInput;

    private Partie partieEnCours;
    private RemoteCursorOverlay remoteCursorOverlay;

    
    private CouleurEquipe equipeTest;
    private String roleTest;
    
    public void configurerVueTest(CouleurEquipe equipe, String role) {
        this.equipeTest = equipe;
        this.roleTest = role;
    }
    
    private void appliquerConfigurationVue() {
        if (roleTest == null) {
            return;
        }

        if ("MAITRE_ESPION".equals(roleTest)) {
            plateauMEspionMini.setVisible(true);
        } else {
            plateauMEspionMini.setVisible(false);
            xCloseMiniPlateau.setVisible(false);
            plateauMEspionGrand.setVisible(false);
            plateauPrincipal.setVisible(true);
        }
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        xCloseMiniPlateau.setVisible(false);
        plateauMEspionGrand.setVisible(false);

        initialiserOverlayCurseurs();
        configurerInteractionsEspion();

        if (chatArea != null) {
            chatArea.setText("Système : chat initialisé.\n");
        }
    }

    private void initialiserOverlayCurseurs() {
        if (cursorOverlayContainer != null) {
            remoteCursorOverlay = new RemoteCursorOverlay();
            remoteCursorOverlay.prefWidthProperty().bind(cursorOverlayContainer.widthProperty());
            remoteCursorOverlay.prefHeightProperty().bind(cursorOverlayContainer.heightProperty());
            cursorOverlayContainer.getChildren().add(remoteCursorOverlay);
        }
    }

    public void demarrerAffichageJeu(Partie partie) {
        this.partieEnCours = partie;
        genererPlateaux();
        appliquerConfigurationVue();

        if (chatArea != null) {
            ajouterMessageChat("Système", "Partie chargée.");
        }
    }

    private void genererPlateaux() {
        if (partieEnCours == null || partieEnCours.getPlateau() == null) {
            System.out.println("Aucune partie ou aucun plateau disponible.");
            return;
        }

        Plateau vraiPlateau = partieEnCours.getPlateau();

        plateauPrincipal.getChildren().clear();
        plateauMEspionMini.getChildren().clear();
        plateauMEspionGrand.getChildren().clear();

        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                Carte vraieCarte = vraiPlateau.getCartePosition(i, j);

                if (vraieCarte == null) {
                    continue;
                }

                String couleurHex = getCouleurHex(vraieCarte);

                genererCartePrincipale(vraieCarte, i, j);
                genererMiniPlateauEspion(couleurHex, i, j);
                genererGrandPlateauEspion(vraieCarte, couleurHex, i, j);
            }
        }
    }

    private void genererCartePrincipale(Carte vraieCarte, int ligne, int colonne) {
        try {
            URL fxmlUrl = getClass().getResource("/codename/idmc/ui/view/carte/carte_view.fxml");

            if (fxmlUrl == null) {
                throw new IllegalStateException("FXML introuvable : /codename/idmc/ui/view/carte/carte_view.fxml");
            }

            FXMLLoader loader = new FXMLLoader(fxmlUrl);
            StackPane carteNode = loader.load();

            CarteViewController carteCtrl = loader.getController();
            carteCtrl.initialiserCarte(vraieCarte);

            carteNode.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
            GridPane.setHgrow(carteNode, Priority.ALWAYS);
            GridPane.setVgrow(carteNode, Priority.ALWAYS);

            plateauPrincipal.add(carteNode, colonne, ligne);

        } catch (IOException e) {
            System.err.println("Erreur de chargement du FXML de la carte : " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void genererMiniPlateauEspion(String couleurHex, int ligne, int colonne) {
        VBox miniCase = new VBox();
        miniCase.setStyle("-fx-background-color: " + couleurHex + "; -fx-border-color: black;");
        miniCase.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);

        GridPane.setHgrow(miniCase, Priority.ALWAYS);
        GridPane.setVgrow(miniCase, Priority.ALWAYS);

        plateauMEspionMini.add(miniCase, colonne, ligne);
    }

    private void genererGrandPlateauEspion(Carte vraieCarte, String couleurHex, int ligne, int colonne) {
        StackPane caseEspionGrand = new StackPane();
        caseEspionGrand.setStyle("-fx-background-color: " + couleurHex + "; -fx-border-color: black;");
        caseEspionGrand.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);

        GridPane.setHgrow(caseEspionGrand, Priority.ALWAYS);
        GridPane.setVgrow(caseEspionGrand, Priority.ALWAYS);

        Label motEspion = new Label(vraieCarte.getContenu());
        motEspion.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        if ("#212121".equalsIgnoreCase(couleurHex) || "#1E88E5".equalsIgnoreCase(couleurHex)) {
            motEspion.setTextFill(Color.WHITE);
        }

        caseEspionGrand.getChildren().add(motEspion);
        plateauMEspionGrand.add(caseEspionGrand, colonne, ligne);
    }

    private String getCouleurHex(Carte carte) {
        CouleurCarte type = carte.getType();

        return switch (type) {
            case ROUGE -> "#E53935";
            case BLEU -> "#1E88E5";
            case NEUTRE -> "#D6D6D6";
            case ASSASSIN -> "#212121";
        };
    }

    private void configurerInteractionsEspion() {
        plateauMEspionMini.setOnMouseClicked(event -> {
            plateauMEspionMini.setVisible(false);
            xCloseMiniPlateau.setVisible(true);

            plateauPrincipal.setVisible(false);
            plateauMEspionGrand.setVisible(true);
            plateauMEspionGrand.toFront();
        });

        xCloseMiniPlateau.setOnMouseClicked(event -> {
            xCloseMiniPlateau.setVisible(false);
            plateauMEspionMini.setVisible(true);

            plateauMEspionGrand.setVisible(false);
            plateauPrincipal.setVisible(true);
        });

        plateauMEspionMini.setCursor(Cursor.HAND);
        xCloseMiniPlateau.setCursor(Cursor.HAND);
    }

    @FXML
    public void envoyerMessageChat(ActionEvent event) {
        if (chatInput == null || chatArea == null) {
            return;
        }

        String message = chatInput.getText();

        if (message == null || message.isBlank()) {
            return;
        }

        ajouterMessageChat("Moi", message.trim());
        chatInput.clear();
    }

    public void ajouterMessageChat(String auteur, String message) {
        if (chatArea == null) {
            return;
        }

        chatArea.appendText(auteur + " : " + message + "\n");
    }

    public void updateRemoteCursor(String playerId, String pseudo, double x, double y) {
        if (remoteCursorOverlay == null) {
            return;
        }

        remoteCursorOverlay.updateCursor(playerId, pseudo, x, y);
    }

    public void removeRemoteCursor(String playerId) {
        if (remoteCursorOverlay == null) {
            return;
        }

        remoteCursorOverlay.removeCursor(playerId);
    }

    @FXML
    public void sauvegarderPartie() {
        if (partieEnCours != null) {
            SauvegarderPartiController.ouvrirPopup(partieEnCours);
        } else {
            System.out.println("Impossible de sauvegarder : aucune partie n'est en cours.");
        }
    }
}