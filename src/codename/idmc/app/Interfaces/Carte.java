/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codename.idmc.app;

/**
 *
 * @author guill
 */
public class Carte {

    private int idCarte;
    private String contenu;
    //private String type; // Remplace l'énumération (ex: "Rouge", "Bleu", "Neutre", "Assassin")
    private boolean retournee;

    //constructor 
    public Carte(int idCat, String contenu, String categorie, TypeCarte type) {
        this.contenu = contenu;
        this.type = type;
        this.retournee = false;
    }

    // --- Getters et Setters ---
    public CouleurCarte getColor() {
        return couleur;
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

    public TypeCarte getType() {
        return type;
    }

    public void setType(TypeCarte type) {
        this.type = type;
    }

    public boolean isRetournee() {
        return retournee;
    }

    public void retourner() {
        this.retournee = true;
    }
}
