package codename.idmc.ui.view.config;

import codename.idmc.app.MainApp;
import codename.idmc.infrastructure.network.ApiClient;
import codename.idmc.infrastructure.network.ApiEndPoint;
import codename.idmc.infrastructure.network.ApiItem;
import codename.idmc.infrastructure.network.multiplayer.server.GameServer;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.awt.Desktop;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

public class ConfigPartieController {

    @FXML
    private TextField hostField;

    @FXML
    private TextField lobbyField;

    @FXML
    private ComboBox<ApiItem> difficultyBox;

    @FXML
    private ComboBox<ApiItem> establishmentBox;

    @FXML
    private ComboBox<ApiItem> categoryBox;

    @FXML
    private Button launchButton;

    @FXML
    private Label statusLabel;

    @FXML
    private TextArea previewArea;

    private final List<ApiItem> allCategories = new ArrayList<>();

    @FXML
    public void initialize() {
        hostField.setText("Sasha");
        lobbyField.setText("Lobby Local");

        chargerDonneesApi();
        binderEvenements();
        syncEtatUI();
        mettreAJourPreview();
    }

    private void chargerDonneesApi() {
        List<ApiItem> difficulties = ApiClient.fetch(ApiEndPoint.DIFFICULTIES);
        List<ApiItem> establishments = ApiClient.fetch(ApiEndPoint.ESTABLISHMENTS);
        List<ApiItem> categories = ApiClient.fetch(ApiEndPoint.CATEGORIES);

        difficultyBox.setItems(FXCollections.observableArrayList(difficulties));
        establishmentBox.setItems(FXCollections.observableArrayList(establishments));

        allCategories.clear();
        allCategories.addAll(categories);

        categoryBox.setItems(FXCollections.observableArrayList());
        categoryBox.setDisable(true);
    }

    private void binderEvenements() {
        hostField.textProperty().addListener((obs, o, n) -> {
            syncEtatUI();
            mettreAJourPreview();
        });

        lobbyField.textProperty().addListener((obs, o, n) -> {
            syncEtatUI();
            mettreAJourPreview();
        });

        difficultyBox.valueProperty().addListener((obs, o, n) -> {
            syncEtatUI();
            mettreAJourPreview();
        });

        establishmentBox.valueProperty().addListener((obs, o, n) -> {
            rechargerCategoriesSelonEtablissement();
            syncEtatUI();
            mettreAJourPreview();
        });

        categoryBox.valueProperty().addListener((obs, o, n) -> {
            syncEtatUI();
            mettreAJourPreview();
        });
    }

    private void rechargerCategoriesSelonEtablissement() {
        ApiItem selectedEstablishment = establishmentBox.getValue();

        categoryBox.getItems().clear();
        categoryBox.setValue(null);

        if (selectedEstablishment == null) {
            categoryBox.setDisable(true);
            return;
        }

        categoryBox.setDisable(false);
        categoryBox.setItems(FXCollections.observableArrayList(allCategories));
    }

    private void syncEtatUI() {
        boolean hostOk = hostField.getText() != null && !hostField.getText().isBlank();
        boolean lobbyOk = lobbyField.getText() != null && !lobbyField.getText().isBlank();
        boolean diffOk = difficultyBox.getValue() != null;
        boolean estOk = establishmentBox.getValue() != null;
        boolean catOk = categoryBox.getValue() != null;

        categoryBox.setDisable(!estOk);

        boolean canLaunch = hostOk && lobbyOk && diffOk && estOk && catOk;
        launchButton.setDisable(!canLaunch);

        if (!estOk) {
            statusLabel.setText("Sélectionne un établissement.");
        } else if (!canLaunch) {
            statusLabel.setText("Complète tous les paramètres.");
        } else {
            statusLabel.setText("");
        }
    }

    private void mettreAJourPreview() {
        StringBuilder sb = new StringBuilder();

        sb.append("Nom hôte : ").append(valeurTexte(hostField.getText())).append("\n");
        sb.append("Nom lobby : ").append(valeurTexte(lobbyField.getText())).append("\n");
        sb.append("Difficulté : ").append(valeurItem(difficultyBox.getValue())).append("\n");
        sb.append("Établissement : ").append(valeurItem(establishmentBox.getValue())).append("\n");
        sb.append("Catégorie : ").append(valeurItem(categoryBox.getValue())).append("\n");
        sb.append("Équipe commençante : ROUGE\n");

        previewArea.setText(sb.toString());
    }

    private String valeurTexte(String value) {
        return value == null || value.isBlank() ? "-" : value;
    }

    private String valeurItem(ApiItem item) {
        return item == null ? "-" : item.getName() + " (id=" + item.getId() + ")";
    }

    @FXML
    private void reinitialiser() {
        hostField.setText("Sasha");
        lobbyField.setText("Lobby Local");

        difficultyBox.setValue(null);
        establishmentBox.setValue(null);

        categoryBox.getItems().clear();
        categoryBox.setValue(null);
        categoryBox.setDisable(true);

        syncEtatUI();
        mettreAJourPreview();
    }

    @FXML
    private void lancerPartie() {
        ApiItem difficulty = difficultyBox.getValue();
        ApiItem establishment = establishmentBox.getValue();
        ApiItem category = categoryBox.getValue();

        if (difficulty == null || establishment == null || category == null) {
            statusLabel.setText("Paramètres incomplets.");
            return;
        }

        MainApp.setHostName(hostField.getText().trim());
        MainApp.setLobbyName(lobbyField.getText().trim());
        MainApp.setDifficultyId(difficulty.getId());
        MainApp.setCategorieId(category.getId());
        MainApp.setEstablishmentId(establishment.getId());
        MainApp.setEquipeCommencante("ROUGE");

        new Thread(() -> {
            try {
                new GameServer(5555, MainApp.getLobbyName(), MainApp.getHostName(), 8).start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();

        try {
            Thread.sleep(500);

            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/codename/idmc/ui/view/lobbybrowser/lobby_browser.fxml")
            );

            Stage stage = (Stage) hostField.getScene().getWindow();
            stage.setScene(new Scene(loader.load()));
            stage.setTitle("Codenames - Lobby Browser");

        } catch (Exception e) {
            e.printStackTrace();
            statusLabel.setText("Erreur ouverture lobby browser.");
        }
    }

    @FXML
    private void ouvrirAdmin() {
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