package codename.idmc.ui.view.lobbybrowser;

import codename.idmc.infrastructure.network.multiplayer.client.GameClient;
import codename.idmc.infrastructure.network.multiplayer.discovery.LobbyDiscoveryClient;
import codename.idmc.infrastructure.network.multiplayer.dto.LobbyInfoDto;
import codename.idmc.infrastructure.network.multiplayer.dto.NetworkMessage;
import codename.idmc.infrastructure.network.multiplayer.protocol.MessageType;
import codename.idmc.ui.view.lobby.LobbyController;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.util.List;
import java.util.Map;

public class LobbyBrowserController {

    @FXML
    private TableView<LobbyInfoDto> lobbiesTable;

    @FXML
    private TableColumn<LobbyInfoDto, String> nameColumn;

    @FXML
    private TableColumn<LobbyInfoDto, String> hostColumn;

    @FXML
    private TableColumn<LobbyInfoDto, String> addressColumn;

    @FXML
    private TableColumn<LobbyInfoDto, String> playersColumn;

    @FXML
    private TableColumn<LobbyInfoDto, String> statusColumn;

    @FXML
    private TextField ipField;

    @FXML
    private Label messageLabel;

    private final ObservableList<LobbyInfoDto> lobbies = FXCollections.observableArrayList();
    private final LobbyDiscoveryClient discoveryClient = new LobbyDiscoveryClient();

    @FXML
    public void initialize() {
        nameColumn.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getLobbyName()));

        hostColumn.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getHostPseudo()));

        addressColumn.setCellValueFactory(data ->
                new SimpleStringProperty(
                        data.getValue().getHostAddress() + ":" + data.getValue().getTcpPort()
                ));

        playersColumn.setCellValueFactory(data ->
                new SimpleStringProperty(
                        data.getValue().getCurrentPlayers() + "/" + data.getValue().getMaxPlayers()
                ));

        statusColumn.setCellValueFactory(data ->
                new SimpleStringProperty(
                        data.getValue().isStarted() ? "En jeu" : "En attente"
                ));

        lobbiesTable.setItems(lobbies);
        actualiserLobbies();
    }

    @FXML
    private void actualiserLobbies() {
        messageLabel.setText("Recherche des lobbies...");

        new Thread(() -> {
            List<LobbyInfoDto> result = discoveryClient.discover(1500);

            Platform.runLater(() -> {
                lobbies.setAll(result);

                if (result.isEmpty()) {
                    messageLabel.setText("Aucun lobby trouvé.");
                } else {
                    messageLabel.setText(result.size() + " lobby(s) trouvé(s).");
                }
            });
        }).start();
    }

    @FXML
    private void rejoindreLobby() {
        LobbyInfoDto selected = lobbiesTable.getSelectionModel().getSelectedItem();

        if (selected == null) {
            messageLabel.setText("Sélectionne un lobby.");
            return;
        }

        connecterAuServeur(selected.getHostAddress(), selected.getTcpPort());
    }

    @FXML
    private void connexionDirecte() {
        String ip = ipField.getText();

        if (ip == null || ip.isBlank()) {
            messageLabel.setText("Entre une IP.");
            return;
        }

        connecterAuServeur(ip.trim(), 5555);
    }

    private void connecterAuServeur(String host, int port) {
        messageLabel.setText("Connexion en cours...");

        new Thread(() -> {
            try {
                GameClient client = new GameClient(host, port);
                client.connect();

                client.send(new NetworkMessage(
                        MessageType.JOIN_LOBBY,
                        "TEMP_ID",
                        Map.of("pseudo", "Sasha")
                ));

                Platform.runLater(() -> {
                    messageLabel.setText("Connecté !");
                    ouvrirLobby(client);
                });

            } catch (Exception e) {
                Platform.runLater(() ->
                        messageLabel.setText("Erreur : " + e.getMessage()));
            }
        }).start();
    }

    private void ouvrirLobby(GameClient client) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/codename/idmc/ui/view/lobby/lobby_view.fxml")
            );

            Parent root = loader.load();

            LobbyController controller = loader.getController();
            controller.setClient(client);

            Stage stage = (Stage) lobbiesTable.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Lobby - Codenames");

        } catch (Exception e) {
            e.printStackTrace();
            messageLabel.setText("Erreur ouverture lobby");
        }
    }

    @FXML
    private void retourMenu() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/codename/idmc/ui/view/menu/main_menu_view.fxml")
            );

            Parent root = loader.load();

            Stage stage = (Stage) lobbiesTable.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Code Name IDMC");

        } catch (Exception e) {
            e.printStackTrace();
            messageLabel.setText("Erreur retour menu");
        }
    }
}