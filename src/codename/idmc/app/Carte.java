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
    private int idCat;
    private int idDiff;
    private String contenu;
    private String type; // Remplace l'énumération (ex: "Rouge", "Bleu", "Neutre", "Assassin")
    private String categorie;

    // --- Getters et Setters ---
    public int getIdCat() { return idCat; }
    public void setIdCat(int idCat) { this.idCat = idCat; }

    public int getIdDiff() { return idDiff; }
    public void setIdDiff(int idDiff) { this.idDiff = idDiff; }

    public String getContenu() { return contenu; }
    public void setContenu(String contenu) { this.contenu = contenu; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getCategorie() { return categorie; }
    public void setCategorie(String categorie) { this.categorie = categorie; }
}
