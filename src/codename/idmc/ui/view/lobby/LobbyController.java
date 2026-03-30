package codename.idmc.ui.view.lobby;

import codename.idmc.application.usecases.CreatePartieUseCase;
import codename.idmc.app.Interfaces.Agent;
import codename.idmc.app.Interfaces.CouleurEquipe;
import codename.idmc.app.Interfaces.Joueur;
import codename.idmc.app.Interfaces.MaitreEspion;
import codename.idmc.app.Interfaces.Partie;
import codename.idmc.infrastructure.network.multiplayer.client.GameClient;
import codename.idmc.infrastructure.network.multiplayer.dto.LobbyStateDto;
import codename.idmc.infrastructure.network.multiplayer.dto.NetworkMessage;
import codename.idmc.infrastructure.network.multiplayer.protocol.MessageType;
import codename.idmc.ui.view.plateau.PlateauController;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.util.Map;

public class LobbyController {

    @FXML
    private TextField pseudoField;

    @FXML
    private ComboBox<String> equipeCombo;

    @FXML
    private ComboBox<String> roleCombo;

    @FXML
    private TableView<LobbyPlayerRow> joueursTable;

    @FXML
    private TableColumn<LobbyPlayerRow, String> pseudoColumn;

    @FXML
    private TableColumn<LobbyPlayerRow, String> equipeColumn;

    @FXML
    private TableColumn<LobbyPlayerRow, String> roleColumn;

    @FXML
    private Label messageLabel;

    private final ObservableList<LobbyPlayerRow> joueurs = FXCollections.observableArrayList();

    private GameClient client;
    private final ObjectMapper mapper = new ObjectMapper();

    @FXML
    public void initialize() {
        equipeCombo.setItems(FXCollections.observableArrayList("ROUGE", "BLEU"));
        roleCombo.setItems(FXCollections.observableArrayList("AGENT", "MAITRE_ESPION"));

        pseudoColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getPseudo()));
        equipeColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getEquipe().name()));
        roleColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getRole()));

        joueursTable.setItems(joueurs);
    }

    public void setClient(GameClient client) {
        this.client = client;

        if (this.client != null) {
            this.client.setMessageListener(this::handleMessage);

            try {
                this.client.send(new NetworkMessage(
                        MessageType.LOBBY_STATE,
                        "CLIENT",
                        "REQUEST"
                ));
            } catch (Exception e) {
                messageLabel.setText("Erreur sync lobby");
            }
        }
    }

    private void handleMessage(NetworkMessage msg) {
        if (msg.getType() == MessageType.LOBBY_STATE) {
            LobbyStateDto state = mapper.convertValue(
                    msg.getPayload(),
                    LobbyStateDto.class
            );

            Platform.runLater(() -> {
                joueurs.clear();

                state.getPlayers().forEach(p -> {
                    CouleurEquipe equipe = "ROUGE".equals(p.getEquipe())
                            ? CouleurEquipe.ROUGE
                            : CouleurEquipe.BLEU;

                    joueurs.add(new LobbyPlayerRow(
                            p.getPseudo(),
                            equipe,
                            p.getRole()
                    ));
                });

                messageLabel.setText("Lobby synchronisé");
            });
        }

        if (msg.getType() == MessageType.START_GAME) {
            Platform.runLater(() -> messageLabel.setText("La partie démarre..."));
        }
    }

    @FXML
    private void ajouterJoueur() {
        String pseudo = pseudoField.getText();
        String equipe = equipeCombo.getValue();
        String role = roleCombo.getValue();

        if (pseudo == null || pseudo.isBlank()) {
            messageLabel.setText("Le pseudo est obligatoire.");
            return;
        }

        if (equipe == null || role == null) {
            messageLabel.setText("L'équipe et le rôle sont obligatoires.");
            return;
        }

        CouleurEquipe couleur = "ROUGE".equals(equipe) ? CouleurEquipe.ROUGE : CouleurEquipe.BLEU;

        joueurs.add(new LobbyPlayerRow(pseudo.trim(), couleur, role));

        pseudoField.clear();
        equipeCombo.getSelectionModel().clearSelection();
        roleCombo.getSelectionModel().clearSelection();
        messageLabel.setText("");
    }

    @FXML
    private void supprimerJoueurSelectionne() {
        LobbyPlayerRow selection = joueursTable.getSelectionModel().getSelectedItem();

        if (selection == null) {
            messageLabel.setText("Aucun joueur sélectionné.");
            return;
        }

        joueurs.remove(selection);
        messageLabel.setText("");
    }

    @FXML
    private void lancerPartie() {
        if (client != null) {
            try {
                client.send(new NetworkMessage(
                        MessageType.START_GAME,
                        "CLIENT",
                        "START"
                ));
                messageLabel.setText("Demande de lancement envoyée...");
                return;
            } catch (Exception e) {
                messageLabel.setText("Erreur lancement réseau");
                return;
            }
        }

        try {
            verifierConfiguration();

            CreatePartieUseCase useCase = new CreatePartieUseCase();
            Partie partie = useCase.execute(
                    1,
                    3,
                    1,
                    CouleurEquipe.ROUGE
            );

            for (LobbyPlayerRow row : joueurs) {
                Joueur joueur;

                if ("MAITRE_ESPION".equals(row.getRole())) {
                    joueur = new MaitreEspion(row.getPseudo());
                } else {
                    joueur = new Agent(row.getPseudo());
                }

                partie.choisirEquipe(joueur, row.getEquipe());
            }

            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/codename/idmc/ui/view/plateau/board_view.fxml")
            );

            Parent root = loader.load();

            PlateauController controller = loader.getController();
            controller.demarrerAffichageJeu(partie);

            Stage stage = (Stage) joueursTable.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Plateau - Codenames");
            stage.show();

        } catch (Exception e) {
            messageLabel.setText(e.getMessage());
        }
    }

    private void verifierConfiguration() {
        if (joueurs.size() < 4) {
            throw new IllegalStateException("Il faut au moins 4 joueurs.");
        }

        long rouges = joueurs.stream().filter(j -> j.getEquipe() == CouleurEquipe.ROUGE).count();
        long bleus = joueurs.stream().filter(j -> j.getEquipe() == CouleurEquipe.BLEU).count();

        if (rouges < 2 || bleus < 2) {
            throw new IllegalStateException("Chaque équipe doit avoir au moins 2 joueurs.");
        }

        long meRouge = joueurs.stream()
                .filter(j -> j.getEquipe() == CouleurEquipe.ROUGE)
                .filter(j -> "MAITRE_ESPION".equals(j.getRole()))
                .count();

        long meBleu = joueurs.stream()
                .filter(j -> j.getEquipe() == CouleurEquipe.BLEU)
                .filter(j -> "MAITRE_ESPION".equals(j.getRole()))
                .count();

        if (meRouge != 1 || meBleu != 1) {
            throw new IllegalStateException("Chaque équipe doit avoir exactement 1 maître espion.");
        }
    }
}