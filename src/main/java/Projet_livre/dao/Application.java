package Projet_livre.dao;
import Projet_livre.dao.vueLivre.VueLivre;

public final class Application {
    private Application() {
    }

    public static void main(String[] args) {
        VueLivre vue = new VueLivre();
        vue.setVisible(true);
	}
}