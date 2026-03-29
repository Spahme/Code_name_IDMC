package codename.idmc.infrastructure.network.multiplayer.dto;

import java.util.ArrayList;
import java.util.List;

public class LobbyStateDto {

    private List<LobbyPlayerDto> players = new ArrayList<>();

    public LobbyStateDto() {
    }

    public LobbyStateDto(List<LobbyPlayerDto> players) {
        this.players = players;
    }

    public List<LobbyPlayerDto> getPlayers() {
        return players;
    }

    public void setPlayers(List<LobbyPlayerDto> players) {
        this.players = players;
    }
}