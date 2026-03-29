package codename.idmc.app.Interfaces;

import codename.idmc.app.Interfaces.Carte;
import codename.idmc.app.Interfaces.CouleurCarte;
import codename.idmc.app.Interfaces.CouleurEquipe;
import codename.idmc.app.Interfaces.Equipe;
import codename.idmc.app.Interfaces.Joueur;
import codename.idmc.app.Interfaces.Plateau;
import codename.idmc.infrastructure.persistance.Saveable;

import java.util.ArrayList;
import java.util.List;

public class Partie {

    @Saveable
    private int nbJoueur;

    @Saveable
    private int tourActuelle;

    @Saveable
    private int scoreRouge;

    @Saveable
    private int scoreBleu;

    @Saveable
    private Grille grille;

    @Saveable
    private GrilleCarteCle grilleME;

    @Saveable
    private String difficulte;

    @Saveable
    private String categorie;

    @Saveable
    private long tempsDeJeuEnSecondes;

    private Equipe equipeRouge;
    private Equipe equipeBleu;
    private final List<Joueur> joueurs;
    private Equipe equipeCourante;
    private Plateau plateau;
    private Indice indiceCourant;
    private boolean estEnCours;

    public Partie() {
        this.equipeRouge = new Equipe("Équipe Rouge", CouleurEquipe.ROUGE, 9);
        this.equipeBleu = new Equipe("Équipe Bleue", CouleurEquipe.BLEU, 8);
        this.joueurs = new ArrayList<>();
        this.equipeCourante = equipeRouge;
        this.plateau = null;
        this.indiceCourant = null;
        this.estEnCours = false;

        this.nbJoueur = 0;
        this.tourActuelle = 0;
        this.scoreRouge = 0;
        this.scoreBleu = 0;
        this.grille = null;
        this.grilleME = null;
        this.difficulte = null;
        this.categorie = null;
        this.tempsDeJeuEnSecondes = 0;
    }

    public void choisirEquipe(Joueur joueur, CouleurEquipe couleur) {
        if (couleur == CouleurEquipe.ROUGE) {
            equipeRouge.ajouterJoueur(joueur);
        } else {
            equipeBleu.ajouterJoueur(joueur);
        }

        joueurs.add(joueur);
        nbJoueur = joueurs.size();

        System.out.println(joueur.getPseudo() + " a rejoint " + joueur.getEquipe().getNom());
    }

    public void initialiserPlateau(List<Carte> cartesDisponibles) {
        initialiserPlateau(cartesDisponibles, CouleurEquipe.ROUGE);
    }

    public void initialiserPlateau(List<Carte> cartesDisponibles, CouleurEquipe equipeCommencante) {
        this.plateau = new Plateau(equipeCommencante);
        this.plateau.initialiser(cartesDisponibles);

        if (this.plateau.getGrille() != null) {
            this.grille = this.plateau.getGrille();
        }

        System.out.println("Plateau initialisé avec " + cartesDisponibles.size() + " cartes.");
    }

    public void demarrerPartie() {
        if (plateau == null) {
            System.out.println("Erreur : le plateau n'est pas initialisé.");
            return;
        }

        if (equipeRouge.getJoueurs().size() < 2 || equipeBleu.getJoueurs().size() < 2) {
            System.out.println("Erreur : chaque équipe doit avoir au moins 2 joueurs.");
            return;
        }

        if (!equipeAUnMaitreEspion(equipeRouge) || !equipeAUnMaitreEspion(equipeBleu)) {
            System.out.println("Erreur : chaque équipe doit avoir exactement 1 maître espion.");
            return;
        }

        estEnCours = true;
        tourActuelle = 1;

        System.out.println("\n╔══════════════════════════════╗");
        System.out.println("║   La partie commence !       ║");
        System.out.println("╚══════════════════════════════╝");
        System.out.println("C'est au tour de : " + equipeCourante.getNom());
        plateau.afficher(false);
    }

    public void donnerIndice(String mot, int nombre) {
        if (!estEnCours) {
            System.out.println("La partie n'est pas encore démarrée.");
            return;
        }

        indiceCourant = new Indice(mot, nombre, equipeCourante);
        System.out.println("\nIndice de " + equipeCourante.getNom() + " : " + indiceCourant);
    }

