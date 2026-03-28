/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codename.idmc.app.Interfaces;

import codename.idmc.infrastructure.persistance.Saveable;

/**
 *
 * @author guill
 */
public class Partie {
    @Saveable//l'attribue suivante seras dans le fichier de sauvegarde
    private int nbJoueur;
    @Saveable//l'attribue suivante seras dans le fichier de sauvegarde
    private int tourActuelle;
    @Saveable//l'attribue suivante seras dans le fichier de sauvegarde
    private int scoreRouge;
    @Saveable//l'attribue suivante seras dans le fichier de sauvegarde
    private int scoreBleu;
    @Saveable//l'attribue suivante seras dans le fichier de sauvegarde
    private Grille grille;
    @Saveable//l'attribue suivante seras dans le fichier de sauvegarde
    private GrilleCarteCle grilleME;
    @Saveable
    private String difficulte;

    @Saveable
    private String categorie;

    @Saveable
    private long tempsDeJeuEnSecondes; // on stocke en secondes, plus propre

    // --- Getters et Setters ---
    public int getNbJoueur() { return nbJoueur; }
    public void setNbJoueur(int nbJoueur) { this.nbJoueur = nbJoueur; }

    public int getTourActuelle() { return tourActuelle; }
    public void setTourActuelle(int tourActuelle) { this.tourActuelle = tourActuelle; }

    public int getScoreRouge() { return scoreRouge; }
    public void setScoreRouge(int scoreRouge) { this.scoreRouge = scoreRouge; }

    public int getScoreBleu() { return scoreBleu; }
    public void setScoreBleu(int scoreBleu) { this.scoreBleu = scoreBleu; }
    public String getDifficulte() { return difficulte; }
    public void setDifficulte(String difficulte) { this.difficulte = difficulte; }

    public String getCategorie() { return categorie; }
    public void setCategorie(String categorie) { this.categorie = categorie; }

    public long getTempsDeJeuEnSecondes() { return tempsDeJeuEnSecondes; }
    public void setTempsDeJeuEnSecondes(long tempsDeJeuEnSecondes) { this.tempsDeJeuEnSecondes = tempsDeJeuEnSecondes; }

    // Retourne le temps formaté "HH:MM:SS" pour l'affichage dans la TableView
    public String getTempsDeJeuFormate() {
    long heures  = tempsDeJeuEnSecondes / 3600;
    long minutes = (tempsDeJeuEnSecondes % 3600) / 60;
    long secondes = tempsDeJeuEnSecondes % 60;
    return String.format("%02d:%02d:%02d", heures, minutes, secondes);
}
    public Grille getGrille() { return grille; }
    public void setGrille(Grille grille) { this.grille = grille; }

    public GrilleCarteCle getGrilleME() { return grilleME; }
    public void setGrilleME(GrilleCarteCle grilleME) { this.grilleME = grilleME; }

    // --- Méthodes ---
    public void demarrerPartie() { }
    public void tourSuivant() { }
}
