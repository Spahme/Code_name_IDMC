/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */


/**
 *
 * @author _Np
 */


package codename.idmc.ui.view.plateau;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.layout.Priority;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.image.ImageView;
import javafx.scene.Cursor;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;


import codename.idmc.app.Carte;
import codename.idmc.app.Interfaces.Plateau;
import codename.idmc.app.Partie;
import codename.idmc.ui.view.card.CarteViewController; // Import de l'autre contrôleur
import codename.idmc.ui.view.sauvegarder_parti.SauvegarderPartiController;

public class PlateauController implements Initializable {

    @FXML private GridPane plateauPrincipal;
    @FXML private GridPane plateauMEspionGrand;
    @FXML private GridPane plateauMEspionMini;
    @FXML private ImageView xCloseMiniPlateau; 

    // Référence vers modele de jeu
    private Partie partieEnCours;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // État initial de l'interface maitre Espion
        xCloseMiniPlateau.setVisible(false);
        plateauMEspionGrand.setVisible(false);
        configurerInteractionsEspion();
        
        // !!!!!: Pour l'instant, pas de mécanisme pour transmettre la Partie depuis l'écran Menu.
        // faut bidouilller un truc du style "partieEnCours = Session.getPartieActuelle();"
        // Si partieEnCours est null l'affichage plante.
    }

    
    //Méthode à appeler une fois que la partie (avec les vraies données de la BDD) est chargée.
    
    public void demarrerAffichageJeu(Partie partie) {
        this.partieEnCours = partie;
        genererPlateaux();
    }

    private void genererPlateaux() {
        Plateau vraiPlateau = partieEnCours.getPlateau();

        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                // Récupération de la vraie carte depuis la logique
                Carte vraieCarte = vraiPlateau.getCartePosition(i, j);

                // --- Generation carte principale ----
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/codename/idmc/ui/view/card/carte_view.fxml"));
                    StackPane carteNode = loader.load();
                    
                    // On récupère le contrôleur spécifique de cette carte pour lui passer les données
                    CarteViewController carteCtrl = loader.getController();
                    carteCtrl.initialiserCarte(vraieCarte);

                    // Responsive design
                    carteNode.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
                    GridPane.setHgrow(carteNode, Priority.ALWAYS);
                    GridPane.setVgrow(carteNode, Priority.ALWAYS);

                    plateauPrincipal.add(carteNode, j, i);

                } catch (IOException e) {
                    System.err.println("Erreur de chargement du FXML de la carte : " + e.getMessage());
                    e.printStackTrace();
                }

                // --- generation du mini plateau Maitre espion ---
                VBox miniCase = new VBox();
                miniCase.setStyle("-fx-background-color: " + vraieCarte.getCouleurHex() + "; -fx-border-color: black;");
                plateauMEspionMini.add(miniCase, j, i);

                // --- generation du grand plateau Maitre espion ---
                StackPane caseEspionGrand = new StackPane();
                caseEspionGrand.setStyle("-fx-background-color: " + vraieCarte.getCouleurHex() + "; -fx-border-color: black;");
                caseEspionGrand.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE); 
                GridPane.setHgrow(caseEspionGrand, Priority.ALWAYS);
                GridPane.setVgrow(caseEspionGrand, Priority.ALWAYS);
                
                Label motEspion = new Label(vraieCarte.getMot());
                motEspion.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");
                
                // Assurer la lisibilité (Blanc sur Noir/Bleu)
                if (vraieCarte.getCouleurHex().equalsIgnoreCase("#000000") || vraieCarte.getCouleurHex().equalsIgnoreCase("#0000FF")) {
                    motEspion.setTextFill(Color.WHITE); 
                }
                
                caseEspionGrand.getChildren().add(motEspion);
                plateauMEspionGrand.add(caseEspionGrand, j, i);
            }
        }
    }

    private void configurerInteractionsEspion() {
        // Clic sur le mini plateau -> Ouvre le grand plateau pour les Maitres espion par dessus 
        //le plateau de base et affiche la croix a la place du mini plateau
        plateauMEspionMini.setOnMouseClicked(event -> {
            plateauMEspionMini.setVisible(false);
            xCloseMiniPlateau.setVisible(true);
            
            plateauPrincipal.setVisible(false);
            plateauMEspionGrand.setVisible(true);
            plateauMEspionGrand.toFront(); 
        });
        // Clic sur la croix -> fait l'inverse : 
        //Ferme le grand plateau M espion et remet l'affichage du mini plateau
        xCloseMiniPlateau.setOnMouseClicked(event -> {
            xCloseMiniPlateau.setVisible(false);
            plateauMEspionMini.setVisible(true);
            
            plateauMEspionGrand.setVisible(false);
            plateauPrincipal.setVisible(true);
        });
        
        plateauMEspionMini.setCursor(Cursor.HAND);
        xCloseMiniPlateau.setCursor(Cursor.HAND);
    }
}
    /**
     * Méthode liée au bouton "Sauvegarder" dans l'interface du plateau (board_view.fxml).
     */
    @FXML
    public void sauvegarderPartie() {
        if (partieEnCours != null) {
            // C'est la ligne de ton collègue ! Elle ouvre sa popup et s'occupe de tout.
            SauvegarderPartiController.ouvrirPopup(partieEnCours);
        } else {
            System.out.println("Impossible de sauvegarder : aucune partie n'est en cours.");
        }
    }

