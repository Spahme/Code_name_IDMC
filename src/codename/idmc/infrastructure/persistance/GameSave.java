package codename.idmc.infrastructure.persistance;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

/**
 * Représente une sauvegarde complète d'une partie.
 * Contient les métadonnées affichées dans charger_parti_view
 * et les données brutes de l'état du jeu.
 */
public class GameSave {

    // --- Métadonnées (affichées dans la TableView) ---
    private String nomPartie;
    private String date;
    private String tempsDeJeu;
    private String categorie;
    private String difficulte;

    // --- État du jeu (champs @Saveable collectés par SaveManager) ---
    private Map<String, Object> etatPartie;
    private Map<String, Object> etatGrille;

    // Constructeur vide requis pour la désérialisation JSON
    public GameSave() {}

    public GameSave(String nomPartie, String categorie, String difficulte,
                    String tempsDeJeu,
                    Map<String, Object> etatPartie,
                    Map<String, Object> etatGrille) {
        this.nomPartie  = nomPartie;
        this.date       = LocalDateTime.now()
                            .format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
        this.tempsDeJeu = tempsDeJeu;
        this.categorie  = categorie;
        this.difficulte = difficulte;
        this.etatPartie = etatPartie;
        this.etatGrille = etatGrille;
    }

    // --- Getters et Setters ---
    public String getNomPartie()  { return nomPartie; }
    public void setNomPartie(String nomPartie) { this.nomPartie = nomPartie; }

    public String getDate()       { return date; }
    public void setDate(String date) { this.date = date; }

    public String getTempsDeJeu() { return tempsDeJeu; }
    public void setTempsDeJeu(String tempsDeJeu) { this.tempsDeJeu = tempsDeJeu; }

    public String getCategorie()  { return categorie; }
    public void setCategorie(String categorie) { this.categorie = categorie; }

    public String getDifficulte() { return difficulte; }
    public void setDifficulte(String difficulte) { this.difficulte = difficulte; }

    public Map<String, Object> getEtatPartie() { return etatPartie; }
    public void setEtatPartie(Map<String, Object> etatPartie) { this.etatPartie = etatPartie; }

    public Map<String, Object> getEtatGrille() { return etatGrille; }
    public void setEtatGrille(Map<String, Object> etatGrille) { this.etatGrille = etatGrille; }
}
