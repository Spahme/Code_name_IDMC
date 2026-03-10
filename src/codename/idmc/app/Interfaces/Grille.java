/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codename.idmc.app;

/**
 *
 * @author guill
 */
public class Grille {
    
    // faire une grille de 5*5
    public static final int ROWS = 5;
    public static final int COLS = 5;
    public static final int SIZE = ROWS * COLS; // 25
    private Carte[][] cartes; 
    
    
    
    

    // getter: ça retourne seulement la carte de la row et col demander 
    public Carte getCard(int row, int col) {
    return Carte[row][col];
}
    //trouver le mot exacte sans distinction de la caractère
    public Carte findByWord(String word) {
        for (int r = 0; r < ROWS; r++)
            for (int c = 0; c < COLS; c++)
                if (cartes[r][c].getWord().equalsIgnoreCase(word))
                    return cartes[r][c];
        return null;
    }
    //récuperation des cartes
    public List<Carte> getAllCards() {
        List<Carte> list = new ArrayList<>(SIZE);
        for (int r = 0; r < ROWS; r++)
            for (int c = 0; c < COLS; c++)
                list.add(cartes[r][c]);
        return Collections.unmodifiableList(list);
    }
//exception WIK
    
    
    //setter 
    //public void setCartes(Carte[][] cartes) { this.cartes = cartes; } **PAS BESOIN pour le moment**

    // --- Méthodes ---
    public Carte getCarte(int x, int y) { return null; }
    
    
    // compte combiend de carte de couleur il reste 
    public int countRemaining(CardColor color) {
        int count = 0;
        for (Carte c : getAllCards())
            if (c.getColor() == color && !c.isRevealed())
                count++;
        return count;
    }
}

    
    // Le type de retour devient String car l'énumération n'existe plus
   // public String revelerCarte(int x, int y) { return null; } <=//
