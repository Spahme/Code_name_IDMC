/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codename.idmc.app.Interfaces;

/**
 *
 * @author guill
 */
public class Carte {
    private int idCarte;
    private String contenu;
    private String type; // Remplace l'énumération (ex: "Rouge", "Bleu", "Neutre", "Assassin")

    // --- Getters et Setters ---
    public int setIdCart(){}; //i d aleatoire unique de 1 a 25

    public String getContenu() { return contenu; }
    public void setContenu(String contenu) { this.contenu = contenu; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

}
