/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codename.idmc.app.Interfaces;

/**
 *
 * @author guill
 */
public class Indice {

    private String mot;
    private int nbMot;
   //Ajout de la classe equipe 
    private Equipe equipe;

    // Constructor
    public Indice(String mot, int nbmot, Equipe equipe) {
        this.mot = mot;
        this.nbMot = nbmot;
        this.equipe = equipe;
    }

    // Getters & Setters
    public String getMot() {
        return mot;
    }

    public void setMot(String mot) {
        this.mot = mot;
    }

    public int getNombre() {
        return nbMot;
    }

    public void setNombre(int nombre) {
        this.nbMot = nombre;
    }

    public Equipe getEquipe() {
        return equipe;
    }

    public void setEquipe(Equipe equipe) {
        this.equipe = equipe;
    }

    // Max guesses = number + 1 bonus guess (0 = unlimited)
    public int getNombreMaxTentatives() {
        return nbMot == 0 ? Integer.MAX_VALUE : nbMot + 1;
    }

    public int getNbMot() {
        return nbMot;
    }

    public void setNbMot(int nbMot) {
        this.nbMot = nbMot;
    }
    
     @Override
    public String toString() {
        return mot + " " + (nbMot == 0 ? "∞" : nbMot);
    }
}

