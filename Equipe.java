/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codename.idmc.app.Interfaces;

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
    private CouleurEquipe couleur;  //en realité type de la carte si qlq'un peut changer le nom des variables
    private int cartesRestantes;
    private List<Joueur> joueurs;

    // Constructor
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
    
      /**
     * Retourne le Maître Espion de l'équipe, ou null si non assigné.
     */
    public Joueur getMaitreEspion() {
        for (Joueur j : joueurs)
            if (j.isEstMaitreEspion())
                return j;
        return null;
    }
        /**
     * Un joueur réclame le rôle de Maître Espion.
     * Premier arrivé, premier servi — un seul par équipe.
     */
    public boolean reclamerMaitreEspion(Joueur joueur) {
        // check if role already taken
        if (getMaitreEspion() != null) {
            System.out.println("Le rôle de Maître Espion est déjà pris par " +
                               getMaitreEspion().getPseudo());
            return false;
        }
        // check if player belongs to this team
        if (!joueurs.contains(joueur)) {
            System.out.println("Erreur : " + joueur.getPseudo() +
                               " n'appartient pas à " + nom);
            return false;
        }
        joueur.setEstMaitreEspion(true);
        System.out.println(joueur.getPseudo() +
                           " est maintenant Maître Espion de " + nom);
        return true;
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
