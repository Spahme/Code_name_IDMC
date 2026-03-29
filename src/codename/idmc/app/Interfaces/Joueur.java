/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codename.idmc.app.Interfaces;

public class Joueur {
    private String nom;
    private String equipe;
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

    public String getPseudo() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}