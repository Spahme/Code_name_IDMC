package codename.idmc.app;

import codename.idmc.infrastructure.network.multiplayer.server.GameServer;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class TestLobbyBrowser extends Application {

    @Override
    public void start(Stage stage) throws Exception {

        // ================= SERVEUR EN LOCAL =================

        new Thread(() -> {
            try {
                new GameServer(5555, "Lobby Local", "Sasha", 8).start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();

        // petit délai pour laisser le serveur démarrer
        Thread.sleep(500);

        // ================= UI =================

        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/codename/idmc/ui/view/lobbybrowser/lobby_browser.fxml")
        );

        Scene scene = new Scene(loader.load());

        stage.setTitle("Test Lobby Browser");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}