package codename.idmc.infrastructure.network.multiplayer.client;

import codename.idmc.infrastructure.network.multiplayer.dto.NetworkMessage;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.application.Platform;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.function.Consumer;

public class GameClient {

    private final String host;
    private final int port;
    private final ObjectMapper mapper;

    private Socket socket;
    private BufferedReader reader;
    private PrintWriter writer;
    private Thread listenerThread;

    private Consumer<NetworkMessage> messageListener;

    public GameClient(String host, int port) {
        this.host = host;
        this.port = port;
        this.mapper = new ObjectMapper();
    }

    public void connect() throws IOException {
        socket = new Socket(host, port);
        reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);

        listenerThread = new Thread(this::listenLoop, "game-client-listener");
        listenerThread.setDaemon(true);
        listenerThread.start();
    }

    public void setMessageListener(Consumer<NetworkMessage> messageListener) {
        this.messageListener = messageListener;
    }

    public synchronized void send(NetworkMessage message) throws IOException {
        String json = mapper.writeValueAsString(message);
        writer.println(json);
    }

    private void listenLoop() {
        try {
            String line;
            while ((line = reader.readLine()) != null) {
                NetworkMessage message = mapper.readValue(line, NetworkMessage.class);

                if (messageListener != null) {
                    Platform.runLater(() -> messageListener.accept(message));
                }
            }
        } catch (Exception e) {
            Platform.runLater(() ->
                    System.err.println("Connexion au serveur perdue : " + e.getMessage())
            );
        }
    }

    public void disconnect() {
        try {
            if (socket != null) {
                socket.close();
            }
        } catch (IOException ignored) {
        }
    }

    public boolean isConnected() {
        return socket != null && socket.isConnected() && !socket.isClosed();
    }
}