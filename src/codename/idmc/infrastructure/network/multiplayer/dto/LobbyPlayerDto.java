package codename.idmc.infrastructure.network.multiplayer.dto;

public class LobbyPlayerDto {

    private String playerId;
    private String pseudo;
    private String equipe;
    private String role;
    private boolean ready;
    private boolean host;

    public LobbyPlayerDto() {
    }

    public LobbyPlayerDto(String playerId, String pseudo, String equipe, String role, boolean ready, boolean host) {
        this.playerId = playerId;
        this.pseudo = pseudo;
        this.equipe = equipe;
        this.role = role;
        this.ready = ready;
        this.host = host;
    }

    public String getPlayerId() {
        return playerId;
    }

    public void setPlayerId(String playerId) {
        this.playerId = playerId;
    }

    public String getPseudo() {
        return pseudo;
    }

    public void setPseudo(String pseudo) {
        this.pseudo = pseudo;
    }

    public String getEquipe() {
        return equipe;
    }

    public void setEquipe(String equipe) {
        this.equipe = equipe;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public boolean isReady() {
        return ready;
    }

    public void setReady(boolean ready) {
        this.ready = ready;
    }

    public boolean isHost() {
        return host;
    }

    public void setHost(boolean host) {
        this.host = host;
    }
}