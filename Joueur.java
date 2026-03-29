/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codename.idmc.app.Interfaces;

/**
 * Représente un joueur participant à la partie. L'équipe est assignée
 * automatiquement via Partie.choisirEquipe().
 */
public class Joueur {

    private boolean admin;
    private int idJoueur;
    private String pseudo;
    private Equipe equipe;            // assignée automatiquement
    private boolean estMaitreEspion;  // true = Maître Espion, false = Opératif

    // Constructeur — sans équipe, elle sera assignée après
    public Joueur(int idJoueur, String pseudo) {
        this.idJoueur = idJoueur;
        this.pseudo = pseudo;
        this.equipe = null;
        this.estMaitreEspion = false;
    }

    // Getters & Setters
    public int getIdJoueur() {
        return idJoueur;
    }

    public void setIdJoueur(int idJoueur) {
        this.idJoueur = idJoueur;
    }

    public String getPseudo() {
        return pseudo;
    }

    public void setPseudo(String pseudo) {
        this.pseudo = pseudo;
    }

    public Equipe getEquipe() {
        return equipe;
    }

    public void setEquipe(Equipe equipe) {
        this.equipe = equipe;
    }

    public boolean isEstMaitreEspion() {
        return estMaitreEspion;
    }

    public void setEstMaitreEspion(boolean estMaitreEspion) {
        this.estMaitreEspion = estMaitreEspion;
    }

   
    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    // --- Méthodes ---
    public void stopPartie(boolean admin) {
    }

    public void sauvegarder(boolean admin) {
    }

    public void kickUnJoueur(boolean admin) {
    }
    //INdice 
    public boolean peutDonnerIndice() {
    return estMaitreEspion;
}

public boolean peutDeviner() {
    return !estMaitreEspion;
} 

     @Override
    public String toString() {
        String role = estMaitreEspion ? "Maître Espion" : "Opératif";
        String equipeNom = equipe != null ? equipe.getNom() : "Sans équipe";
        return pseudo + " - " + role + " - " + equipeNom;
    }
}
