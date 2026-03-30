package codename.idmc.ui.view.plateau;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import codename.idmc.application.validators.CardSelectionValidationResult;
import codename.idmc.application.validators.CardSelectionValidator;
import codename.idmc.app.Interfaces.Carte;
import codename.idmc.app.Interfaces.CouleurEquipe;
import codename.idmc.app.Interfaces.Partie;
import codename.idmc.app.Interfaces.Plateau;
import codename.idmc.infrastructure.network.multiplayer.client.GameClient;
import codename.idmc.infrastructure.network.multiplayer.dto.ChatMessageDto;
import codename.idmc.infrastructure.network.multiplayer.dto.CursorPositionDto;
import codename.idmc.infrastructure.network.multiplayer.dto.NetworkMessage;
import codename.idmc.infrastructure.network.multiplayer.protocol.MessageType;
import codename.idmc.ui.common.RemoteCursorOverlay;
import codename.idmc.ui.view.Sauvegarde.SauvegarderPartiController;
import codename.idmc.ui.view.carte.CarteViewController;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.application.Platform;
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
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

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

    @FXML
    private Label infoEquipeRole;

    @FXML
    private Label infoTour;

    @FXML
    private Label scoreRougeLabel;

    @FXML
    private Label scoreBleuLabel;

    private Partie partieEnCours;
    private RemoteCursorOverlay remoteCursorOverlay;

    private CouleurEquipe equipeLocale;
    private String roleLocal;
    private String pseudoLocal = "Moi";

    private final Map<Carte, CarteViewController> carteControllers = new HashMap<>();
    private final CardSelectionValidator validator = new CardSelectionValidator();

    private GameClient client;
    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        if (xCloseMiniPlateau != null) {
            xCloseMiniPlateau.setVisible(false);
        }

        if (plateauMEspionGrand != null) {
            plateauMEspionGrand.setVisible(false);
        }

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

    public void setClient(GameClient client) {
        this.client = client;

        if (this.client != null) {
            this.client.setMessageListener(this::handleNetworkMessage);
            installerTrackingCurseur();
        }
    }

    public void configurerVueTest(CouleurEquipe equipe, String role) {
        this.equipeLocale = equipe;
        this.roleLocal = role;
    }

    public void configurerJoueurLocal(String pseudo, CouleurEquipe equipe, String role) {
        this.pseudoLocal = (pseudo == null || pseudo.isBlank()) ? "Moi" : pseudo;
        this.equipeLocale = equipe;
        this.roleLocal = role;
    }

    public void demarrerAffichageJeu(Partie partie) {
        this.partieEnCours = partie;

        if (this.partieEnCours == null) {
            afficher("Impossible d'afficher la partie : partie nulle.");
            return;
        }

        if (!partieEnCours.isEstEnCours()) {
            partieEnCours.demarrerPartie();
        }

        genererPlateaux();
        appliquerVueSelonRole();
        afficherInfosJoueur();
        afficherTour();
        afficherScores();

        ajouterMessageChat(
                "Système",
                "Partie lancée - " + valeurRole() + " / " + valeurEquipe()
        );
    }

    private void handleNetworkMessage(NetworkMessage msg) {
        switch (msg.getType()) {

            case CHAT_MESSAGE -> {
                ChatMessageDto chat = mapper.convertValue(
                        msg.getPayload(),
                        ChatMessageDto.class
                );

                Platform.runLater(() -> {
                    String auteur = extraireAuteurChat(chat);
                    String message = extraireMessageChat(chat);
                    ajouterMessageChat(auteur, message);
                });
            }

            case CURSOR_MOVED -> {
                CursorPositionDto cursor = mapper.convertValue(
                        msg.getPayload(),
                        CursorPositionDto.class
                );

                Platform.runLater(() -> {
                    if (remoteCursorOverlay != null) {
                        remoteCursorOverlay.updateCursor(
                                msg.getPlayerId(),
                                extrairePseudoCursor(cursor),
                                extraireXCursor(cursor),
                                extraireYCursor(cursor)
                        );
                    }
                });
            }

            default -> {
            }
        }
    }

    private void installerTrackingCurseur() {
        if (cursorOverlayContainer == null || client == null) {
            return;
        }

        cursorOverlayContainer.setOnMouseMoved(event -> {
            try {
                client.send(new NetworkMessage(
                        MessageType.CURSOR_MOVED,
                        "CLIENT",
                        Map.of(
                                "pseudo", pseudoLocal,
                                "x", event.getX(),
                                "y", event.getY()
                        )
                ));
            } catch (Exception ignored) {
            }
        });
    }

    private void appliquerVueSelonRole() {
        if (plateauMEspionMini == null || plateauMEspionGrand == null || plateauPrincipal == null) {
            return;
        }

        if ("MAITRE_ESPION".equals(roleLocal)) {
            plateauMEspionMini.setVisible(true);
            plateauPrincipal.setVisible(true);
        } else {
            plateauMEspionMini.setVisible(false);
            plateauMEspionGrand.setVisible(false);
            plateauPrincipal.setVisible(true);
            if (xCloseMiniPlateau != null) {
                xCloseMiniPlateau.setVisible(false);
            }
        }
    }

    private void genererPlateaux() {
        if (partieEnCours == null || partieEnCours.getPlateau() == null) {
            return;
        }

        Plateau plateau = partieEnCours.getPlateau();

        plateauPrincipal.getChildren().clear();
        plateauMEspionMini.getChildren().clear();
        plateauMEspionGrand.getChildren().clear();
        carteControllers.clear();

        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                Carte carte = plateau.getCartePosition(i, j);
                if (carte == null) {
                    continue;
                }

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
            CarteViewController controller = loader.getController();

            controller.initialiserCarte(carte);
            controller.setClickListener(this::onCarteCliquee);

            carteControllers.put(carte, controller);
            plateauPrincipal.add(node, colonne, ligne);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void genererMini(Carte carte, int ligne, int colonne) {
        VBox box = new VBox();
        box.setStyle("-fx-background-color:" + getCouleurHex(carte) + "; -fx-border-color:black;");
        plateauMEspionMini.add(box, colonne, ligne);
    }

    private void genererGrand(Carte carte, int ligne, int colonne) {
        StackPane box = new StackPane();
        String couleur = getCouleurHex(carte);

        box.setStyle("-fx-background-color:" + couleur + "; -fx-border-color:black;");

        Label label = new Label(carte.getContenu());
        label.setStyle("-fx-font-size:18px; -fx-font-weight:bold;");

        box.getChildren().add(label);
        plateauMEspionGrand.add(box, colonne, ligne);
    }

    private void onCarteCliquee(Carte carte) {
        if (partieEnCours == null) {
            return;
        }

        CardSelectionValidationResult result = validator.validate(
                partieEnCours,
                carte,
                equipeLocale,
                roleLocal
        );

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

        ajouterMessageChat(
                "Système",
                continuer ? "Bonne carte" : "Fin du tour"
        );
    }

    public void donnerIndiceTest(String mot, int nb) {
        if (!"MAITRE_ESPION".equals(roleLocal)) {
            afficher("Seul le maître espion peut donner un indice");
            return;
        }

        if (partieEnCours == null) {
            return;
        }

        partieEnCours.donnerIndice(mot, nb);
        ajouterMessageChat("Indice", mot + " (" + nb + ")");
    }

    private void rafraichirCartes() {
        carteControllers.values().forEach(CarteViewController::rafraichirAffichage);
    }

    private void rafraichirInfosPartie() {
        afficherTour();
        afficherScores();

        if (partieEnCours != null && partieEnCours.getEquipeCourante() != null) {
            ajouterMessageChat("Tour", partieEnCours.getEquipeCourante().getNom());
        }
    }

    private void afficherInfosJoueur() {
        if (infoEquipeRole == null) {
            return;
        }

        String equipe = valeurEquipe();
        String role = valeurRole();

        infoEquipeRole.setText("Vous êtes : " + role + " - " + equipe);

        if (equipeLocale == CouleurEquipe.ROUGE) {
            infoEquipeRole.setStyle("-fx-text-fill:#E53935; -fx-font-weight:bold;");
        } else if (equipeLocale == CouleurEquipe.BLEU) {
            infoEquipeRole.setStyle("-fx-text-fill:#1E88E5; -fx-font-weight:bold;");
        } else {
            infoEquipeRole.setStyle("-fx-font-weight:bold;");
        }
    }

    private void afficherTour() {
        if (infoTour == null || partieEnCours == null || partieEnCours.getEquipeCourante() == null) {
            return;
        }

        infoTour.setText("Tour : " + partieEnCours.getEquipeCourante().getNom());

        if (equipeLocale != null && partieEnCours.getEquipeCourante().getCouleur() == equipeLocale) {
            infoTour.setStyle("-fx-text-fill: green; -fx-font-weight:bold;");
        } else {
            infoTour.setStyle("-fx-text-fill: gray;");
        }
    }

    private void afficherScores() {
        if (partieEnCours == null) {
            return;
        }

        if (scoreRougeLabel != null) {
            scoreRougeLabel.setText("Rouge : " + partieEnCours.getScoreRouge());
        }

        if (scoreBleuLabel != null) {
            scoreBleuLabel.setText("Bleu : " + partieEnCours.getScoreBleu());
        }
    }

    @FXML
    public void envoyerMessageChat(ActionEvent event) {
        String msg = chatInput.getText();

        if (msg == null || msg.isBlank()) {
            return;
        }

        if (client != null) {
            try {
                client.send(new NetworkMessage(
                        MessageType.CHAT_MESSAGE,
                        "CLIENT",
                        Map.of(
                                "pseudo", pseudoLocal,
                                "message", msg.trim()
                        )
                ));
            } catch (Exception ex) {
                ajouterMessageChat("Système", "Erreur envoi chat");
            }
        } else {
            ajouterMessageChat(pseudoLocal, msg.trim());
        }

        chatInput.clear();
    }

    private void ajouterMessageChat(String auteur, String msg) {
        if (chatArea == null) {
            return;
        }

        chatArea.appendText(auteur + " : " + msg + "\n");
    }

    private String getCouleurHex(Carte carte) {
        return switch (carte.getType()) {
            case ROUGE -> "#E53935";
            case BLEU -> "#1E88E5";
            case NEUTRE -> "#D6D6D6";
            case ASSASSIN -> "#212121";
        };
    }

    private void afficher(String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText(msg);
        alert.showAndWait();
    }

    private void configurerInteractionsEspion() {
        if (plateauMEspionMini != null) {
            plateauMEspionMini.setOnMouseClicked(event -> {
                if (!"MAITRE_ESPION".equals(roleLocal)) {
                    return;
                }

                plateauPrincipal.setVisible(false);
                plateauMEspionGrand.setVisible(true);
                if (xCloseMiniPlateau != null) {
                    xCloseMiniPlateau.setVisible(true);
                }
            });

            plateauMEspionMini.setCursor(Cursor.HAND);
        }

        if (xCloseMiniPlateau != null) {
            xCloseMiniPlateau.setOnMouseClicked(event -> {
                plateauPrincipal.setVisible(true);
                plateauMEspionGrand.setVisible(false);
                xCloseMiniPlateau.setVisible(false);
            });

            xCloseMiniPlateau.setCursor(Cursor.HAND);
        }
    }

    private String extraireAuteurChat(ChatMessageDto chat) {
        try {
            return (String) chat.getClass().getMethod("getPseudo").invoke(chat);
        } catch (Exception e1) {
            try {
                return (String) chat.getClass().getMethod("getAuteur").invoke(chat);
            } catch (Exception e2) {
                return "Joueur";
            }
        }
    }

    private String extraireMessageChat(ChatMessageDto chat) {
        try {
            return (String) chat.getClass().getMethod("getMessage").invoke(chat);
        } catch (Exception e) {
            return "";
        }
    }

    private String extrairePseudoCursor(CursorPositionDto cursor) {
        try {
            return (String) cursor.getClass().getMethod("getPseudo").invoke(cursor);
        } catch (Exception e) {
            return "Joueur";
        }
    }

    private double extraireXCursor(CursorPositionDto cursor) {
        try {
            Object value = cursor.getClass().getMethod("getX").invoke(cursor);
            return ((Number) value).doubleValue();
        } catch (Exception e) {
            return 0;
        }
    }

    private double extraireYCursor(CursorPositionDto cursor) {
        try {
            Object value = cursor.getClass().getMethod("getY").invoke(cursor);
            return ((Number) value).doubleValue();
        } catch (Exception e) {
            return 0;
        }
    }

    private String valeurEquipe() {
        if (equipeLocale == null) {
            return "Inconnue";
        }
        return equipeLocale == CouleurEquipe.ROUGE ? "ROUGE" : "BLEU";
    }

    private String valeurRole() {
        if (roleLocal == null) {
            return "Inconnu";
        }
        return "AGENT".equals(roleLocal) ? "Agent" : "Maître espion";
    }

    public void updateRemoteCursor(String id, String pseudo, double x, double y) {
        if (remoteCursorOverlay != null) {
            remoteCursorOverlay.updateCursor(id, pseudo, x, y);
        }
    }

    public void removeRemoteCursor(String id) {
        if (remoteCursorOverlay != null) {
            remoteCursorOverlay.removeCursor(id);
        }
    }

    @FXML
    public void sauvegarderPartie() {
        if (partieEnCours != null) {
            SauvegarderPartiController.ouvrirPopup(partieEnCours);
        }
    }
}