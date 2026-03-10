/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codename.idmc.app.Interfaces;

/**
 *
 * @author guill
 */
public class Grille {
    private Carte[][] cartes; 

    // --- Getters et Setters ---
    public Carte[][] getCartes() { return cartes; }
    public void setCartes(Carte[][] cartes) { this.cartes = cartes; }

    // --- Méthodes ---
    public Carte getCarte(int x, int y) { return null; }
    
    // Le type de retour devient String car l'énumération n'existe plus
    public String revelerCarte(int x, int y) { return null; } 
}
