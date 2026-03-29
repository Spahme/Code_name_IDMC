/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codename.idmc.app.Interfaces;

import codename.idmc.infrastructure.persistance.Saveable;// systhème de sauvegarde

public class Carte {
    @Saveable//l'attribue suivante seras dans le fichier de sauvegarde
    private int idCarte;
    @Saveable//l'attribue suivante seras dans le fichier de sauvegarde
    private String contenu;
    @Saveable//l'attribue suivante seras dans le fichier de sauvegarde
    private CouleurCarte type;
    @Saveable//l'attribue suivante seras dans le fichier de sauvegarde
    private boolean retournee;



    public Carte(int idCarte, String contenu, CouleurCarte type) {
        this.idCarte = idCarte;
        this.contenu = contenu;
        this.type = type;
        this.retournee = false;
    }

    public int getIdCarte() {
        return idCarte;
    }

    public void setIdCarte(int idCarte) {
        this.idCarte = idCarte;
    }

    public String getContenu() {
        return contenu;
    }

    public void setContenu(String contenu) {
        this.contenu = contenu;
    }

    public CouleurCarte getType() {
        return type;
    }

    public void setType(CouleurCarte type) {
        this.type = type;
    }

    public boolean isRetournee() {
        return retournee;
    }

    public void retourner() {
        this.retournee = true;
    }

    static class CouleurCarte {

        public CouleurCarte() {
        }
    }
}