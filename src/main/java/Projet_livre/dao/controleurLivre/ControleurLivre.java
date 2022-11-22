package Projet_livre.dao.controleurLivre;

import java.util.List;

import Projet_livre.dao.modelLivre.DaoLivre;
import Projet_livre.dao.modelLivre.Livre;

public class ControleurLivre implements IActionsLivre {

    private static ControleurLivre CtrLivre_Instance = null;
    private static DaoLivre Dao_Instance = null;
    // Singleton du contrôleur
    // getControleurFilm() est devenu une zonne critique.
    // Pour ne pas avoir deux processus légers (threads) qui
    // appellent au même temps getConnexion
    private ControleurLivre(){}

    public static synchronized ControleurLivre getControleurLivre() {
        //try {
            if (CtrLivre_Instance == null) {
                CtrLivre_Instance = new ControleurLivre();
                Dao_Instance = DaoLivre.getLivreDao();
            }
            return CtrLivre_Instance;/*
        } catch (Exception e) {
            // e.printStackTrace();
            throw new RuntimeException(e);
        }*/
    }

    @Override
    public void CtrLivre_ChargerDB() {
        try {
               Dao_Instance.remplirBD();
            } catch (Exception e) {
                e.printStackTrace();
            }
        
    }

    public String CtrLivre_Enregistrer(Livre livre) {
        String message = null;
        message = Dao_Instance.MdlLivre_Enregistrer(livre);
        return message;
    }


    @Override
    public Livre CtrLivre_GetById(int numLivre) {
        return Dao_Instance.MdlLivre_GetByID(numLivre);
    }

    @Override
    public Livre CtrLivre_GetByTitre(String titre) {
        return Dao_Instance.MdlLivre_GetByTitre(titre);
    }

    @Override
    public int CtrLivre_Modifier(Livre livre) {
        return Dao_Instance.MdlLivre_Modifier(livre);
    }

    @Override
    public int CtrLivre_Enlever(int numLivre) {
        return Dao_Instance.MdlLivre_Supprimer(numLivre);
    }

    @Override
    public List<Livre> CtrLivre_GetAll() {
        return Dao_Instance.MdlLivre_GetAll();
    }


}
