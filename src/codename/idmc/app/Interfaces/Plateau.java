package codename.idmc.app.Interfaces;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Plateau {

    private final Grille grille;
    private final CouleurEquipe equipeCommencante;

    public Plateau(CouleurEquipe equipeCommencante) {
        this.equipeCommencante = equipeCommencante;
        this.grille = new Grille();
    }

    public void initialiser(List<Carte> cartesDisponibles) {
        if (cartesDisponibles.size() < Grille.SIZE) {
            throw new IllegalArgumentException(
                    "Pas assez de cartes : besoin de " + Grille.SIZE
                            + ", reçu " + cartesDisponibles.size()
            );
        }

        List<Carte> cartes = new ArrayList<>(cartesDisponibles);
        Collections.shuffle(cartes);
        cartes = cartes.subList(0, Grille.SIZE);

        List<CouleurCarte> types = new ArrayList<>();

        CouleurCarte typeCommencant = (equipeCommencante == CouleurEquipe.ROUGE)
                ? CouleurCarte.ROUGE
                : CouleurCarte.BLEU;

        CouleurCarte typeAutre = (equipeCommencante == CouleurEquipe.ROUGE)
                ? CouleurCarte.BLEU
                : CouleurCarte.ROUGE;

        for (int i = 0; i < 9; i++) {
            types.add(typeCommencant);
        }

        for (int i = 0; i < 8; i++) {
            types.add(typeAutre);
        }

        for (int i = 0; i < 7; i++) {
            types.add(CouleurCarte.NEUTRE);
        }

        types.add(CouleurCarte.ASSASSIN);

        Collections.shuffle(types);

        for (int i = 0; i < Grille.SIZE; i++) {
            cartes.get(i).setType(types.get(i));
        }

        int index = 0;
        for (int r = 0; r < Grille.ROWS; r++) {
            for (int c = 0; c < Grille.COLS; c++) {
                grille.setCard(r, c, cartes.get(index++));
            }
        }
    }

    public CouleurCarte jouerCarte(String contenu) {
        Carte carte = grille.findByWord(contenu);

        if (carte == null) {
            System.out.println("Carte introuvable : " + contenu);
            return null;
        }

        if (carte.isRetournee()) {
            System.out.println("Cette carte est déjà retournée.");
            return null;
        }

        carte.retourner();
        return carte.getType();
    }

    public boolean aGagne(CouleurEquipe equipe) {
        CouleurCarte type = (equipe == CouleurEquipe.ROUGE)
                ? CouleurCarte.ROUGE
                : CouleurCarte.BLEU;

        return grille.countRemaining(type) == 0;
    }

    public boolean assassinRetourne() {
        for (Carte c : grille.getAllCards()) {
            if (c.getType() == CouleurCarte.ASSASSIN && c.isRetournee()) {
                return true;
            }
        }
        return false;
    }

    public Grille getGrille() {
        return grille;
    }

    public CouleurEquipe getEquipeCommencante() {
        return equipeCommencante;
    }

    public int getCartesRestantes(CouleurCarte type) {
        return grille.countRemaining(type);
    }

    public Carte getCartePosition(int ligne, int colonne) {
        return grille.getCard(ligne, colonne);
    }

    public void afficher(boolean vueMaitreEspion) {
        System.out.println("\n=== PLATEAU ===");
        for (int r = 0; r < Grille.ROWS; r++) {
            for (int c = 0; c < Grille.COLS; c++) {
                Carte carte = grille.getCard(r, c);
                if (carte.isRetournee()) {
                    System.out.printf("%-15s [%-8s] | ", carte.getContenu(), carte.getType());
                } else if (vueMaitreEspion) {
                    System.out.printf("%-15s [%-8s] | ", carte.getContenu(), carte.getType());
                } else {
                    System.out.printf("%-15s [???????] | ", carte.getContenu());
                }
            }
            System.out.println();
        }
    }
}