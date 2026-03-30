package codename.idmc.infrastructure.network.multiplayer.discovery;

import codename.idmc.infrastructure.network.multiplayer.dto.LobbyInfoDto;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class LobbyDiscoveryClient {

    private final ObjectMapper mapper = new ObjectMapper();

    public List<LobbyInfoDto> discover(int timeoutMillis) {
        List<LobbyInfoDto> lobbies = new ArrayList<>();
        Set<String> seen = new HashSet<>();

        try (DatagramSocket socket = new DatagramSocket()) {
            socket.setBroadcast(true);
            socket.setSoTimeout(timeoutMillis);

            byte[] requestData = LobbyDiscoveryResponder.DISCOVERY_REQUEST.getBytes(StandardCharsets.UTF_8);

            DatagramPacket packet = new DatagramPacket(
                    requestData,
                    requestData.length,
                    InetAddress.getByName("255.255.255.255"),
                    LobbyDiscoveryResponder.DISCOVERY_PORT
            );

            socket.send(packet);

            long end = System.currentTimeMillis() + timeoutMillis;

            while (System.currentTimeMillis() < end) {
                try {
                    byte[] buf = new byte[4096];
                    DatagramPacket response = new DatagramPacket(buf, buf.length);
                    socket.receive(response);

                    String json = new String(response.getData(), 0, response.getLength(), StandardCharsets.UTF_8);
                    LobbyInfoDto dto = mapper.readValue(json, LobbyInfoDto.class);

                    String key = dto.getHostAddress() + ":" + dto.getTcpPort();
                    if (seen.add(key)) {
                        lobbies.add(dto);
                    }

                } catch (SocketTimeoutException e) {
                    break;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return lobbies;
    }
}