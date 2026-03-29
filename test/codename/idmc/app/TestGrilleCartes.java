package codename.idmc.app;

import codename.idmc.application.usecases.CreatePartieUseCase;
import codename.idmc.app.Interfaces.CouleurEquipe;
import codename.idmc.app.Interfaces.Partie;

public class TestGrilleCartes {

    public static void main(String[] args) {
        try {
            CreatePartieUseCase useCase = new CreatePartieUseCase();

            Partie partie = useCase.execute(1, 3, 1, CouleurEquipe.ROUGE);

            System.out.println("=== GRILLE JOUEUR ===");
            partie.getPlateau().afficher(false);

            System.out.println();
            System.out.println("=== GRILLE MAITRE ESPION ===");
            partie.getPlateau().afficher(true);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}