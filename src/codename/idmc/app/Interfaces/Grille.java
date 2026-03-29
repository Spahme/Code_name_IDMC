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
    public static final int NEUTRALCARTESCOUNT = 7;
    public static final int ASSASSINCARTESCOUNT = 1;

    private final Carte[][] cartes;

    public Grille() {
        cartes = new Carte[ROWS][COLS];
    }

    public Carte getCard(int row, int col) {
        return cartes[row][col];
    }

    public void setCard(int row, int col, Carte carte) {
        cartes[row][col] = carte;
    }

    public Carte findByWord(String word) {
        for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLS; c++) {
                if (cartes[r][c] != null &&
                    cartes[r][c].getContenu().equalsIgnoreCase(word)) {
                    return cartes[r][c];
                }
            }
        }

        return null;
    }

    public List<Carte> getAllCards() {
        List<Carte> list = new ArrayList<>(SIZE);

        for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLS; c++) {
                if (cartes[r][c] != null) {
                    list.add(cartes[r][c]);
                }
            }
        }

        return Collections.unmodifiableList(list);
    }

    public int countRemaining(CouleurCarte color) {
        int count = 0;

        for (Carte c : getAllCards()) {
            if (c.getType() == color && !c.isRetournee()) {
                count++;
            }
        }

        return count;
    }

    public CouleurCarte revelerCarte(int row, int col) {
        Carte c = cartes[row][col];

        if (c == null) {
            return null;
        }

        c.retourner();
        return c.getType();
    }
}