package codename.idmc.ui.view.Sauvegarde;

import codename.idmc.app.Interfaces.Partie;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class SauvegarderPartiController {

    private SauvegarderPartiController() {
    }

    public static void ouvrirPopup(Partie partie) {
        if (partie == null) {
            Alert alert = new Alert(AlertType.WARNING);
            alert.setTitle("Sauvegarde");
            alert.setHeaderText("Impossible de sauvegarder");
            alert.setContentText("Aucune partie n'est en cours.");
            alert.showAndWait();
            return;
        }

        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Sauvegarde");
        alert.setHeaderText("Sauvegarde de la partie");
        alert.setContentText("Popup de sauvegarde prête.\nPartie : " + partie);
        alert.showAndWait();
    }
}