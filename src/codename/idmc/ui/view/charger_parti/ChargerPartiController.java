package codename.idmc.ui.view.charger_parti;

import codename.idmc.infrastructure.persistance.GameSave;
import codename.idmc.infrastructure.persistance.LocalSaveRepository;
import codename.idmc.infrastructure.persistance.SaveRepository;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Contrôleur JavaFX pour charger_parti_view.fxml.
 * Alimente la TableView avec les sauvegardes trouvées localement.
 */
public class ChargerPartiController {

    // --- Liaison avec le FXML ---
    @FXML private TableColumn<GameSave, String> colnom;
    @FXML private TableColumn<GameSave, String> coldate;
    @FXML private TableColumn<GameSave, String> coltempjeu;
    @FXML private TableColumn<GameSave, String> colcategorie;
    @FXML private TableColumn<GameSave, String> coldiff;
    @FXML private TableColumn<GameSave, Void>   colbuttoncharge;

    @FXML private TableView<GameSave> tableView;
    @FXML private TextField           searchField;

    // --- Données ---
    private final SaveRepository saveRepository = new LocalSaveRepository();
    private ObservableList<GameSave> toutesLesSauvegardes;

    /**
     * Appelé automatiquement par JavaFX après le chargement du FXML.
     */
    @FXML
    public void initialize() {
        // Liaison colonnes <-> champs de GameSave
        colnom.setCellValueFactory(new PropertyValueFactory<>("nomPartie"));
        coldate.setCellValueFactory(new PropertyValueFactory<>("date"));
        coltempjeu.setCellValueFactory(new PropertyValueFactory<>("tempsDeJeu"));
        colcategorie.setCellValueFactory(new PropertyValueFactory<>("categorie"));
        coldiff.setCellValueFactory(new PropertyValueFactory<>("difficulte"));

        // Bouton "Charger" dans chaque ligne
        colbuttoncharge.setCellFactory(col -> new TableCell<>() {
            private final Button btnCharger = new Button("Charger");
            {
                btnCharger.setOnAction(e -> {
                    GameSave save = getTableView().getItems().get(getIndex());
                    chargerPartie(save);
                });
            }
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : btnCharger);
            }
        });

        // Filtre de recherche en temps réel
        searchField.textProperty().addListener((obs, ancien, nouveau) ->
                filtrerSauvegardes(nouveau)
        );

        // Chargement initial
        chargerListe();
    }

    /**
     * Charge et affiche toutes les sauvegardes dans la TableView.
     */
    private void chargerListe() {
        List<GameSave> sauvegardes = saveRepository.listerTout();
        toutesLesSauvegardes = FXCollections.observableArrayList(sauvegardes);
        tableView.setItems(toutesLesSauvegardes);
    }

    /**
     * Filtre la liste selon le texte saisi dans le champ de recherche.
     */
    private void filtrerSauvegardes(String recherche) {
        if (recherche == null || recherche.isEmpty()) {
            tableView.setItems(toutesLesSauvegardes);
            return;
        }
        String filtre = recherche.toLowerCase();
        List<GameSave> filtrees = toutesLesSauvegardes.stream()
                .filter(s -> s.getNomPartie().toLowerCase().contains(filtre)
                          || s.getCategorie().toLowerCase().contains(filtre)
                          || s.getDifficulte().toLowerCase().contains(filtre))
                .collect(Collectors.toList());
        tableView.setItems(FXCollections.observableArrayList(filtrees));
    }

    /**
     * Lance le chargement d'une partie sélectionnée.
     * À compléter avec la navigation vers l'écran de jeu.
     */
    private void chargerPartie(GameSave save) {
        System.out.println("Chargement de la partie : " + save.getNomPartie());
        // TODO : naviguer vers l'écran de jeu avec l'état restauré
        // Exemple :
        // Partie partie = new Partie();
        // SaveManager.restaurerChamps(partie, save.getEtatPartie());
        // SaveManager.restaurerChamps(partie.getGrille(), save.getEtatGrille());
        // NavigationManager.aller(Screen.PLATEAU, partie);
    }

    /**
     * Bouton retour (lié au FXML si vous ajoutez onAction="#retour").
     */
    @FXML
    public void retour() {
        // TODO : navigation vers l'écran précédent
    }
}
