package Projet_livre.dao.controleurLivre;

import java.util.List;

import Projet_livre.dao.modelLivre.Livre;

public interface IActionsLivre {
    // Pour le CRUD - Create Read Update Delete
    public void CtrLivre_ChargerDB();
    // Create
    public String CtrLivre_Enregistrer(Livre livre);
    
    // // Read
     public List<Livre> CtrLivre_GetAll();

     public Livre CtrLivre_GetById(int numLivre);

    public Livre CtrLivre_GetByTitre(String titre);

    // // Update
    public int CtrLivre_Modifier(Livre livre);

    // // Delete
     public int CtrLivre_Enlever(int numLivre); 
}
