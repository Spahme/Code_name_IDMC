package codename.idmc.infrastructure.network.mapper;

import codename.idmc.app.Interfaces.Carte;
import codename.idmc.app.Interfaces.CouleurCarte;
import codename.idmc.infrastructure.network.dto.CardDto;

public class GameApiMapper {

    public Carte toDomain(CardDto dto, int id) {

        return new Carte(
                id,                 // id local 1..25
                dto.name,           // mot récupéré depuis l'API
                CouleurCarte.NEUTRE // couleur temporaire
        );
    }
}