package codename.idmc.infrastructure.persistance;

import codename.idmc.app.Interfaces.Partie;
import codename.idmc.app.Interfaces.Grille;

import java.util.Map;

/**
 * Exemple d'utilisation du système de sauvegarde.
 * Ce fichier est un guide — ne pas inclure dans la build finale.
 *
 * ---------------------------------------------------------------
 * ÉTAPE 1 — Annoter vos classes avec @Saveable
 * ---------------------------------------------------------------
 *
 * Dans Partie.java, ajoutez @Saveable sur les champs à sauvegarder :
 *
 *   import codename.idmc.infrastructure.persistance.Saveable;
 *
 *   @Saveable private int nbJoueur;
 *   @Saveable private int tourActuelle;
 *   @Saveable private int scoreRouge;
 *   @Saveable private int scoreBleu;
 *
 * Dans Carte.java :
 *
 *   @Saveable private int idCarte;
 *   @Saveable private String contenu;
 *   @Saveable private boolean retournee;
 *   (pas besoin de sauvegarder "type" si les cartes sont rechargées depuis l'API)
 *
 * ---------------------------------------------------------------
 * ÉTAPE 2 — Sauvegarder une partie en cours
 * ---------------------------------------------------------------
 */
public class ExempleSauvegarde {

    public static void sauvegarderPartieEnCours(Partie partie, String nomPartie,
                                                 String categorie, String difficulte,
                                                 String tempsDeJeu) {

        // 1. Extraire les champs @Saveable automatiquement
        Map<String, Object> etatPartie = SaveManager.extraireChamps(partie);
        Map<String, Object> etatGrille = SaveManager.extraireChamps(partie.getGrille());

        // 2. Créer l'objet de sauvegarde
        GameSave save = new GameSave(
                nomPartie,
                categorie,
                difficulte,
                tempsDeJeu,
                etatPartie,
                etatGrille
        );

        // 3. Écrire sur disque
        SaveRepository repo = new LocalSaveRepository();
        repo.sauvegarder(save);
    }

    /**
     * ---------------------------------------------------------------
     * ÉTAPE 3 — Charger une partie sauvegardée
     * ---------------------------------------------------------------
     */
    public static Partie chargerPartie(String nomPartie) {

        SaveRepository repo = new LocalSaveRepository();
        GameSave save = repo.charger(nomPartie);

        // Créer des objets vides et y restaurer les données
        Partie partie = new Partie();
        SaveManager.restaurerChamps(partie, save.getEtatPartie());

        Grille grille = new Grille();
        SaveManager.restaurerChamps(grille, save.getEtatGrille());
        partie.setGrille(grille);

        return partie;
    }
}
