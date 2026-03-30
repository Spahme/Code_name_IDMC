package codename.idmc.infrastructure.network.multiplayer.discovery;

import codename.idmc.infrastructure.network.multiplayer.dto.LobbyInfoDto;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.charset.StandardCharsets;
import java.util.function.Supplier;

public class LobbyDiscoveryResponder implements Runnable {

    public static final int DISCOVERY_PORT = 5556;
    public static final String DISCOVERY_REQUEST = "DISCOVER_CODENAME_IDMC_LOBBY";

    private final Supplier<LobbyInfoDto> lobbyInfoSupplier;
    private final ObjectMapper mapper = new ObjectMapper();
    private volatile boolean running = true;

    public LobbyDiscoveryResponder(Supplier<LobbyInfoDto> lobbyInfoSupplier) {
        this.lobbyInfoSupplier = lobbyInfoSupplier;
    }

    @Override
    public void run() {
        try (DatagramSocket socket = new DatagramSocket(DISCOVERY_PORT)) {
            socket.setBroadcast(true);

            byte[] buffer = new byte[1024];

            while (running) {
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                socket.receive(packet);

                String request = new String(packet.getData(), 0, packet.getLength(), StandardCharsets.UTF_8);

                if (!DISCOVERY_REQUEST.equals(request)) {
                    continue;
                }

                LobbyInfoDto lobbyInfo = lobbyInfoSupplier.get();
                String json = mapper.writeValueAsString(lobbyInfo);
                byte[] responseBytes = json.getBytes(StandardCharsets.UTF_8);

                InetAddress address = packet.getAddress();
                int port = packet.getPort();

                DatagramPacket response = new DatagramPacket(responseBytes, responseBytes.length, address, port);
                socket.send(response);
            }
        } catch (Exception e) {
            if (running) {
                e.printStackTrace();
            }
        }
    }

    public void stop() {
        running = false;
    }
}