    public boolean jouerCarte(String contenu) {
        if (!estEnCours) {
            System.out.println("La partie n'est pas encore démarrée.");
            return false;
        }

        if (indiceCourant == null) {
            System.out.println("Le maître espion doit d'abord donner un indice.");
            return false;
        }

        CouleurCarte type = plateau.jouerCarte(contenu);
        if (type == null) {
            return false;
        }

        System.out.println("Carte retournée : " + contenu + " → " + type);

        if (type == CouleurCarte.ASSASSIN) {
            System.out.println("L'assassin a été retourné ! " + equipeCourante.getNom() + " perd la partie !");
            estEnCours = false;
            return false;
        }

        CouleurCarte typeCourant = equipeCourante.getCouleur() == CouleurEquipe.ROUGE
                ? CouleurCarte.ROUGE
                : CouleurCarte.BLEU;

        if (type == typeCourant) {
            equipeCourante.carteRetournee();

            if (equipeCourante.getCouleur() == CouleurEquipe.ROUGE) {
                scoreRouge++;
            } else {
                scoreBleu++;
            }

            if (equipeCourante.aGagne()) {
                System.out.println(equipeCourante.getNom() + " a gagné la partie !");
                estEnCours = false;
                return false;
            }

            return true;
        }

        Equipe equipeAdverse = equipeCourante == equipeRouge ? equipeBleu : equipeRouge;
        CouleurCarte typeAdverse = equipeAdverse.getCouleur() == CouleurEquipe.ROUGE
                ? CouleurCarte.ROUGE
                : CouleurCarte.BLEU;

        if (type == typeAdverse) {
            equipeAdverse.carteRetournee();

            if (equipeAdverse.getCouleur() == CouleurEquipe.ROUGE) {
                scoreRouge++;
            } else {
                scoreBleu++;
            }

            if (equipeAdverse.aGagne()) {
                System.out.println(equipeAdverse.getNom() + " a gagné la partie !");
                estEnCours = false;
                return false;
            }
        }

        passerTour();
        return false;
    }

    public void passerTour() {
        indiceCourant = null;
        equipeCourante = (equipeCourante == equipeRouge) ? equipeBleu : equipeRouge;
        tourActuelle++;
        System.out.println("\nC'est maintenant au tour de : " + equipeCourante.getNom());
    }

    private boolean equipeAUnMaitreEspion(Equipe equipe) {
        int count = 0;
        for (Joueur j : equipe.getJoueurs()) {
            if (j.isEstMaitreEspion()) {
                count++;
            }
        }
        return count == 1;
    }

    public String getTempsDeJeuFormate() {
        long heures = tempsDeJeuEnSecondes / 3600;
        long minutes = (tempsDeJeuEnSecondes % 3600) / 60;
        long secondes = tempsDeJeuEnSecondes % 60;
        return String.format("%02d:%02d:%02d", heures, minutes, secondes);
    }

    public int getNbJoueur() {
        return nbJoueur;
    }

    public void setNbJoueur(int nbJoueur) {
        this.nbJoueur = nbJoueur;
    }

    public int getTourActuelle() {
        return tourActuelle;
    }

    public void setTourActuelle(int tourActuelle) {
        this.tourActuelle = tourActuelle;
    }

    public int getScoreRouge() {
        return scoreRouge;
    }

    public void setScoreRouge(int scoreRouge) {
        this.scoreRouge = scoreRouge;
    }

    public int getScoreBleu() {
        return scoreBleu;
    }

    public void setScoreBleu(int scoreBleu) {
        this.scoreBleu = scoreBleu;
    }

    public Grille getGrille() {
        return grille;
    }

    public void setGrille(Grille grille) {
        this.grille = grille;
    }

    public GrilleCarteCle getGrilleME() {
        return grilleME;
    }

    public void setGrilleME(GrilleCarteCle grilleME) {
        this.grilleME = grilleME;
    }

    public String getDifficulte() {
        return difficulte;
    }

    public void setDifficulte(String difficulte) {
        this.difficulte = difficulte;
    }

    public String getCategorie() {
        return categorie;
    }

    public void setCategorie(String categorie) {
        this.categorie = categorie;
    }

    public long getTempsDeJeuEnSecondes() {
        return tempsDeJeuEnSecondes;
    }

    public void setTempsDeJeuEnSecondes(long tempsDeJeuEnSecondes) {
        this.tempsDeJeuEnSecondes = tempsDeJeuEnSecondes;
    }

    public Equipe getEquipeRouge() {
        return equipeRouge;
    }

    public Equipe getEquipeBleu() {
        return equipeBleu;
    }

    public List<Joueur> getJoueurs() {
        return joueurs;
    }

    public Equipe getEquipeCourante() {
        return equipeCourante;
    }

    public Plateau getPlateau() {
        return plateau;
    }

    public Indice getIndiceCourant() {
        return indiceCourant;
    }

    public boolean isEstEnCours() {
        return estEnCours;
    }

    @Override
    public String toString() {
        return estEnCours
                ? "Partie en cours - Tour : " + equipeCourante.getNom()
                : "Partie non démarrée";
    }
}