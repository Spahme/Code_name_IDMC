package codename.idmc.application.usecases;

import codename.idmc.app.Interfaces.Partie;
import codename.idmc.app.Interfaces.Carte;
import codename.idmc.app.Interfaces.CouleurEquipe;
import codename.idmc.infrastructure.network.PhpGameApiClient;
import codename.idmc.infrastructure.network.dto.CardDto;
import codename.idmc.infrastructure.network.mapper.GameApiMapper;

import java.util.ArrayList;
import java.util.List;

public class CreatePartieUseCase {

    private final PhpGameApiClient api;
    private final GameApiMapper mapper;

    public CreatePartieUseCase() {
        this.api = new PhpGameApiClient();
        this.mapper = new GameApiMapper();
    }

    public Partie execute(
            int langueId,
            int difficultyId,
            int categorieId,
            CouleurEquipe equipeCommencante
    ) throws Exception {

        List<CardDto> dtos = api.searchCards(langueId, difficultyId, categorieId);

        if (dtos == null || dtos.size() < 25) {
            throw new IllegalStateException(
                    "Pas assez de cartes pour créer un plateau (25 requis)"
            );
        }

        List<Carte> cartes = new ArrayList<>();
        int id = 1;

        for (CardDto dto : dtos) {
            cartes.add(mapper.toDomain(dto, id++));
        }

        Partie partie = new Partie();
        partie.initialiserPlateau(cartes, equipeCommencante);

        partie.setDifficulte(String.valueOf(difficultyId));
        partie.setCategorie(String.valueOf(categorieId));

        return partie;
    }
}