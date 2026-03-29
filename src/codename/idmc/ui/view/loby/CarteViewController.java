package codename.idmc.ui.view.card;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author _Np
 */

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import codename.idmc.app.Carte;

public class CarteViewController {

    @FXML private Label motLabel;
    @FXML private StackPane faceCachee; //  recto contenant la couleur + l'image
    @FXML private Pane fondCouleur;     // fond qui determine la carte
    @FXML private ImageView imageProf;  // L'image du prof/images neutres a mettre en PNG transparent!

    private Carte carteModele;

    @FXML
    public void initialize() {
        faceCachee.setVisible(false);
    }

    /**
     * idee du racords bdd utilisé pour le test -> il y aura besoins de getter &setter:
     * Méthode appelée par le PlateauController pour lier les données (Modèle) à la Vue.
     * @param carte
     * 
     * 
     */
    public void initialiserCarte(Carte carte) {
        this.carteModele = carte;
        
        // affichage du mot
        motLabel.setText(carteModele.getMot());

        // preparation couleur Recto
        // (Ex: getCouleurHex() doit renvoyer "#FF0000" pour rouge)
        fondCouleur.setStyle("-fx-background-color: " + carteModele.getCouleurHex() + ";");
        
        // preparation image du prof/fond
        if(carteModele.getCheminImageProf() != null && !carteModele.getCheminImageProf().isEmpty()) {
            try {
                Image img = new Image(getClass().getResourceAsStream(carteModele.getCheminImageProf()));
                imageProf.setImage(img);
                imageProf.setVisible(true);
            } catch (Exception e) {
                System.err.println("Image introuvable : " + carteModele.getCheminImageProf());
                imageProf.setVisible(false);
            }
        } else {
            imageProf.setVisible(false); // Carte neutre, sans prof
        }
    }

    
    //Méthode liée au clic sur la carte -> utilise des placeholder !
    
    @FXML
    public void auClicSurCarte() {
        // Si la carte n'est pas déjà découverte
        if (!carteModele.estDecouverte()) {
            carteModele.reveler(); 
            
            faceCachee.setVisible(true); 
            
            // !! ici plus tard: Dire au PlateauController de vérifier si la partie est finie (Assassin, etc.)
        }
    }
}
