package codename.idmc.infrastructure.persistance;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marquez avec @Saveable tous les champs que vous voulez sauvegarder.
 * Le SaveManager les détectera automatiquement par réflexion.
 *
 * Exemple d'utilisation dans vos classes :
 *
 *   @Saveable
 *   private String nom;
 *
 *   @Saveable
 *   private int score;
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Saveable {
}
