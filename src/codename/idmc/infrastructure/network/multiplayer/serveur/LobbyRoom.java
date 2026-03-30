package codename.idmc.infrastructure.network.multiplayer.server;

import codename.idmc.infrastructure.network.multiplayer.dto.GameStartDto;
import codename.idmc.infrastructure.network.multiplayer.dto.LobbyPlayerDto;
import codename.idmc.infrastructure.network.multiplayer.dto.LobbyStateDto;
import codename.idmc.infrastructure.network.multiplayer.dto.NetworkMessage;
import codename.idmc.infrastructure.network.multiplayer.protocol.MessageType;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class LobbyRoom {

    private final Map<String, ClientHandler> clients = new ConcurrentHashMap<>();
    private final Map<String, LobbyPlayerDto> players = new ConcurrentHashMap<>();

    private final int maxPlayers;
    private final boolean devMode;

    private volatile boolean started = false;

    private int langueId = 1;
    private int difficultyId = 3;
    private int categorieId = 1;
    private String equipeCommencante = "ROUGE";

    public LobbyRoom(int maxPlayers, boolean devMode) {
        this.maxPlayers = maxPlayers;
        this.devMode = devMode;
        System.out.println("LobbyRoom créé | maxPlayers=" + maxPlayers + " | devMode=" + devMode);
    }

    public void addClient(ClientHandler handler, LobbyPlayerDto player) {
        if (players.size() >= maxPlayers) {
            System.out.println("Lobby plein");
            return;
        }

        clients.put(player.getPlayerId(), handler);
        players.put(player.getPlayerId(), player);

        System.out.println("JOIN : " + player.getPseudo());
        broadcastLobbyState();
    }

    public void removeClient(String playerId) {
        players.remove(playerId);
        clients.remove(playerId);
        broadcastLobbyState();
    }

    public void updatePlayer(String playerId, String equipe, String role, boolean ready) {
        LobbyPlayerDto player = players.get(playerId);
        if (player == null) {
            return;
        }

        player.setEquipe(equipe);
        player.setRole(role);
        player.setReady(ready);

        System.out.println("UPDATE_PLAYER : " + player.getPseudo()
                + " | equipe=" + equipe
                + " | role=" + role
                + " | ready=" + ready);

        broadcastLobbyState();
    }

    public void updateGameSettings(int langueId, int difficultyId, int categorieId, String equipeCommencante) {
        this.langueId = langueId;
        this.difficultyId = difficultyId;
        this.categorieId = categorieId;
        this.equipeCommencante = equipeCommencante;

        System.out.println(
                "Paramètres partie mis à jour : langue=" + langueId
                        + ", difficulté=" + difficultyId
                        + ", catégorie=" + categorieId
                        + ", équipeCommencante=" + equipeCommencante
        );

        broadcastLobbyState();
    }

    public void startGame() {
        System.out.println("Demande de START_GAME reçue");
        System.out.println("devMode runtime = " + devMode);

        if (!canStartGame()) {
            System.out.println("Impossible de lancer la partie");
            return;
        }

        started = true;

        System.out.println("START GAME");

        GameStartDto dto = new GameStartDto(
                new ArrayList<>(players.values()),
                langueId,
                difficultyId,
                categorieId,
                equipeCommencante
        );

        broadcast(new NetworkMessage(
                MessageType.START_GAME,
                "SERVER",
                dto
        ));
    }

    private boolean canStartGame() {
        System.out.println("canStartGame() appelé | devMode=" + devMode + " | players=" + players.size());

        if (devMode) {
            System.out.println("DEV MODE -> bypass règles");
            return true;
        }

        if (players.size() < 4) {
            System.out.println("Refus start : moins de 4 joueurs");
            return false;
        }

        long rouges = players.values().stream()
                .filter(p -> "ROUGE".equals(p.getEquipe()))
                .count();

        long bleus = players.values().stream()
                .filter(p -> "BLEU".equals(p.getEquipe()))
                .count();

        long meRouge = players.values().stream()
                .filter(p -> "ROUGE".equals(p.getEquipe()))
                .filter(p -> "MAITRE_ESPION".equals(p.getRole()))
                .count();

        long meBleu = players.values().stream()
                .filter(p -> "BLEU".equals(p.getEquipe()))
                .filter(p -> "MAITRE_ESPION".equals(p.getRole()))
                .count();

        System.out.println("Stats lobby | rouges=" + rouges + " | bleus=" + bleus
                + " | ME rouge=" + meRouge + " | ME bleu=" + meBleu);

        return rouges >= 2 && bleus >= 2 && meRouge == 1 && meBleu == 1;
    }

    public void broadcast(NetworkMessage msg) {
        for (ClientHandler client : clients.values()) {
            try {
                client.send(msg);
            } catch (IOException e) {
                System.err.println("Erreur envoi client");
            }
        }
    }

    public void broadcastExcept(String excludedPlayerId, NetworkMessage msg) {
        for (Map.Entry<String, ClientHandler> entry : clients.entrySet()) {
            if (entry.getKey().equals(excludedPlayerId)) {
                continue;
            }

            try {
                entry.getValue().send(msg);
            } catch (IOException e) {
                System.err.println("Erreur envoi client (except)");
            }
        }
    }

    public void broadcastLobbyState() {
        LobbyStateDto state = new LobbyStateDto(
                new ArrayList<>(players.values())
        );

        broadcast(new NetworkMessage(
                MessageType.LOBBY_STATE,
                "SERVER",
                state
        ));
    }

    public int getCurrentPlayers() {
        return players.size();
    }

    public boolean isStarted() {
        return started;
    }

    public void addFakePlayers() {
        players.put("F1", new LobbyPlayerDto("F1", "Bot_R_ME", "ROUGE", "MAITRE_ESPION", true, false));
        players.put("F2", new LobbyPlayerDto("F2", "Bot_B_ME", "BLEU", "MAITRE_ESPION", true, false));
        players.put("F3", new LobbyPlayerDto("F3", "Bot_R", "ROUGE", "AGENT", true, false));

        System.out.println("Fake players injectés");
        broadcastLobbyState();
    }
}