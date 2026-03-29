package codename.idmc.infrastructure.network.multiplayer.dto;

import codename.idmc.infrastructure.network.multiplayer.protocol.MessageType;

public class NetworkMessage {

    private MessageType type;
    private String playerId;
    private Object payload;

    public NetworkMessage() {
    }

    public NetworkMessage(MessageType type, String playerId, Object payload) {
        this.type = type;
        this.playerId = playerId;
        this.payload = payload;
    }

    public MessageType getType() {
        return type;
    }

    public void setType(MessageType type) {
        this.type = type;
    }

    public String getPlayerId() {
        return playerId;
    }

    public void setPlayerId(String playerId) {
        this.playerId = playerId;
    }

    public Object getPayload() {
        return payload;
    }

    public void setPayload(Object payload) {
        this.payload = payload;
    }
}