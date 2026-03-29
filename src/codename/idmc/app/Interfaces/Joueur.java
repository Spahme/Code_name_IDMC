package codename.idmc.app.Interfaces;

import codename.idmc.infrastructure.persistance.Saveable;

public class Joueur {

    @Saveable
    private String nom;

    private Equipe equipe;

    @Saveable
    private boolean admin;

    private boolean estMaitreEspion;

    // Constructeur
    public Joueur(String nom) {
        this.nom = nom;
    }

    // Getters / Setters
    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public Equipe getEquipe() {
        return equipe;
    }

    public void setEquipe(Equipe equipe) {
        this.equipe = equipe;
    }

    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    public boolean isEstMaitreEspion() {
        return estMaitreEspion;
    }

    public void setEstMaitreEspion(boolean estMaitreEspion) {
        this.estMaitreEspion = estMaitreEspion;
    }

    public String getPseudo() {
        return nom;
    }

    // Méthodes
    public void stopPartie() {
        if (!admin) return;
    }

    public void sauvegarder() {
        if (!admin) return;
    }

    public void kickUnJoueur() {
        if (!admin) return;
    }
}