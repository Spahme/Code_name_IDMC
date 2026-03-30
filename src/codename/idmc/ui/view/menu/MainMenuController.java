package codename.idmc.ui.view.menu;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.awt.Desktop;
import java.net.URI;

public class MainMenuController {

    @FXML
    private Label statusLabel;

    @FXML
    private void creerPartie() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/codename/idmc/ui/view/config/config_partie_view.fxml")
            );

            Stage stage = (Stage) statusLabel.getScene().getWindow();
            stage.setScene(new Scene(loader.load()));
            stage.setTitle("Configuration de la partie");

        } catch (Exception e) {
            e.printStackTrace();
            statusLabel.setText("Erreur ouverture configuration.");
        }
    }

    @FXML
    private void rejoindrePartie() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/codename/idmc/ui/view/lobbybrowser/lobby_browser.fxml")
            );

            Stage stage = (Stage) statusLabel.getScene().getWindow();
            stage.setScene(new Scene(loader.load()));
            stage.setTitle("Lobby Browser");

        } catch (Exception e) {
            e.printStackTrace();
            statusLabel.setText("Erreur ouverture lobby browser.");
        }
    }

    @FXML
    private void chargerPartie() {
        statusLabel.setText("Chargement de partie non implémenté.");
    }

    @FXML
    private void ajouterContenu() {
        try {
            Desktop.getDesktop().browse(
                    new URI("https://sasha-daza.fr/APP/code_name/admin/")
            );
        } catch (Exception e) {
            e.printStackTrace();
            statusLabel.setText("Impossible d’ouvrir le lien.");
        }
    }
}