package codename.idmc.infrastructure.network.multiplayer.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class GameServer {

    private final int port;
    private final ExecutorService pool;
    private final LobbyRoom lobbyRoom;

    public GameServer(int port) {
        this.port = port;
        this.pool = Executors.newCachedThreadPool();
        this.lobbyRoom = new LobbyRoom();
    }

    public void start() throws IOException {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Serveur multijoueur démarré sur le port " + port);

            while (true) {
                Socket socket = serverSocket.accept();
                ClientHandler handler = new ClientHandler(socket, lobbyRoom);
                pool.submit(handler);
            }
        }
    }

    public static void main(String[] args) {
        try {
            new GameServer(5555).start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}