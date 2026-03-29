package codename.idmc.infrastructure.network.mapper;

import codename.idmc.app.Interfaces.Carte;
import codename.idmc.app.Interfaces.CouleurCarte;
import codename.idmc.infrastructure.network.dto.CardDto;

public class GameApiMapper {

    public Carte toDomain(CardDto dto) {
        return new Carte(
                dto.getId(),
                dto.getTitle(),
                dto.getDescription(),
                dto.getIdDiff(),
                CouleurCarte.NEUTRE
        );
    }
}