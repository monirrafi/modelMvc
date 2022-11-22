package Projet_livre.dao.modelLivre;
import java.util.List;

public interface ILivre {

    // Pour le CRUD - Create Read Update Delete
    
    // Create
    public String MdlLivre_Enregistrer(Livre livre);
    
    // Read
    public List<Livre> MdlLivre_GetAll();

    public Livre MdlLivre_GetByID(int numLivre);

    public Livre MdlLivre_GetByTitre(String Titre);
    
    // Update
    public int MdlLivre_Modifier(Livre livre);

    // Delete
    public int MdlLivre_Supprimer(int numLivre);

}
