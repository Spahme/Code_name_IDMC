package codename.idmc.ui.view.carte;

import codename.idmc.app.Interfaces.Carte;

@FunctionalInterface
public interface CardClickListener {
    void onCardClicked(Carte carte);
}