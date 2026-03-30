package codename.idmc.ui.view.plateau;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import codename.idmc.application.validators.CardSelectionValidationResult;
import codename.idmc.application.validators.CardSelectionValidator;
import codename.idmc.app.Interfaces.*;
import codename.idmc.ui.common.RemoteCursorOverlay;
import codename.idmc.ui.view.Sauvegarde.SauvegarderPartiController;
import codename.idmc.ui.view.carte.CarteViewController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;

public class PlateauController implements Initializable {

    /* ================= FXML ================= */

    @FXML private Pane cursorOverlayContainer;

    @FXML private GridPane plateauPrincipal;
    @FXML private GridPane plateauMEspionGrand;
    @FXML private GridPane plateauMEspionMini;

    @FXML private ImageView xCloseMiniPlateau;

    @FXML private TextArea chatArea;
    @FXML private TextField chatInput;

    @FXML private Label infoEquipeRole;
    @FXML private Label infoTour;

    /* ================= STATE ================= */

    private Partie partieEnCours;
    private RemoteCursorOverlay remoteCursorOverlay;

    private CouleurEquipe equipeLocale;
    private String roleLocal;

    private final Map<Carte, CarteViewController> carteControllers = new HashMap<>();
    private final CardSelectionValidator validator = new CardSelectionValidator();

    /* ================= INIT ================= */

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        xCloseMiniPlateau.setVisible(false);
        plateauMEspionGrand.setVisible(false);

        initialiserOverlay();
        configurerInteractionsEspion();

