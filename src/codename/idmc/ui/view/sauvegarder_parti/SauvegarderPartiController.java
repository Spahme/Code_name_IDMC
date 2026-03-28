package codename.idmc.ui.view.sauvegarder_parti;

import codename.idmc.app.Interfaces.Partie;
import codename.idmc.infrastructure.persistance.GameSave;
import codename.idmc.infrastructure.persistance.LocalSaveRepository;
import codename.idmc.infrastructure.persistance.SaveManager;
import codename.idmc.infrastructure.persistance.SaveRepository;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.util.Map;

/**
 * Contrôleur de la popup de sauvegarde.
 *
 * Votre collègue n'a qu'à appeler UNE seule ligne :
 *
 *   SauvegarderPartiController.ouvrirPopup(partie);
 *
 * Tout le reste est récupéré automatiquement depuis l'objet Partie.
 */
public class SauvegarderPartiController {

    @FXML private TextField nomPartieField;
    @FXML private Label     messageErreur;

    private Partie partie;

    private final SaveRepository saveRepository = new LocalSaveRepository();

    /**
     * Méthode statique à appeler depuis le contrôleur du plateau.
     * Une seule ligne suffit !
     */
    public static void ouvrirPopup(Partie partie) {
        try {
            javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(
                SauvegarderPartiController.class.getResource(
                    "/codename/idmc/ui/view/sauvegarder_parti/sauvegarder_parti_view.fxml"
                )
            );

            javafx.scene.Parent root = loader.load();
            SauvegarderPartiController ctrl = loader.getController();
            ctrl.setPartie(partie);

            Stage popup = new Stage();
            popup.setTitle("Sauvegarder");
            popup.setScene(new javafx.scene.Scene(root));
            popup.setResizable(false);
            popup.show();

        } catch (Exception e) {
            System.err.println("Erreur ouverture popup sauvegarde : " + e.getMessage());
        }
    }

    /**
     * Injecte la partie en cours.
     */
    public void setPartie(Partie partie) {
        this.partie = partie;
    }

    /**
     * Déclenché par le bouton "Sauvegarder".
     * Récupère tout automatiquement depuis l'objet Partie.
     */
    @FXML
    public void sauvegarder() {
        String nom = nomPartieField.getText().trim();

        // Validation du nom
        if (nom.isEmpty()) {
            messageErreur.setText("Veuillez entrer un nom de partie !");
            return;
        }

        // Tout est extrait automatiquement depuis Partie via @Saveable
        Map<String, Object> etatPartie = SaveManager.extraireChamps(partie);
        Map<String, Object> etatGrille = SaveManager.extraireChamps(partie.getGrille());

        // Les métadonnées viennent directement de l'objet Partie
        GameSave save = new GameSave(
                nom,
                partie.getCategorie(),
                partie.getDifficulte(),
                partie.getTempsDeJeuFormate(),
                etatPartie,
                etatGrille
        );

        saveRepository.sauvegarder(save);
        System.out.println("Partie \"" + nom + "\" sauvegardée !");

        fermerPopup();
    }

    /**
     * Déclenché par le bouton "Annuler".
     */
    @FXML
    public void annuler() {
        fermerPopup();
    }

    private void fermerPopup() {
        Stage stage = (Stage) nomPartieField.getScene().getWindow();
        stage.close();
    }
}
