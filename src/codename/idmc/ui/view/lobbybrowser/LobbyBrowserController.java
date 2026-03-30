package codename.idmc.ui.view.lobbybrowser;

import codename.idmc.ui.view.networklobby.NetworkLobbyController;
import codename.idmc.infrastructure.network.multiplayer.client.GameClient;
import codename.idmc.infrastructure.network.multiplayer.discovery.LobbyDiscoveryClient;
import codename.idmc.infrastructure.network.multiplayer.dto.LobbyInfoDto;
import codename.idmc.infrastructure.network.multiplayer.dto.NetworkMessage;
import codename.idmc.infrastructure.network.multiplayer.protocol.MessageType;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.util.List;
import java.util.Map;

public class LobbyBrowserController {

    /* ================= FXML ================= */

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

    /* ================= DATA ================= */

    private final ObservableList<LobbyInfoDto> lobbies = FXCollections.observableArrayList();
    private final LobbyDiscoveryClient discoveryClient = new LobbyDiscoveryClient();

    /* ================= INIT ================= */

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

    /* ================= ACTIONS ================= */

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

                // listener réseau (pour mise à jour lobby ensuite)
                client.setMessageListener(message -> {
                    System.out.println("Reçu : " + message.getType());
                });

                // envoi JOIN
                client.send(new NetworkMessage(
                        MessageType.JOIN_LOBBY,
                        "TEMP_ID",
                        Map.of("pseudo", "Sasha")
                ));

                // 👉 TRANSITION DIRECTE (clé du fix)
                Platform.runLater(() -> {
                    messageLabel.setText("Connecté !");
                    ouvrirLobbyReseau(client);
                });

            } catch (Exception e) {
                Platform.runLater(() ->
                        messageLabel.setText("Erreur : " + e.getMessage()));
            }
        }).start();
    }
    /* ================= NAVIGATION ================= */

    private void ouvrirLobbyReseau(GameClient client) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/codename/idmc/ui/view/networklobby/network_lobby.fxml")
            );

            Parent root = loader.load();

            // injecter le client réseau dans le controller
            NetworkLobbyController controller = loader.getController();
            controller.setClient(client);

            Stage stage = (Stage) lobbiesTable.getScene().getWindow();
            stage.setScene(new Scene(root));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void retourMenu() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/codename/idmc/ui/view/menu/menu.fxml")
            );

            Parent root = loader.load();

            Stage stage = (Stage) lobbiesTable.getScene().getWindow();
            stage.setScene(new Scene(root));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}