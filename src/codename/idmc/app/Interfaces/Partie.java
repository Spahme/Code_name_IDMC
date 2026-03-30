package codename.idmc.app.Interfaces;

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

        this.equipeCourante = (equipeCommencante == CouleurEquipe.ROUGE) ? equipeRouge : equipeBleu;
        this.scoreRouge = 0;
        this.scoreBleu = 0;

        System.out.println("Plateau initialisé avec " + cartesDisponibles.size() + " cartes.");
    }

    public void demarrerPartie() {
        if (plateau == null) {
            System.out.println("Erreur : le plateau n'est pas initialisé.");
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

        if (type == CouleurCarte.NEUTRE) {
            System.out.println("Carte neutre retournée : fin du tour.");
            passerTour();
            return false;
        }

        CouleurCarte typeEquipeCourante = (equipeCourante.getCouleur() == CouleurEquipe.ROUGE)
                ? CouleurCarte.ROUGE
                : CouleurCarte.BLEU;

        CouleurCarte typeEquipeAdverse = (equipeCourante.getCouleur() == CouleurEquipe.ROUGE)
                ? CouleurCarte.BLEU
                : CouleurCarte.ROUGE;

        if (type == typeEquipeCourante) {
            incrementerScoreEquipeCourante();

            if (aGagneEquipeCourante()) {
                System.out.println(equipeCourante.getNom() + " a gagné la partie !");
                estEnCours = false;
                return false;
            }

            return true;
        }

        if (type == typeEquipeAdverse) {
            incrementerScoreEquipeAdverse();

            if (aGagneEquipeAdverse()) {
                Equipe adverse = getEquipeAdverse();
                System.out.println(adverse.getNom() + " a gagné la partie !");
                estEnCours = false;
                return false;
            }

            System.out.println("Carte adverse retournée : fin du tour.");
            passerTour();
            return false;
        }

        return false;
    }

    private void incrementerScoreEquipeCourante() {
        if (equipeCourante.getCouleur() == CouleurEquipe.ROUGE) {
            scoreRouge++;
        } else {
            scoreBleu++;
        }
    }

    private void incrementerScoreEquipeAdverse() {
        if (equipeCourante.getCouleur() == CouleurEquipe.ROUGE) {
            scoreBleu++;
        } else {
            scoreRouge++;
        }
    }

    private boolean aGagneEquipeCourante() {
        if (equipeCourante.getCouleur() == CouleurEquipe.ROUGE) {
            return plateau.getCartesRestantes(CouleurCarte.ROUGE) == 0;
        }
        return plateau.getCartesRestantes(CouleurCarte.BLEU) == 0;
    }

    private boolean aGagneEquipeAdverse() {
        if (equipeCourante.getCouleur() == CouleurEquipe.ROUGE) {
            return plateau.getCartesRestantes(CouleurCarte.BLEU) == 0;
        }
        return plateau.getCartesRestantes(CouleurCarte.ROUGE) == 0;
    }

    private Equipe getEquipeAdverse() {
        return (equipeCourante == equipeRouge) ? equipeBleu : equipeRouge;
    }

    public void passerTour() {
        indiceCourant = null;
        equipeCourante = (equipeCourante == equipeRouge) ? equipeBleu : equipeRouge;
        tourActuelle++;
        System.out.println("\nC'est maintenant au tour de : " + equipeCourante.getNom());
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