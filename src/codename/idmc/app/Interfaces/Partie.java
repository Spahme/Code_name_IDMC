/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codename.idmc.app.Interfaces;

/**
 *
 * @author guill
 */
public class Partie {
    private int nbJoueur;
    private int tourActuelle;
    private int scoreRouge;
    private int scoreBleu;
    
    private Grille grille;
    private GrilleCarteCle grilleME;

    // --- Getters et Setters ---
    public int getNbJoueur() { return nbJoueur; }
    public void setNbJoueur(int nbJoueur) { this.nbJoueur = nbJoueur; }

    public int getTourActuelle() { return tourActuelle; }
    public void setTourActuelle(int tourActuelle) { this.tourActuelle = tourActuelle; }

    public int getScoreRouge() { return scoreRouge; }
    public void setScoreRouge(int scoreRouge) { this.scoreRouge = scoreRouge; }

    public int getScoreBleu() { return scoreBleu; }
    public void setScoreBleu(int scoreBleu) { this.scoreBleu = scoreBleu; }

    public Grille getGrille() { return grille; }
    public void setGrille(Grille grille) { this.grille = grille; }

    public GrilleCarteCle getGrilleME() { return grilleME; }
    public void setGrilleME(GrilleCarteCle grilleME) { this.grilleME = grilleME; }

    // --- Méthodes ---
    public void demarrerPartie() { }
    public void tourSuivant() { }
}
