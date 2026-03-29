package codename.idmc.app.Interfaces;

import codename.idmc.infrastructure.persistance.Saveable;

public class Carte {

    @Saveable
    private int idCarte;

    @Saveable
    private String contenu;

    @Saveable
    private CouleurCarte type;

    @Saveable
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

    public void setRetournee(boolean retournee) {
        this.retournee = retournee;
    }

    public String getCouleurHex() {
    if (type == null) {
        return "#FFFFFF";
    }

    return switch (type) {
        case ROUGE -> "#E53935";
        case BLEU -> "#1E88E5";
        case NEUTRE -> "#D6D6D6";
        case ASSASSIN -> "#212121";
    };
}

    @Override
    public String toString() {
        return "Carte{" +
                "idCarte=" + idCarte +
                ", contenu='" + contenu + '\'' +
                ", type=" + type +
                ", retournee=" + retournee +
                '}';
    }
}