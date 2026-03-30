package codename.idmc.ui.view.lobby;

import codename.idmc.infrastructure.network.multiplayer.client.GameClient;
import codename.idmc.infrastructure.network.multiplayer.dto.LobbyStateDto;
import codename.idmc.infrastructure.network.multiplayer.dto.NetworkMessage;
import codename.idmc.infrastructure.network.multiplayer.protocol.MessageType;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.util.Map;

public class LobbyController {

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

    private LobbyPlayerRow joueurSelectionne;
    private boolean miseAJourEnCours = false;

    @FXML
    public void initialize() {
        equipeCombo.setItems(FXCollections.observableArrayList("ROUGE", "BLEU"));
        roleCombo.setItems(FXCollections.observableArrayList("AGENT", "MAITRE_ESPION"));

        pseudoColumn.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getPseudo()));

        equipeColumn.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getEquipe().name()));

        roleColumn.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getRole()));

        joueursTable.setItems(joueurs);

        joueursTable.getSelectionModel().selectedItemProperty().addListener((obs, oldValue, newValue) -> {
            joueurSelectionne = newValue;
            chargerSelectionDansFormulaire(newValue);
        });

        equipeCombo.setOnAction(event -> envoyerModificationSiPossible());
        roleCombo.setOnAction(event -> envoyerModificationSiPossible());
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
                String pseudoSelectionne = joueurSelectionne != null
                        ? joueurSelectionne.getPseudo()
                        : null;

                joueurs.clear();

                state.getPlayers().forEach(p -> {
                    joueurs.add(new LobbyPlayerRow(
                            p.getPlayerId(),
                            p.getPseudo(),
                            "ROUGE".equals(p.getEquipe())
                                    ? codename.idmc.app.Interfaces.CouleurEquipe.ROUGE
                                    : codename.idmc.app.Interfaces.CouleurEquipe.BLEU,
                            p.getRole()
                    ));
                });

                if (pseudoSelectionne != null) {
                    joueurs.stream()
                            .filter(j -> j.getPseudo().equals(pseudoSelectionne))
                            .findFirst()
                            .ifPresent(j -> {
                                joueursTable.getSelectionModel().select(j);
                                joueurSelectionne = j;
                                chargerSelectionDansFormulaire(j);
                            });
                }

                messageLabel.setText("Lobby synchronisé");
            });
        }

        if (msg.getType() == MessageType.START_GAME) {
            Platform.runLater(() -> messageLabel.setText("La partie démarre..."));
        }
    }

    private void chargerSelectionDansFormulaire(LobbyPlayerRow row) {
        if (row == null) {
            return;
        }

        miseAJourEnCours = true;
        equipeCombo.setValue(row.getEquipe().name());
        roleCombo.setValue(row.getRole());
        miseAJourEnCours = false;

        messageLabel.setText("Joueur sélectionné : " + row.getPseudo());
    }

    private void envoyerModificationSiPossible() {
        if (miseAJourEnCours) {
            return;
        }

        if (joueurSelectionne == null) {
            return;
        }

        if (client == null) {
            return;
        }

        String equipe = equipeCombo.getValue();
        String role = roleCombo.getValue();

        if (equipe == null || role == null) {
            return;
        }

        joueurSelectionne.setEquipe(
                "ROUGE".equals(equipe)
                        ? codename.idmc.app.Interfaces.CouleurEquipe.ROUGE
                        : codename.idmc.app.Interfaces.CouleurEquipe.BLEU
        );
        joueurSelectionne.setRole(role);
        joueursTable.refresh();

        try {
            client.send(new NetworkMessage(
                    MessageType.UPDATE_PLAYER,
                    "CLIENT",
                    Map.of(
                            "equipe", equipe,
                            "role", role,
                            "ready", true
                    )
            ));

            messageLabel.setText("Rôle et équipe mis à jour.");
        } catch (Exception e) {
            messageLabel.setText("Erreur envoi modification réseau.");
        }
    }

    @FXML
    private void annulerSelection() {
        joueursTable.getSelectionModel().clearSelection();
        joueurSelectionne = null;

        miseAJourEnCours = true;
        equipeCombo.getSelectionModel().clearSelection();
        roleCombo.getSelectionModel().clearSelection();
        miseAJourEnCours = false;

        messageLabel.setText("Sélection annulée.");
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
            } catch (Exception e) {
                messageLabel.setText("Erreur lancement réseau");
            }
        }
    }
}