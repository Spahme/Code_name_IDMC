package codename.idmc.infrastructure.network.multiplayer.server;

import codename.idmc.infrastructure.network.multiplayer.dto.ChatMessageDto;
import codename.idmc.infrastructure.network.multiplayer.dto.CursorPositionDto;
import codename.idmc.infrastructure.network.multiplayer.dto.LobbyPlayerDto;
import codename.idmc.infrastructure.network.multiplayer.dto.NetworkMessage;
import codename.idmc.infrastructure.network.multiplayer.protocol.MessageType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.UUID;

public class ClientHandler implements Runnable {

    private final Socket socket;
    private final LobbyRoom lobbyRoom;
    private final ObjectMapper mapper;

    private BufferedReader reader;
    private PrintWriter writer;

    private String playerId;
    private String pseudo = "Invité";

    public ClientHandler(Socket socket, LobbyRoom lobbyRoom) {
        this.socket = socket;
        this.lobbyRoom = lobbyRoom;
        this.mapper = new ObjectMapper();
    }

    @Override
    public void run() {
        try {
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);

            String line;
            while ((line = reader.readLine()) != null) {
                JsonNode root = mapper.readTree(line);
                MessageType type = MessageType.valueOf(root.get("type").asText());
                JsonNode payload = root.get("payload");

                switch (type) {
                    case JOIN_LOBBY -> handleJoinLobby(payload);
                    case LEAVE_LOBBY -> handleLeaveLobby();
                    case CHAT_MESSAGE -> handleChatMessage(payload);
                    case CURSOR_MOVED -> handleCursorMoved(payload);
                    case UPDATE_PLAYER -> handleUpdatePlayer(payload);
                    case UPDATE_GAME_SETTINGS -> handleUpdateGameSettings(payload);
                    case LOBBY_STATE -> handleLobbyStateRequest();
                    case START_GAME -> handleStartGame();
                    default -> send(new NetworkMessage(
                            MessageType.ERROR,
                            "SERVER",
                            "Type non géré"
                    ));
                }
            }

        } catch (Exception e) {
            System.err.println("Client déconnecté : " + e.getMessage());
        } finally {
            cleanup();
        }
    }

    private void handleJoinLobby(JsonNode payload) {
        this.playerId = UUID.randomUUID().toString();
        this.pseudo = payload.has("pseudo")
                ? payload.get("pseudo").asText()
                : "Invité";

        LobbyPlayerDto player = new LobbyPlayerDto(
                playerId,
                pseudo,
                "ROUGE",
                "AGENT",
                false,
                lobbyRoom.getCurrentPlayers() == 0
        );

        lobbyRoom.addClient(this, player);
    }

    private void handleUpdatePlayer(JsonNode payload) {
        String equipe = payload.get("equipe").asText();
        String role = payload.get("role").asText();
        boolean ready = payload.get("ready").asBoolean();

        lobbyRoom.updatePlayer(playerId, equipe, role, ready);
    }

    private void handleUpdateGameSettings(JsonNode payload) {
        int langueId = payload.get("langueId").asInt();
        int difficultyId = payload.get("difficultyId").asInt();
        int categorieId = payload.get("categorieId").asInt();
        String equipeCommencante = payload.get("equipeCommencante").asText();

        lobbyRoom.updateGameSettings(
                langueId,
                difficultyId,
                categorieId,
                equipeCommencante
        );
    }

    private void handleChatMessage(JsonNode payload) throws IOException {
        ChatMessageDto chat = mapper.treeToValue(payload, ChatMessageDto.class);

        NetworkMessage msg = new NetworkMessage(
                MessageType.CHAT_MESSAGE,
                playerId,
                chat
        );

        lobbyRoom.broadcast(msg);
    }

    private void handleCursorMoved(JsonNode payload) throws IOException {
        CursorPositionDto cursor = mapper.treeToValue(payload, CursorPositionDto.class);

        NetworkMessage msg = new NetworkMessage(
                MessageType.CURSOR_MOVED,
                playerId,
                cursor
        );

        lobbyRoom.broadcastExcept(playerId, msg);
    }

    private void handleLobbyStateRequest() {
        lobbyRoom.broadcastLobbyState();
    }

    private void handleStartGame() {
        lobbyRoom.startGame();
    }

    private void handleLeaveLobby() {
        if (playerId != null) {
            lobbyRoom.removeClient(playerId);
        }
    }

    public void send(NetworkMessage message) throws IOException {
        writer.println(mapper.writeValueAsString(message));
    }

    private void cleanup() {
        try {
            if (playerId != null) {
                lobbyRoom.removeClient(playerId);
            }
            if (socket != null) {
                socket.close();
            }
        } catch (IOException ignored) {
        }
    }
}