        ajouterMessageChat("Système", "UI prête.");
    }

    private void initialiserOverlay() {
        if (cursorOverlayContainer != null) {
            remoteCursorOverlay = new RemoteCursorOverlay();
            remoteCursorOverlay.prefWidthProperty().bind(cursorOverlayContainer.widthProperty());
            remoteCursorOverlay.prefHeightProperty().bind(cursorOverlayContainer.heightProperty());
            cursorOverlayContainer.getChildren().add(remoteCursorOverlay);
        }
    }

    /* ================= CONFIG ================= */

    public void configurerVueTest(CouleurEquipe equipe, String role) {
        this.equipeLocale = equipe;
        this.roleLocal = role;
    }

    /* ================= DEMARRAGE ================= */

    public void demarrerAffichageJeu(Partie partie) {
        this.partieEnCours = partie;

        genererPlateaux();
        appliquerVueSelonRole();

        afficherInfosJoueur();
        afficherTour();

        ajouterMessageChat("Système",
                "Partie lancée - " + equipeLocale + " / " + roleLocal);
    }

    private void appliquerVueSelonRole() {
        if ("MAITRE_ESPION".equals(roleLocal)) {
            plateauMEspionMini.setVisible(true);
        } else {
            plateauMEspionMini.setVisible(false);
            plateauMEspionGrand.setVisible(false);
            plateauPrincipal.setVisible(true);
        }
    }

    /* ================= GENERATION ================= */

    private void genererPlateaux() {
        Plateau plateau = partieEnCours.getPlateau();

        plateauPrincipal.getChildren().clear();
        plateauMEspionMini.getChildren().clear();
        plateauMEspionGrand.getChildren().clear();
        carteControllers.clear();

        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {

                Carte carte = plateau.getCartePosition(i, j);
                if (carte == null) continue;

                genererCarte(carte, i, j);
                genererMini(carte, i, j);
                genererGrand(carte, i, j);
            }
        }
    }

    private void genererCarte(Carte carte, int ligne, int colonne) {
        try {
            FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/codename/idmc/ui/view/carte/carte_view.fxml")
            );

            StackPane node = loader.load();
            CarteViewController ctrl = loader.getController();

            ctrl.initialiserCarte(carte);
            ctrl.setClickListener(this::onCarteCliquee);

            carteControllers.put(carte, ctrl);
            plateauPrincipal.add(node, colonne, ligne);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void genererMini(Carte carte, int l, int c) {
        VBox box = new VBox();
        box.setStyle("-fx-background-color:" + getCouleurHex(carte) + "; -fx-border-color:black;");
        plateauMEspionMini.add(box, c, l);
    }

    private void genererGrand(Carte carte, int l, int c) {
        StackPane box = new StackPane();
        String couleur = getCouleurHex(carte);

        box.setStyle("-fx-background-color:" + couleur + "; -fx-border-color:black;");

        Label label = new Label(carte.getContenu());
        label.setStyle("-fx-font-size:18px; -fx-font-weight:bold;");

        box.getChildren().add(label);
        plateauMEspionGrand.add(box, c, l);
    }

    /* ================= LOGIQUE ================= */

    private void onCarteCliquee(Carte carte) {

        CardSelectionValidationResult result = validator.validate(
                partieEnCours, carte, equipeLocale, roleLocal);

        if (!result.isValide()) {
            afficher(result.getMessage());
            return;
        }

        boolean continuer = partieEnCours.jouerCarte(carte.getContenu());

        rafraichirCartes();
        rafraichirInfosPartie();

        if (!partieEnCours.isEstEnCours()) {
            afficher("Partie terminée");
            return;
        }

        ajouterMessageChat("Système",
                continuer ? "Bonne carte" : "Fin du tour");
    }

    public void donnerIndiceTest(String mot, int nb) {
        if (!"MAITRE_ESPION".equals(roleLocal)) {
            afficher("Seul le maître espion peut donner un indice");
            return;
        }

        partieEnCours.donnerIndice(mot, nb);
        ajouterMessageChat("Indice", mot + " (" + nb + ")");
    }

    /* ================= REFRESH ================= */

    private void rafraichirCartes() {
        carteControllers.values().forEach(CarteViewController::rafraichirAffichage);
    }

    private void rafraichirInfosPartie() {
        afficherTour();

        ajouterMessageChat("Tour",
                partieEnCours.getEquipeCourante().getNom());
    }

    /* ================= HUD ================= */

    private void afficherInfosJoueur() {
        if (infoEquipeRole == null) return;

        String equipe = (equipeLocale == CouleurEquipe.ROUGE) ? "ROUGE" : "BLEU";
        String role = "AGENT".equals(roleLocal) ? "Agent" : "Maître espion";

        infoEquipeRole.setText("Vous êtes : " + role + " - " + equipe);

        if (equipeLocale == CouleurEquipe.ROUGE) {
            infoEquipeRole.setStyle("-fx-text-fill:#E53935; -fx-font-weight:bold;");
        } else {
            infoEquipeRole.setStyle("-fx-text-fill:#1E88E5; -fx-font-weight:bold;");
        }
    }

    private void afficherTour() {
        if (infoTour == null || partieEnCours == null) return;

        infoTour.setText("Tour : " + partieEnCours.getEquipeCourante().getNom());

        if (partieEnCours.getEquipeCourante().getCouleur() == equipeLocale) {
            infoTour.setStyle("-fx-text-fill: green; -fx-font-weight:bold;");
        } else {
            infoTour.setStyle("-fx-text-fill: gray;");
        }
    }

    /* ================= CHAT ================= */

    @FXML
    public void envoyerMessageChat(ActionEvent e) {
        String msg = chatInput.getText();
        if (msg == null || msg.isBlank()) return;

        ajouterMessageChat("Moi", msg);
        chatInput.clear();
    }

    private void ajouterMessageChat(String auteur, String msg) {
        chatArea.appendText(auteur + " : " + msg + "\n");
    }

    /* ================= UTILS ================= */

    private String getCouleurHex(Carte c) {
        return switch (c.getType()) {
            case ROUGE -> "#E53935";
            case BLEU -> "#1E88E5";
            case NEUTRE -> "#D6D6D6";
            case ASSASSIN -> "#212121";
        };
    }

    private void afficher(String msg) {
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setContentText(msg);
        a.showAndWait();
    }

    private void configurerInteractionsEspion() {
        plateauMEspionMini.setOnMouseClicked(e -> {
            plateauPrincipal.setVisible(false);
            plateauMEspionGrand.setVisible(true);
            xCloseMiniPlateau.setVisible(true);
        });

        xCloseMiniPlateau.setOnMouseClicked(e -> {
            plateauPrincipal.setVisible(true);
            plateauMEspionGrand.setVisible(false);
            xCloseMiniPlateau.setVisible(false);
        });

        plateauMEspionMini.setCursor(Cursor.HAND);
        xCloseMiniPlateau.setCursor(Cursor.HAND);
    }

    /* ================= CURSORS ================= */

    public void updateRemoteCursor(String id, String pseudo, double x, double y) {
        remoteCursorOverlay.updateCursor(id, pseudo, x, y);
    }

    public void removeRemoteCursor(String id) {
        remoteCursorOverlay.removeCursor(id);
    }

    /* ================= SAVE ================= */

    @FXML
    public void sauvegarderPartie() {
        if (partieEnCours != null) {
            SauvegarderPartiController.ouvrirPopup(partieEnCours);
        }
    }
}