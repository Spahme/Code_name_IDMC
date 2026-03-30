package codename.idmc.app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.net.URL;

public class MainApp extends Application {

    private static String hostName;
    private static String lobbyName;
    private static int difficultyId;
    private static int categorieId;
    private static Integer establishmentId;
    private static String equipeCommencante;

    @Override
    public void start(Stage stage) throws Exception {
        URL fxmlUrl = getClass().getResource("/codename/idmc/ui/view/menu/main_menu_view.fxml");

        if (fxmlUrl == null) {
            throw new IllegalStateException(
                    "FXML introuvable : /codename/idmc/ui/view/menu/main_menu_view.fxml"
            );
        }

        FXMLLoader loader = new FXMLLoader(fxmlUrl);
        Scene scene = new Scene(loader.load());

        stage.setTitle("Code Name IDMC");
        stage.setScene(scene);
        stage.show();
    }

    public static String getHostName() {
        return hostName;
    }

    public static void setHostName(String hostName) {
        MainApp.hostName = hostName;
    }

    public static String getLobbyName() {
        return lobbyName;
    }

    public static void setLobbyName(String lobbyName) {
        MainApp.lobbyName = lobbyName;
    }

    public static int getDifficultyId() {
        return difficultyId;
    }

    public static void setDifficultyId(int difficultyId) {
        MainApp.difficultyId = difficultyId;
    }

    public static int getCategorieId() {
        return categorieId;
    }

    public static void setCategorieId(int categorieId) {
        MainApp.categorieId = categorieId;
    }

    public static Integer getEstablishmentId() {
        return establishmentId;
    }

    public static void setEstablishmentId(Integer establishmentId) {
        MainApp.establishmentId = establishmentId;
    }

    public static String getEquipeCommencante() {
        return equipeCommencante;
    }

    public static void setEquipeCommencante(String equipeCommencante) {
        MainApp.equipeCommencante = equipeCommencante;
    }

    public static void main(String[] args) {
        launch(args);
    }
}