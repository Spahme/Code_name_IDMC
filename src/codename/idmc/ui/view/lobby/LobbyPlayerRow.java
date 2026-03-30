package codename.idmc.ui.view.lobby;

import codename.idmc.app.Interfaces.CouleurEquipe;

public class LobbyPlayerRow {

    private final String playerId;

    private String pseudo;
    private CouleurEquipe equipe;
    private String role;

    public LobbyPlayerRow(String playerId, String pseudo, CouleurEquipe equipe, String role) {
        this.playerId = playerId;
        this.pseudo = pseudo;
        this.equipe = equipe;
        this.role = role;
    }

    public String getPlayerId() {
        return playerId;
    }

    public String getPseudo() {
        return pseudo;
    }

    public void setPseudo(String pseudo) {
        this.pseudo = pseudo;
    }

    public CouleurEquipe getEquipe() {
        return equipe;
    }

    public void setEquipe(CouleurEquipe equipe) {
        this.equipe = equipe;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}