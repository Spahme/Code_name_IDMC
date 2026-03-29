package codename.idmc.app.Interfaces;

public class MaitreEspion extends Joueur {

    public MaitreEspion(String pseudo) {
        super(pseudo);
        setEstMaitreEspion(true);
    }

    public Indice faireUnIndice() {
        return null;
    }
}