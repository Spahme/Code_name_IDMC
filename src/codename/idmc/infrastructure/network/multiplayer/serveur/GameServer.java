package codename.idmc.infrastructure.network.multiplayer.server;

import codename.idmc.infrastructure.network.multiplayer.discovery.LobbyDiscoveryResponder;
import codename.idmc.infrastructure.network.multiplayer.dto.LobbyInfoDto;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class GameServer {

    private final int port;
    private final ExecutorService pool;
    private final LobbyRoom lobbyRoom;

    private final String lobbyName;
    private final String hostPseudo;
    private final int maxPlayers;

    private static final boolean DEV_MODE = true;

    public GameServer(int port, String lobbyName, String hostPseudo, int maxPlayers) {
        this.port = port;
        this.lobbyName = lobbyName;
        this.hostPseudo = hostPseudo;
        this.maxPlayers = maxPlayers;
        this.pool = Executors.newCachedThreadPool();

        this.lobbyRoom = new LobbyRoom(maxPlayers, DEV_MODE);
        System.out.println("GameServer créé | DEV_MODE=" + DEV_MODE);
    }

    public void start() throws IOException {
        LobbyDiscoveryResponder responder = new LobbyDiscoveryResponder(this::buildLobbyInfo);
        Thread discoveryThread = new Thread(responder, "lobby-discovery-responder");
        discoveryThread.setDaemon(true);
        discoveryThread.start();

        if (DEV_MODE) {
            System.out.println("DEV MODE : injection joueurs fake");
            lobbyRoom.addFakePlayers();
        }

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("=================================");
            System.out.println("Serveur lobby démarré");
            System.out.println("Port : " + port);
            System.out.println("Lobby : " + lobbyName);
            System.out.println("Hôte : " + hostPseudo);
            System.out.println("DEV_MODE : " + DEV_MODE);
            System.out.println("=================================");

            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("Connexion entrante : " + socket.getInetAddress());

                ClientHandler handler = new ClientHandler(socket, lobbyRoom);
                pool.submit(handler);
            }
        }
    }

    private LobbyInfoDto buildLobbyInfo() {
        try {
            return new LobbyInfoDto(
                    lobbyName,
                    hostPseudo,
                    InetAddress.getLocalHost().getHostAddress(),
                    port,
                    lobbyRoom.getCurrentPlayers(),
                    maxPlayers,
                    lobbyRoom.isStarted()
            );
        } catch (Exception e) {
            return new LobbyInfoDto(
                    lobbyName,
                    hostPseudo,
                    "127.0.0.1",
                    port,
                    lobbyRoom.getCurrentPlayers(),
                    maxPlayers,
                    lobbyRoom.isStarted()
            );
        }
    }

    public static void main(String[] args) {
        try {
            new GameServer(
                    5555,
                    "Lobby test",
                    "Sasha",
                    8
            ).start();
        } catch (IOException e) {
            System.err.println("Erreur serveur : " + e.getMessage());
            e.printStackTrace();
        }
    }
}