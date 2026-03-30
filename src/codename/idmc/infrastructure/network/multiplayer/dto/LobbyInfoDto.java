package codename.idmc.infrastructure.network.multiplayer.dto;

public class LobbyInfoDto {

    private String lobbyName;
    private String hostPseudo;
    private String hostAddress;
    private int tcpPort;
    private int currentPlayers;
    private int maxPlayers;
    private boolean started;

    public LobbyInfoDto() {
    }

    public LobbyInfoDto(String lobbyName, String hostPseudo, String hostAddress,
                        int tcpPort, int currentPlayers, int maxPlayers, boolean started) {
        this.lobbyName = lobbyName;
        this.hostPseudo = hostPseudo;
        this.hostAddress = hostAddress;
        this.tcpPort = tcpPort;
        this.currentPlayers = currentPlayers;
        this.maxPlayers = maxPlayers;
        this.started = started;
    }

    public String getLobbyName() {
        return lobbyName;
    }

    public void setLobbyName(String lobbyName) {
        this.lobbyName = lobbyName;
    }

    public String getHostPseudo() {
        return hostPseudo;
    }

    public void setHostPseudo(String hostPseudo) {
        this.hostPseudo = hostPseudo;
    }

    public String getHostAddress() {
        return hostAddress;
    }

    public void setHostAddress(String hostAddress) {
        this.hostAddress = hostAddress;
    }

    public int getTcpPort() {
        return tcpPort;
    }

    public void setTcpPort(int tcpPort) {
        this.tcpPort = tcpPort;
    }

    public int getCurrentPlayers() {
        return currentPlayers;
    }

    public void setCurrentPlayers(int currentPlayers) {
        this.currentPlayers = currentPlayers;
    }

    public int getMaxPlayers() {
        return maxPlayers;
    }

    public void setMaxPlayers(int maxPlayers) {
        this.maxPlayers = maxPlayers;
    }

    public boolean isStarted() {
        return started;
    }

    public void setStarted(boolean started) {
        this.started = started;
    }
}