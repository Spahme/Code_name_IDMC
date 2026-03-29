package codename.idmc.app;

import codename.idmc.application.usecases.CreatePartieUseCase;
import codename.idmc.app.Interfaces.CouleurEquipe;
import codename.idmc.app.Interfaces.Partie;
import codename.idmc.ui.view.plateau.PlateauController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceDialog;
import javafx.stage.Stage;

import java.util.List;
import java.util.Optional;

public class TestPlateauPartie extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        CouleurEquipe equipeChoisie = demanderEquipe();
        String roleChoisi = demanderRole();

        if (equipeChoisie == null || roleChoisi == null) {
            System.out.println("Test annulé.");
            return;
        }

        CreatePartieUseCase useCase = new CreatePartieUseCase();

        Partie partie = useCase.execute(
                1,
                3,
                1,
                CouleurEquipe.ROUGE
        );

        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/codename/idmc/ui/view/plateau/board_view.fxml")
        );

        Parent root = loader.load();

        PlateauController controller = loader.getController();
        controller.configurerVueTest(equipeChoisie, roleChoisi);
        controller.demarrerAffichageJeu(partie);

        Scene scene = new Scene(root);

        stage.setTitle("Test Plateau - " + equipeChoisie + " - " + roleChoisi);
        stage.setScene(scene);
        stage.show();
    }

    private CouleurEquipe demanderEquipe() {
        ChoiceDialog<String> dialog = new ChoiceDialog<>("ROUGE", List.of("ROUGE", "BLEU"));
        dialog.setTitle("Choix équipe");
        dialog.setHeaderText("Sélectionne l'équipe à tester");
        dialog.setContentText("Équipe :");

        Optional<String> resultat = dialog.showAndWait();

        if (resultat.isEmpty()) {
            return null;
        }

        return "ROUGE".equals(resultat.get()) ? CouleurEquipe.ROUGE : CouleurEquipe.BLEU;
    }

    private String demanderRole() {
        ChoiceDialog<String> dialog = new ChoiceDialog<>("AGENT", List.of("AGENT", "MAITRE_ESPION"));
        dialog.setTitle("Choix rôle");
        dialog.setHeaderText("Sélectionne le rôle à tester");
        dialog.setContentText("Rôle :");

        Optional<String> resultat = dialog.showAndWait();

        return resultat.orElse(null);
    }

    public static void main(String[] args) {
        launch(args);
    }
}