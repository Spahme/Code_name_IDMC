package codename.idmc.ui.view.lobby;

import codename.idmc.app.Interfaces.CouleurEquipe;

public class LobbyPlayerRow {

    private String pseudo;
    private CouleurEquipe equipe;
    private String role;

    public LobbyPlayerRow(String pseudo, CouleurEquipe equipe, String role) {
        this.pseudo = pseudo;
        this.equipe = equipe;
        this.role = role;
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