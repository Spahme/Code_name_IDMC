/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author User
 */
package codename.idmc.app;

import java.util.List;

/**
 * Représente un administrateur de la partie.
 * Étend Joueur avec des pouvoirs de gestion supplémentaires.
 */
public class Admin extends Joueur {

    private String motDePasse;

    // Constructeur
    public Admin(int idJoueur, String pseudo, String motDePasse) {
        super(idJoueur, pseudo);
        this.motDePasse = motDePasse;
    }

    // Getters & Setters
    public String getMotDePasse()                { return motDePasse; }
    public void setMotDePasse(String motDePasse) { this.motDePasse = motDePasse; }

    // ── Gestion des joueurs ───────────────────────────────────────────────────

    /**
     * Ajoute un joueur à une équipe.
     */
    public void ajouterJoueur(Partie partie, Joueur joueur, CouleurEquipe couleur) {
        partie.choisirEquipe(joueur, couleur);
        System.out.println("[ADMIN] " + joueur.getPseudo() + " ajouté.");
    }

    // ── Gestion de la partie ──────────────────────────────────────────────────

    /**
     * Initialise le plateau de jeu.
     */
    public void initialiserPlateau(Partie partie, List<Carte> cartes) {
        partie.initialiserPlateau(cartes);
        System.out.println("[ADMIN] Plateau initialisé par " + getPseudo());
    }

    /**
     * Démarre la partie.
     */
    public void demarrerPartie(Partie partie) {
        partie.demarrerPartie();
        System.out.println("[ADMIN] Partie démarrée par " + getPseudo());
    }

    /**
     * Force le passage au tour suivant.
     * @param partie
     */
    public void forcerPasserTour(Partie partie) {
        partie.passerTour();
        System.out.println("[ADMIN] Tour passé par " + getPseudo());
    }

    /**
     * Affiche le plateau complet (vue maître espion).
     * @param partie
     */
    public void afficherPlateauComplet(Partie partie) {
        System.out.println("[ADMIN] Vue complète :");
        partie.getPlateau().afficher(true);
    }

    /**
     * Déclenche la saisie d'indice du Maître Espion.
     * @param partie
     */
    public void gererSaisieIndice(Partie partie) {
        partie.saisirIndice();
    }

    @Override
    public String toString() {
        return "[ADMIN] " + getPseudo() + " - " +
               (getEquipe() != null ? getEquipe().getNom() : "Sans équipe");
    }
}