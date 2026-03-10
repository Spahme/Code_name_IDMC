package codename.idmc.infrastructure.network.mapper;

import codename.idmc.app.Interfaces.Carte;
import codename.idmc.infrastructure.network.dto.CardDto;

public class GameApiMapper {

    public Carte toDomain(CardDto dto) {

        Carte carte = new Carte();
        carte.setContenu(dto.name);

        return carte;
    }
}