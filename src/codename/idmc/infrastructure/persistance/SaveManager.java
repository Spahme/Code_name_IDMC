package codename.idmc.infrastructure.persistance;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * Moteur de sauvegarde automatique par réflexion.
 *
 * Il parcourt tous les champs annotés @Saveable d'un objet
 * et les collecte dans une Map, sans avoir besoin de savoir
 * à l'avance quelles classes existent.
 *
 * Quand vous ajoutez un nouveau champ dans une classe,
 * il suffit d'ajouter @Saveable dessus — rien d'autre à modifier ici.
 */
public class SaveManager {

    /**
     * Extrait tous les champs @Saveable d'un objet (y compris héritage).
     * Retourne une Map { "nomChamp" -> valeur }.
     */
    public static Map<String, Object> extraireChamps(Object objet) {
        Map<String, Object> donnees = new HashMap<>();

        if (objet == null) return donnees;

        // On remonte toute la hiérarchie de classes (héritage inclus)
        Class<?> classe = objet.getClass();
        while (classe != null && classe != Object.class) {
            for (Field champ : classe.getDeclaredFields()) {
                if (champ.isAnnotationPresent(Saveable.class)) {
                    champ.setAccessible(true); // accès même si private
                    try {
                        donnees.put(champ.getName(), champ.get(objet));
                    } catch (IllegalAccessException e) {
                        System.err.println("Impossible de lire le champ : "
                                + champ.getName());
                    }
                }
            }
            classe = classe.getSuperclass(); // remonte dans la classe parente
        }

        return donnees;
    }

    /**
     * Restitue les valeurs sauvegardées dans un objet existant.
     * Utile au chargement d'une partie.
     */
    public static void restaurerChamps(Object objet, Map<String, Object> donnees) {
        if (objet == null || donnees == null) return;

        Class<?> classe = objet.getClass();
        while (classe != null && classe != Object.class) {
            for (Field champ : classe.getDeclaredFields()) {
                if (champ.isAnnotationPresent(Saveable.class)
                        && donnees.containsKey(champ.getName())) {
                    champ.setAccessible(true);
                    try {
                        Object valeur = donnees.get(champ.getName());
                        // Conversion de type si nécessaire (Jackson retourne des Integer/Double)
                        valeur = convertirType(valeur, champ.getType());
                        champ.set(objet, valeur);
                    } catch (IllegalAccessException e) {
                        System.err.println("Impossible de restaurer le champ : "
                                + champ.getName());
                    }
                }
            }
            classe = classe.getSuperclass();
        }
    }

    /**
     * Conversion de type basique pour compatibilité JSON -> Java.
     * Jackson désérialise les entiers en Integer et les flottants en Double.
     */
    private static Object convertirType(Object valeur, Class<?> typeAttendu) {
        if (valeur == null) return null;
        if (typeAttendu == int.class || typeAttendu == Integer.class) {
            return ((Number) valeur).intValue();
        }
        if (typeAttendu == long.class || typeAttendu == Long.class) {
            return ((Number) valeur).longValue();
        }
        if (typeAttendu == boolean.class || typeAttendu == Boolean.class) {
            return valeur;
        }
        return valeur;
    }
}
