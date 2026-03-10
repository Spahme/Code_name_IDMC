package codename.idmc.infrastructure.network;

import codename.idmc.infrastructure.network.dto.CardDto;

import java.util.List;
import java.util.UUID;

public class PhpGameApiClientTest {

    public static void main(String[] args) {

        PhpGameApiClient api = new PhpGameApiClient();

        try {

            System.out.println("=== INDEX ===");
            System.out.println(api.getIndex());

            System.out.println("\n=== DIFFICULTIES ===");
            System.out.println(api.getDifficulties());

            System.out.println("\n=== SEARCH ===");

            List<CardDto> cards = api.searchCards(
                    1,
                    2,
                    1
            );

            System.out.println("Cartes reçues : " + cards.size());

            int localId = 1;

            for (CardDto card : cards) {

                String newId = UUID.randomUUID().toString();

                System.out.println(
                        "id=" + localId++ +
                        " uuid=" + newId +
                        " name=" + card.name +
                        " diff=" + card.diffId
                );
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}