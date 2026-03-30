package codename.idmc.ui.view.networklobby;

import codename.idmc.application.usecases.CreatePartieUseCase;
import codename.idmc.app.Interfaces.Agent;
import codename.idmc.app.Interfaces.CouleurEquipe;
import codename.idmc.app.Interfaces.MaitreEspion;
import codename.idmc.app.Interfaces.Partie;
import codename.idmc.infrastructure.network.multiplayer.client.GameClient;
import codename.idmc.infrastructure.network.multiplayer.dto.GameStartDto;
import codename.idmc.infrastructure.network.multiplayer.dto.LobbyStateDto;
import codename.idmc.infrastructure.network.multiplayer.dto.NetworkMessage;
import codename.idmc.infrastructure.network.multiplayer.protocol.MessageType;
import codename.idmc.ui.view.plateau.PlateauController;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

import java.util.Map;

public class NetworkLobbyController {

    @FXML
    private ListView<String> playersList;

    @FXML
    private Label statusLabel;

    private GameClient client;
    private final ObjectMapper mapper = new ObjectMapper();

    public void setClient(GameClient client) {
        this.client = client;

        statusLabel.setText("Connecté au serveur");

        client.setMessageListener(this::handleMessage);

        demanderEtatLobby();
        envoyerParametresPartieParDefaut();
    }

    private void demanderEtatLobby() {
        try {
            client.send(new NetworkMessage(
                    MessageType.LOBBY_STATE,
                    "CLIENT",
                    "REQUEST"
            ));
        } catch (Exception e) {
            System.err.println("Erreur demande état lobby");
        }
    }

    private void envoyerParametresPartieParDefaut() {
        try {
            client.send(new NetworkMessage(
                    MessageType.UPDATE_GAME_SETTINGS,
                    "CLIENT",
                    Map.of(
                            "langueId", 1,
                            "difficultyId", 3,
                            "categorieId", 1,
                            "equipeCommencante", "ROUGE"
                    )
            ));
        } catch (Exception e) {
            System.err.println("Erreur envoi paramètres partie");
        }
    }

    private void handleMessage(NetworkMessage msg) {
        System.out.println("Message reçu : " + msg.getType());

        switch (msg.getType()) {
            case LOBBY_STATE -> handleLobbyState(msg);

            case START_GAME -> {
                GameStartDto dto = mapper.convertValue(
                        msg.getPayload(),
                        GameStartDto.class
                );

                Platform.runLater(() -> ouvrirPlateau(dto));
            }

            case ERROR -> Platform.runLater(() ->
                    statusLabel.setText("Erreur serveur"));

            default -> {
            }
        }
    }

    private void handleLobbyState(NetworkMessage msg) {
        LobbyStateDto state = mapper.convertValue(
                msg.getPayload(),
                LobbyStateDto.class
        );

        Platform.runLater(() -> {
            playersList.getItems().clear();

            state.getPlayers().forEach(p ->
                    playersList.getItems().add(
                            p.getPseudo() + " [" + p.getEquipe() + " / " + p.getRole() + "]"
                    )
            );

            statusLabel.setText("Joueurs connectés : " + state.getPlayers().size());
        });
    }

    @FXML
    private void startGame() {
        try {
            System.out.println("Envoi START_GAME au serveur");

            client.send(new NetworkMessage(
                    MessageType.START_GAME,
                    "CLIENT",
                    "START"
            ));

            statusLabel.setText("Lancement de la partie...");
        } catch (Exception e) {
            e.printStackTrace();
            statusLabel.setText("Erreur lancement");
        }
    }

    private void ouvrirPlateau(GameStartDto dto) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/codename/idmc/ui/view/plateau/board_view.fxml")
            );

            Parent root = loader.load();

            PlateauController controller = loader.getController();

            CouleurEquipe equipeCommencante = "ROUGE".equals(dto.getEquipeCommencante())
                    ? CouleurEquipe.ROUGE
                    : CouleurEquipe.BLEU;

            CreatePartieUseCase useCase = new CreatePartieUseCase();
            Partie partie = useCase.execute(
                    dto.getLangueId(),
                    dto.getDifficultyId(),
                    dto.getCategorieId(),
                    equipeCommencante
            );

            dto.getPlayers().forEach(p -> {
                CouleurEquipe equipe = "ROUGE".equals(p.getEquipe())
                        ? CouleurEquipe.ROUGE
                        : CouleurEquipe.BLEU;

                if ("MAITRE_ESPION".equals(p.getRole())) {
                    partie.choisirEquipe(new MaitreEspion(p.getPseudo()), equipe);
                } else {
                    partie.choisirEquipe(new Agent(p.getPseudo()), equipe);
                }
            });

            controller.demarrerAffichageJeu(partie);

            Stage stage = (Stage) playersList.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Plateau - Multijoueur");

        } catch (Exception e) {
            e.printStackTrace();
            statusLabel.setText("Erreur ouverture plateau");
        }
    }
}