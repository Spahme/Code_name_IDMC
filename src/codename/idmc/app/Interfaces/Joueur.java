/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codename.idmc.app.Interfaces;

import codename.idmc.infrastructure.persistance.Saveable;

public class Joueur {
    @Saveable//l'attribue suivante seras dans le fichier de sauvegarde
    private String nom;
    @Saveable//l'attribue suivante seras dans le fichier de sauvegarde
    private String equipe;
    @Saveable//l'attribue suivante seras dans le fichier de sauvegarde
    private boolean admin;

    // --- Getters et Setters ---
    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public String getEquipe() { return equipe; }
    public void setEquipe(String equipe) { this.equipe = equipe; }

    public boolean isAdmin() { return admin; }
    public void setAdmin(boolean admin) { this.admin = admin; }

    // --- Méthodes ---
    public void stopPartie(boolean admin) { }
    public void sauvegarder(boolean admin) { }
    public void kickUnJoueur(boolean admin) { }
}