package codename.idmc.app;

import codename.idmc.application.usecases.CreatePartieUseCase;
import codename.idmc.app.Interfaces.CouleurEquipe;
import codename.idmc.app.Interfaces.Partie;
import codename.idmc.ui.view.plateau.PlateauController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class TestPlateauPartie extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        CreatePartieUseCase useCase = new CreatePartieUseCase();

        Partie partie = useCase.execute(
                1,
                2,
                1,
                CouleurEquipe.ROUGE
        );

        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/codename/idmc/ui/view/plateau/board_view.fxml")
        );

        Parent root = loader.load();

        PlateauController controller = loader.getController();
        controller.demarrerAffichageJeu(partie);

        Scene scene = new Scene(root);

        stage.setTitle("Test Plateau");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}