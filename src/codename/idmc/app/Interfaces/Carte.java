/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codename.idmc.app.Interfaces;

public class Carte {

    private int idCarte;
    private String contenu;
    private CouleurCarte type;
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
}