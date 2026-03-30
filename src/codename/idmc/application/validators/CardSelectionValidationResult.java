package codename.idmc.application.validators;

public class CardSelectionValidationResult {

    private final boolean valide;
    private final String message;

    private CardSelectionValidationResult(boolean valide, String message) {
        this.valide = valide;
        this.message = message;
    }

    public static CardSelectionValidationResult ok() {
        return new CardSelectionValidationResult(true, null);
    }

    public static CardSelectionValidationResult erreur(String message) {
        return new CardSelectionValidationResult(false, message);
    }

    public boolean isValide() {
        return valide;
    }

    public String getMessage() {
        return message;
    }
}