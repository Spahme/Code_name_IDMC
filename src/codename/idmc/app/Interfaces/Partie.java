/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codename.idmc.app.Interfaces;

import codename.idmc.app.Interfaces.Plateau;
import codename.idmc.app.Interfaces.Joueur;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author guill
 */
public class Partie {

    /**
     * Représente une partie de Codenames. Orchestre les équipes, les joueurs,
     * le plateau et le déroulement du jeu.
     */
    private Equipe equipeRouge;
    private Equipe equipeBleu;
    private final List<Joueur> joueurs;
    private Equipe equipeCourante;
    private Plateau plateau;
    private Indice indiceCourant;
    private boolean estEncours;
    private boolean estEnCours;

    // ── Constructeur ──────────────────────────────────────────────────────────
    public Partie() {
        this.equipeRouge = new Equipe("Équipe Rouge", CouleurEquipe.ROUGE, 9);
        this.equipeBleu = new Equipe("Équipe Bleu", CouleurEquipe.BLEU, 8);
        this.joueurs = new ArrayList<>();
        this.equipeCourante = equipeRouge; // rouge always starts
        this.plateau = null;
        this.indiceCourant = null;
        this.estEncours = false;

    }

    // ── Gestion des joueurs ───────────────────────────────────────────────────
    /**
     * Ajoute un joueur à l'équipe choisie. L'équipe est assignée
     * automatiquement via ajouterJoueur().
     *
     * @param joueur
     * @param couleur
     */
    public void choisirEquipe(Joueur joueur, CouleurEquipe couleur) {

        if (couleur == CouleurEquipe.ROUGE) {
            equipeRouge.ajouterJoueur(joueur);
        } else {
            equipeBleu.ajouterJoueur(joueur);
        }
        joueurs.add(joueur);
        System.out.println(joueur.getPseudo() + " a rejoint " + joueur.getEquipe().getNom());
    }

    // ── Initialisation du plateau 
    /**
     * Initialise le plateau avec les cartes disponibles. Doit être appelée
     * avant demarrerPartie().
     */
    public void initialiserPlateau(List<Carte> cartesDisponibles) {
        this.plateau = new Plateau(CouleurEquipe.ROUGE);
        plateau.initialiser(cartesDisponibles);
        System.out.println("Plateau initialisé avec " + cartesDisponibles.size() + " cartes.");
    }

    // ── Démarrag
    /**
     * Démarre la partie si toutes les conditions sont remplies : - Le plateau
     * est initialisé - Chaque équipe a au moins 2 joueurs - Chaque équipe a
     * exactement 1 maître espion
     */
    public void demarrerPartie() {
        if (plateau == null) {
            System.out.println("Erreur : le plateau n'est pas initialisé.");
            return;
        }
        if (equipeRouge.getJoueurs().size() < 2
                || equipeBleu.getJoueurs().size() < 2) {
            System.out.println("Erreur : chaque équipe doit avoir au moins 2 joueurs.");
            return;
        }
        if (!equipeAUnMaitreEspion(equipeRouge)
                || !equipeAUnMaitreEspion(equipeBleu)) {
            System.out.println("Erreur : chaque équipe doit avoir exactement 1 maître espion.");
            return;
        }

        System.out.println("\n╔══════════════════════════════╗");
        System.out.println("║   La partie commence !       ║");
        System.out.println("╚══════════════════════════════╝");
        System.out.println("C'est au tour de : " + equipeCourante.getNom());
        plateau.afficher(false);
    }

    // ── Déroulement du jeu ────────────────────────────────────────────────────
    /**
     * Le maître espion donne un indice pour son équipe.
     */
    public void donnerIndice(String mot, int nombre) {
        if (!estEnCours) {
            System.out.println("La partie n'est pas encore démarrée.");
            return;
        }
        indiceCourant = new Indice(mot, nombre, equipeCourante);
        System.out.println("\nIndice de " + equipeCourante.getNom()
                + " : " + indiceCourant);
    }

    /**
     * Un opératif joue une carte. Retourne true si l'équipe peut continuer à
     * jouer, false sinon.
     */
    public boolean jouerCarte(String contenu) {
        if (!estEnCours) {
            System.out.println("La partie n'est pas encore démarrée.");
            return false;
        }
        if (indiceCourant == null) {
            System.out.println("Le maître espion doit d'abord donner un indice.");
            return false;
        }

        CouleurCarte type = plateau.jouerCarte(contenu);
        if (type == null) {
            return false;
        }

        System.out.println("Carte retournée : " + contenu + " → " + type);

        // Check assassin
        if (type == CouleurCarte.ASSASSIN) {
            System.out.println("L'assassin a été retourné ! "
                    + equipeCourante.getNom() + " perd la partie !");
            estEnCours = false;
            return false;
        }

        // Check if current team's card
        CouleurCarte typeCourant = equipeCourante.getCouleur() == CouleurEquipe.ROUGE
                ? CouleurCarte.ROUGE : CouleurCarte.BLEU;

        if (type == typeCourant) {
            equipeCourante.carteRetournee();
            // Check win condition
            if (equipeCourante.aGagne()) {
                System.out.println(equipeCourante.getNom() + " a gagné la partie !");
                estEnCours = false;
                return false;
            }
            return true; // can keep guessing
        }

        // Neutral or opponent card — turn ends
        if (type != typeCourant) {
            // If opponent's card, update their count
            Equipe equipeAdverse = equipeCourante == equipeRouge ? equipeBleu : equipeRouge;
            CouleurCarte typeAdverse = equipeAdverse.getCouleur() == CouleurEquipe.ROUGE
                    ? CouleurCarte.ROUGE : CouleurCarte.BLEU;
            if (type == typeAdverse) {
                equipeAdverse.carteRetournee();
                // Check if opponent wins
                if (equipeAdverse.aGagne()) {
                    System.out.println(equipeAdverse.getNom() + " a gagné la partie !");
                    estEnCours = false;
                    return false;
                }
            }
        }

        passerTour();
        return false;
    }

    /**
     * Passe le tour à l'équipe suivante.
     */
    public void passerTour() {
        indiceCourant = null;
        equipeCourante = (equipeCourante == equipeRouge) ? equipeBleu : equipeRouge;
        System.out.println("\nC'est maintenant au tour de : " + equipeCourante.getNom());
    }

    // ── Helpers ───────────────────────────────────────────────────────────────
    /**
     * Vérifie qu'une équipe a exactement un maître espion.
     */
    private boolean equipeAUnMaitreEspion(Equipe equipe) {
        int count = 0;
        for (Joueur j : equipe.getJoueurs()) {
            if (j.isEstMaitreEspion()) {
                count++;
            }
        }
        return count == 1;
    }

    // ── Getters ───────────────────────────────────────────────────────────────
    public Equipe getEquipeRouge() {
        return equipeRouge;
    }

    public Equipe getEquipeBleu() {
        return equipeBleu;
    }

    public List<Joueur> getJoueurs() {
        return joueurs;
    }

    public Equipe getEquipeCourante() {
        return equipeCourante;
    }

    public Plateau getPlateau() {
        return plateau;
    }

    public Indice getIndiceCourant() {
        return indiceCourant;
    }

    public boolean isEstEnCours() {
        return estEnCours;
    }

    @Override
    public String toString() {
        return estEnCours
                ? "Partie en cours - Tour : " + equipeCourante.getNom()
                : "Partie non démarrée";
    }

}
