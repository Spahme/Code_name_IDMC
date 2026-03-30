package codename.idmc.application.validators;

import codename.idmc.app.Interfaces.Carte;
import codename.idmc.app.Interfaces.CouleurEquipe;
import codename.idmc.app.Interfaces.Partie;

public class CardSelectionValidator {

    public CardSelectionValidationResult validate(
            Partie partie,
            Carte carte,
            CouleurEquipe equipeLocale,
            String roleLocal
    ) {
        if (partie == null) {
            return CardSelectionValidationResult.erreur("Aucune partie n'est chargée.");
        }

        if (carte == null) {
            return CardSelectionValidationResult.erreur("Aucune carte sélectionnée.");
        }

        if (!partie.isEstEnCours()) {
            return CardSelectionValidationResult.erreur("La partie n'est pas démarrée.");
        }

        if (carte.isRetournee()) {
            return CardSelectionValidationResult.erreur("Cette carte est déjà retournée.");
        }

        if (roleLocal == null || !"AGENT".equals(roleLocal)) {
            return CardSelectionValidationResult.erreur("Seul un agent peut choisir une carte.");
        }

        if (equipeLocale == null) {
            return CardSelectionValidationResult.erreur("Aucune équipe locale définie.");
        }

        if (partie.getEquipeCourante() == null) {
            return CardSelectionValidationResult.erreur("Aucune équipe courante définie.");
        }

        if (partie.getEquipeCourante().getCouleur() != equipeLocale) {
            return CardSelectionValidationResult.erreur("Ce n'est pas le tour de votre équipe.");
        }

        if (partie.getIndiceCourant() == null) {
            return CardSelectionValidationResult.erreur("Le maître espion doit d'abord donner un indice.");
        }

        return CardSelectionValidationResult.ok();
    }
}