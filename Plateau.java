/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codename.idmc.app.Interfaces;

/**
 *
 * @author guill
 */
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Représente le plateau de jeu Codenames. Utilise Grille pour la structure 2D
 * et ajoute la logique de jeu : distribution des cartes, vérification des
 * conditions de victoire.
 */
public class Plateau {

    private final Grille grille;
    private final CouleurEquipe equipeCommencante; // team that starts gets 9 cards

    // Constructeur
    public Plateau(CouleurEquipe equipeCommencante) {
        this.equipeCommencante = equipeCommencante;
        this.grille = new Grille();
    }

    // initialisaition
    /**
     * Initialise et mélange les cartes sur le plateau. Distribue les types
     */
    public void initialiser(List<Carte> cartesDisponibles) {
        if (cartesDisponibles.size() < Grille.SIZE) {
            throw new IllegalArgumentException(
                    "Pas assez de cartes : besoin de " + Grille.SIZE
                    + ", reçu " + cartesDisponibles.size()
            );
        }

        // 25 cartes
        List<Carte> cartes = new ArrayList<>(cartesDisponibles);
        Collections.shuffle(cartes);
        cartes = cartes.subList(0, Grille.SIZE);

        // 2. Build the list of types to assign
        List<CouleurCarte> types = new ArrayList<>();

        // Starting team gets 9, other gets 8
        CouleurCarte typeCommencant = equipeCommencante == CouleurEquipe.ROUGE
                ? CouleurCarte.ROUGE : CouleurCarte.BLEU;
        CouleurCarte typeAutre = equipeCommencante == CouleurEquipe.ROUGE
                ? CouleurCarte.BLEU : CouleurCarte.ROUGE;

        for (int i = 0; i < Grille.REDCARTESCOUNT; i++) {
            types.add(typeCommencant);
        }
        for (int i = 0; i < Grille.BLUECARTESCOUNT; i++) {
            types.add(typeAutre);
        }
        for (int i = 0; i < Grille.NEUTRALCARTESCOUNT; i++) {
            types.add(CouleurCarte.NEUTRE);
        }
        for (int i = 0; i < Grille.ASSASSINCARTESCOUNT; i++) {
            types.add(CouleurCarte.ASSASSIN);
        }

        // Melange des cartes via shuffle
        Collections.shuffle(types);
        for (int i = 0; i < Grille.SIZE; i++) {
            cartes.get(i).setType(types.get(i));
        }

        // placer les cartes
        int index = 0;
        for (int r = 0; r < Grille.ROWS; r++) {
            for (int c = 0; c < Grille.COLS; c++) {
                grille.setCard(r, c, cartes.get(index++));
            }
        }
    }

    // EN JEU 
    /**
     * Révèle une carte par son contenu et retourne son type. Retourne null si
     * la carte n'existe pas.
     */
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

    // Condition de vctoire
    /**
     * Vérifie si une équipe a gagné (plus aucune carte restante).
     */
    public boolean aGagne(CouleurEquipe equipe) {
        CouleurCarte type = equipe == CouleurEquipe.ROUGE
                ? CouleurCarte.ROUGE : CouleurCarte.BLEU;
        return grille.countRemaining(type) == 0;
    }

    /**
     * Vérifie si la carte assassin a été retournée.
     */
    public boolean assassinRetourne() {
        for (Carte c : grille.getAllCards()) {
            if (c.getType() == CouleurCarte.ASSASSIN && c.isRetournee()) {
                return true;
            }
        }
        return false;
    }

    // ── Getters ───────────────────────────────────────────────────────────────
    public Grille getGrille() {
        return grille;
    }

    public CouleurEquipe getEquipeCommencante() {
        return equipeCommencante;
    }

    /**
     * Retourne les cartes restantes d'un type donné.
     */
    public int getCartesRestantes(CouleurCarte type) {
        return grille.countRemaining(type);
    }

    // ── Affichage ─────────────────────────────────────────────────────────────
    /**
     * Affiche le plateau.
     *
     * @param vueMaitreEspion si true, affiche les types cachés
     */
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
