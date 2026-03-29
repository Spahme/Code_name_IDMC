package codename.idmc.infrastructure.network.multiplayer.server;

import codename.idmc.infrastructure.network.multiplayer.dto.LobbyPlayerDto;
import codename.idmc.infrastructure.network.multiplayer.dto.LobbyStateDto;
import codename.idmc.infrastructure.network.multiplayer.dto.NetworkMessage;
import codename.idmc.infrastructure.network.multiplayer.protocol.MessageType;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class LobbyRoom {

    private final Map<String, ClientHandler> clients = new ConcurrentHashMap<>();
    private final Map<String, LobbyPlayerDto> players = new ConcurrentHashMap<>();

    public void addClient(ClientHandler clientHandler, LobbyPlayerDto player) {
        clients.put(player.getPlayerId(), clientHandler);
        players.put(player.getPlayerId(), player);
        broadcastLobbyState();
    }

    public void removeClient(String playerId) {
        clients.remove(playerId);
        players.remove(playerId);
        broadcastLobbyState();
    }

    public void broadcast(NetworkMessage message) {
        for (ClientHandler client : clients.values()) {
            try {
                client.send(message);
            } catch (IOException e) {
                System.err.println("Erreur d'envoi vers client : " + e.getMessage());
            }
        }
    }

    public void broadcastExcept(String excludedPlayerId, NetworkMessage message) {
        for (Map.Entry<String, ClientHandler> entry : clients.entrySet()) {
            if (entry.getKey().equals(excludedPlayerId)) {
                continue;
            }
            try {
                entry.getValue().send(message);
            } catch (IOException e) {
                System.err.println("Erreur d'envoi vers client : " + e.getMessage());
            }
        }
    }

    public void broadcastLobbyState() {
        List<LobbyPlayerDto> snapshot = new ArrayList<>(players.values());
        LobbyStateDto state = new LobbyStateDto(snapshot);
        NetworkMessage msg = new NetworkMessage(MessageType.LOBBY_STATE, "SERVER", state);
        broadcast(msg);
    }
}