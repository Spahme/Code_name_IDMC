package codename.idmc.app.Interfaces;

import codename.idmc.infrastructure.persistance.Saveable;

public class Indice {

    @Saveable
    private String mot;

    @Saveable
    private int nbMot;

    private Equipe equipe;

    public Indice(String mot, int nbMot, Equipe equipe) {
        this.mot = mot;
        this.nbMot = nbMot;
        this.equipe = equipe;
    }

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