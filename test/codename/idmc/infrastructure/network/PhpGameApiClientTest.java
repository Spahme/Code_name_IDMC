package codename.idmc.infrastructure.network;

import codename.idmc.infrastructure.network.dto.CardDto;

import java.util.List;
import java.util.UUID;

public class PhpGameApiClientTest {

    public static void main(String[] args) {
        PhpGameApiClient api = new PhpGameApiClient();

        try {
            System.out.println("=== SEARCH ===");

            List<CardDto> cards = api.searchCards(1, 3, 1);

            if (cards == null) {
                System.out.println("Aucune réponse de l'API.");
                return;
            }

            System.out.println("Cartes reçues : " + cards.size());

            int localId = 1;

            for (CardDto card : cards) {
                String newId = UUID.randomUUID().toString();

                System.out.println(
                    "id=" + localId++ +
                    " title=" + card.getTitle()
                );
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}