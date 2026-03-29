package codename.idmc.app.Interfaces;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Grille {

    public static final int ROWS = 5;
    public static final int COLS = 5;
    public static final int SIZE = ROWS * COLS;

   

    public static final int REDCARTESCOUNT = 8;
    public static final int BLUECARTESCOUNT = 9;
    public static final int WHITECARTESCOUNT = 7;
    public static final int BLACKCARTESCOUNT = 1;
     private Carte[][] cartes;

    public Grille() {
        cartes = new Carte[ROWS][COLS];
    }

    // récupérer une carte précise
    public Carte getCard(int row, int col) {
        return cartes[row][col];
    }

    // placer une carte dans la grille
    public void setCard(int row, int col, Carte carte) {
        cartes[row][col] = carte;
    }

    // trouver une carte par mot
    public Carte findByWord(String word) {
        for (int r = 0; r < ROWS; r++)
            for (int c = 0; c < COLS; c++)
                if (cartes[r][c] != null &&
                        cartes[r][c].getContenu().equalsIgnoreCase(word))
                    return cartes[r][c];

        return null;
    }

    // récupérer toutes les cartes
    public List<Carte> getAllCards() {

        List<Carte> list = new ArrayList<>(SIZE);

        for (int r = 0; r < ROWS; r++)
            for (int c = 0; c < COLS; c++)
                if (cartes[r][c] != null)
                    list.add(cartes[r][c]);

        return Collections.unmodifiableList(list);
    }

    // compter les cartes restantes d'une couleur
    public int countRemaining(CouleurCarte color) {

        int count = 0;

        for (Carte c : getAllCards()) {

            if (c.getType() == color && !c.isRetournee()) {
                count++;
            }

        }

        return count;
    }

    // révéler une carte
    public CouleurCarte revelerCarte(int row, int col) {

        Carte c = cartes[row][col];

        if (c == null) {
            return null;
        }

        c.retourner();

        return c.getType();
    }
}