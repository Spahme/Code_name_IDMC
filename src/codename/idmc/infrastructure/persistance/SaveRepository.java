package codename.idmc.infrastructure.persistance;

import java.util.List;

/**
 * Port hexagonal pour la sauvegarde.
 * Définit ce que le système peut faire avec les sauvegardes,
 * sans dépendre d'une implémentation concrète (fichier, BDD, cloud...).
 *
 * Actuellement implémenté par LocalSaveRepository (fichiers JSON locaux).
 */
public interface SaveRepository {

    /** Sauvegarde une partie (crée ou écrase le fichier). */
    void sauvegarder(GameSave sauvegarde);

    /** Charge une sauvegarde par son nom. */
    GameSave charger(String nomPartie);

    /** Liste toutes les sauvegardes disponibles. */
    List<GameSave> listerTout();

    /** Supprime une sauvegarde par son nom. */
    void supprimer(String nomPartie);
}
