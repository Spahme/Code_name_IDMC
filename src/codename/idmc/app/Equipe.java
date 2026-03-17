/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codename.idmc.app;

import codename.idmc.app.Interfaces.CouleurEquipe;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author User
 */
/**
 * Représente une équipe de joueurs dans la partie. Chaque équipe possède une
 * couleur, un ensemble de joueurs et un nombre de cartes restantes à découvrir.
 */
public class Equipe {

    private String nom;
    private CouleurEquipe couleur;
    private int cartesRestantes;
    private List<Joueur> joueurs;

    // Constructeur
    public Equipe(String nom, CouleurEquipe couleur, int cartesRestantes) {
        this.nom = nom;
        this.couleur = couleur;
        this.cartesRestantes = cartesRestantes;
        this.joueurs = new ArrayList<>();
    }

    // Getters & Setters
    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public CouleurEquipe getCouleur() {
        return couleur;
    }

    public void setCouleur(CouleurEquipe couleur) {
        this.couleur = couleur;
    }

    public int getCartesRestantes() {
        return cartesRestantes;
    }

    public void setCartesRestantes(int cartesRestantes) {
        this.cartesRestantes = cartesRestantes;
    }

    public List<Joueur> getJoueurs() {
        return joueurs;
    }

    public void setJoueurs(List<Joueur> joueurs) {
        this.joueurs = joueurs;
    }

    // Ajoute un joueur et lui assigne automatiquement cette équipe
    public void ajouterJoueur(Joueur joueur) {
        joueur.setEquipe(this);
        joueurs.add(joueur);
    }

    // Décrémente le nombre de cartes restantes
    public void carteRetournee() {
        if (cartesRestantes > 0) {
            cartesRestantes--;
        }
    }

    // Vérifie si l'équipe a gagné
    public boolean aGagne() {
        return cartesRestantes == 0;
    }

    @Override
    public String toString() {
        return nom + " (" + couleur + ") - Cartes restantes: " + cartesRestantes;
    }
}
