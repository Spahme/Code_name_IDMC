package codename.idmc.infrastructure.network.multiplayer.dto;

import java.util.List;

public class GameStartDto {

    private List<LobbyPlayerDto> players;
    private int langueId;
    private int difficultyId;
    private int categorieId;
    private String equipeCommencante;

    public GameStartDto() {
    }

    public GameStartDto(
            List<LobbyPlayerDto> players,
            int langueId,
            int difficultyId,
            int categorieId,
            String equipeCommencante
    ) {
        this.players = players;
        this.langueId = langueId;
        this.difficultyId = difficultyId;
        this.categorieId = categorieId;
        this.equipeCommencante = equipeCommencante;
    }

    public List<LobbyPlayerDto> getPlayers() {
        return players;
    }

    public void setPlayers(List<LobbyPlayerDto> players) {
        this.players = players;
    }

    public int getLangueId() {
        return langueId;
    }

    public void setLangueId(int langueId) {
        this.langueId = langueId;
    }

    public int getDifficultyId() {
        return difficultyId;
    }

    public void setDifficultyId(int difficultyId) {
        this.difficultyId = difficultyId;
    }

    public int getCategorieId() {
        return categorieId;
    }

    public void setCategorieId(int categorieId) {
        this.categorieId = categorieId;
    }

    public String getEquipeCommencante() {
        return equipeCommencante;
    }

    public void setEquipeCommencante(String equipeCommencante) {
        this.equipeCommencante = equipeCommencante;
    }
}