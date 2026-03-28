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
public class Indice {
    @Saveable//l'attribue suivante seras dans le fichier de sauvegarde
    private String mot;
    @Saveable//l'attribue suivante seras dans le fichier de sauvegarde
    private int nbMot; 

    // --- Getters et Setters ---
    public String getMot() { return mot; }
    public void setMot(String mot) { this.mot = mot; }

    public int getNbMot() { return nbMot; }
    public void setNbMot(int nbMot) { this.nbMot = nbMot; }
}
