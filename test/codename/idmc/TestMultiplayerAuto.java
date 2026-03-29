package codename.idmc.app;

import codename.idmc.infrastructure.network.multiplayer.client.GameClient;
import codename.idmc.infrastructure.network.multiplayer.server.GameServer;
import codename.idmc.infrastructure.network.multiplayer.dto.NetworkMessage;
import codename.idmc.infrastructure.network.multiplayer.protocol.MessageType;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;

import java.util.Map;

public class TestMultiplayerAuto extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {

        // 1. Lancer serveur en thread
        new Thread(() -> {
            try {
                GameServer server = new GameServer(5555);
                server.start();
                System.out.println("Serveur lancé sur 5555");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();

        // petite pause pour laisser le serveur démarrer
        Thread.sleep(1000);

        // 2. Lancer plusieurs clients
        lancerClient("Sasha", "ROUGE", "AGENT");
        lancerClient("Alice", "BLEU", "AGENT");
        lancerClient("Bob", "ROUGE", "MAITRE_ESPION");
        lancerClient("tTom", "BLEU", "MAITRE_ESPION");

    }

    private void lancerClient(String pseudo, String equipe, String role) {
        new Thread(() -> {
            try {
                GameClient client = new GameClient("localhost", 5555);
                client.connect();

                client.setMessageHandler(msg -> {
                    System.out.println("[" + pseudo + "] reçu : " + msg.getType());
                });

                // rejoindre lobby
                client.send(new NetworkMessage(
                        MessageType.JOIN_LOBBY,
                        "temp",
                        Map.of(
                                "pseudo", pseudo,
                                "equipe", equipe,
                                "role", role
                        )
                ));

                // envoyer un message chat test
                Thread.sleep(500);

                client.send(new NetworkMessage(
                        MessageType.CHAT_MESSAGE,
                        "temp",
                        Map.of("message", "Salut de " + pseudo)
                ));

            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    public static void main(String[] args) {
        launch(args);
    }
}