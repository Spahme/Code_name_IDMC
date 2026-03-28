package codename.idmc.infrastructure.persistance;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Implémentation concrète de SaveRepository.
 * Sauvegarde chaque partie dans un fichier JSON dans le dossier "saves/".
 *
 * Structure des fichiers :
 *   saves/
 *     ma_partie.json
 *     partie_du_12_mars.json
 *     ...
 */
public class LocalSaveRepository implements SaveRepository {

    private static final String DOSSIER_SAVES = "saves";
    private final ObjectMapper mapper;

    public LocalSaveRepository() {
        this.mapper = new ObjectMapper();
        this.mapper.enable(SerializationFeature.INDENT_OUTPUT); // JSON lisible
        creerDossierSiAbsent();
    }

    @Override
    public void sauvegarder(GameSave sauvegarde) {
        File fichier = getFichier(sauvegarde.getNomPartie());
        try {
            mapper.writeValue(fichier, sauvegarde);
            System.out.println("Partie sauvegardée : " + fichier.getAbsolutePath());
        } catch (IOException e) {
            throw new RuntimeException("Erreur lors de la sauvegarde : "
                    + sauvegarde.getNomPartie(), e);
        }
    }

    @Override
    public GameSave charger(String nomPartie) {
        File fichier = getFichier(nomPartie);
        if (!fichier.exists()) {
            throw new RuntimeException("Sauvegarde introuvable : " + nomPartie);
        }
        try {
            return mapper.readValue(fichier, GameSave.class);
        } catch (IOException e) {
            throw new RuntimeException("Erreur lors du chargement : " + nomPartie, e);
        }
    }

    @Override
    public List<GameSave> listerTout() {
        List<GameSave> liste = new ArrayList<>();
        File dossier = new File(DOSSIER_SAVES);
        File[] fichiers = dossier.listFiles(
                (dir, name) -> name.endsWith(".json")
        );
        if (fichiers == null) return liste;
        for (File f : fichiers) {
            try {
                liste.add(mapper.readValue(f, GameSave.class));
            } catch (IOException e) {
                System.err.println("Fichier de sauvegarde corrompu ignoré : "
                        + f.getName());
            }
        }
        return liste;
    }

    @Override
    public void supprimer(String nomPartie) {
        File fichier = getFichier(nomPartie);
        if (fichier.exists() && !fichier.delete()) {
            System.err.println("Impossible de supprimer : " + fichier.getName());
        }
    }

    // --- Utilitaires privés ---

    private File getFichier(String nomPartie) {
        // Nettoie le nom pour qu'il soit valide comme nom de fichier
        String nomFichier = nomPartie.replaceAll("[^a-zA-Z0-9_\\-]", "_") + ".json";
        return new File(DOSSIER_SAVES, nomFichier);
    }

    private void creerDossierSiAbsent() {
        File dossier = new File(DOSSIER_SAVES);
        if (!dossier.exists()) {
            dossier.mkdirs();
        }
    }
}
