package Projet_livre.dao.modelLivre;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

public class DaoLivre implements ILivre {
    private static Connection conn = null;
    private static DaoLivre instanceDao = null;

    // MySQL
    //private static final String PILOTE = "com.mysql.jdbc.Driver";
    private static final String NOM_BD = "bdBiblio";
    private static final String FICHIER_TXT = "src/main/java/Projet_livre/dao/modelLivre/livres.txt";
    private static final String URL_BD = "jdbc:mysql://localhost/" +NOM_BD;     //bd_Biblio";
    private static final String USAGER = "root";
    private static final String PASS = "";

    private static final String SUPPRIMER = "DELETE FROM livre WHERE numLivre=?";
    private static final String GET_ALL = "SELECT * FROM livre ORDER BY numLivre";
    private static final String GET_BY_ID = "SELECT * FROM livre WHERE numLivre=?";
    private static final String GET_BY_TITRE = "SELECT * FROM livre WHERE titreLivre=?";
    private static final String ENREGISTRER = "INSERT INTO livre VALUES(?,?, ?,?,?, ?)";
    private static final String MODIFIER = "UPDATE livre SET numLivre=?,titreLivre=?,Auteur=?,Annee=?,Pages=?,Cathegorie=? WHERE numLivre=?";

    // Singleton de connexion à la BD
    // getConnexion() est devenu une zonne critique. 
    // Pour ne pas avoir deux processus légers (threads) qui
    // appellent au même temps getConnexion

    private DaoLivre(){};

    public static synchronized DaoLivre getLivreDao () {
        try {
            verifierBD();
            //createTableLivre();
                if (instanceDao == null) {
                    instanceDao = new DaoLivre();
                    conn = DriverManager.getConnection(URL_BD, USAGER, PASS);
                }
                return instanceDao;
            
        } 
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    public static void verifierBD() {
//        String mabase = "bd_Biblio";
        boolean trouve =false;
        try {
            //Class.forName("com.mysql.jdbc.Driver");
            Connection  con = DriverManager.getConnection("jdbc:mysql://localhost:3306/",USAGER,PASS); // tu peux remarquer qu'on peut laisser le champs du nom de la base de données vide !
            Statement st1 = con.createStatement();
        
            ResultSet rs1 = st1.executeQuery("show databases;");
            while (rs1.next()) {
                // ici tu fait le traitement que tu veux avec le résultat de la requette, notament vérifier l'éxistence de ta base de données
                if (rs1.getString(1).equalsIgnoreCase(NOM_BD)){
                    trouve = true;
                    break;
                }
            }
        
            if (trouve == false){ 
                int reponse =JOptionPane.showConfirmDialog(null,"la Base de Données " + NOM_BD +" n'existe pas\n Voulez vous la creer ?");

                if(reponse==0){
                    try {
                            createDB();
                            
                        } catch (IOException e) {
                            e.printStackTrace();
                        } 
                }else{
                    System.exit(0);
                }
    
            }
        }
        catch(SQLException e) {
            System.out.println("erreur sql " + e.toString());
        }
                
    }

    public  void remplirBD() {
        int compteur =0;
        String ligne;
        String []elems;
    try {
        BufferedReader  data = new BufferedReader(new InputStreamReader(new FileInputStream(FICHIER_TXT),StandardCharsets.ISO_8859_1));
        
        ligne = data.readLine().trim();
        while (ligne != null) {
           elems=ligne.split(";");
           Livre livre =new Livre(Integer.parseInt(elems[0]),elems[1],Integer.parseInt(elems[2]),
           Integer.parseInt(elems[3]),Integer.parseInt(elems[4]),elems[5]);
           MdlLivre_Enregistrer(livre);
           compteur++;
           ligne = data.readLine();
        }
       data.close();
       JOptionPane.showMessageDialog(null, compteur + " livres sont enregistrés dans la table livre");
        
    } catch (Exception e) {
    } 
  
    }
    public static void createDB() throws IOException {
        String msg="";
        if(conn == null){    
	 
            try {
                conn = DriverManager.getConnection("jdbc:mysql://localhost/", USAGER,PASS);
                Statement stmt1 = conn.createStatement();

                String query1 = "CREATE database "+ NOM_BD;
                String query2="alter database "+ NOM_BD+" charset=utf8";
                String query3 = "USE "+ NOM_BD;
                String query4 = "CREATE TABLE livre (numLivre int not null"; 
                query4 += ",titreLivre varchar(100),Auteur int,Annee int,Pages int,Cathegorie varchar(30),";
                query4 += " CONSTRAINT livre_pk PRIMARY KEY (numLivre))";
                //Executing the query
                stmt1.execute(query1);
                stmt1.execute(query2);
                
                stmt1.execute(query3);
                stmt1.execute(query4);
                msg += "La base de données " + NOM_BD + " est crée\n";
                msg += "La table livre est crée\n";
                //System.out.println("Database created");
                JOptionPane.showMessageDialog(null, msg);

                
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        

  } 

    @Override
    public String MdlLivre_Enregistrer(Livre livre) {
        PreparedStatement stmt = null;
        try {
            stmt = conn.prepareStatement(ENREGISTRER, Statement.RETURN_GENERATED_KEYS);
            stmt.setInt(1, livre.getNum());
            stmt.setString(2, livre.getTitre());
            stmt.setInt(3, livre.getAuteur());
            stmt.setInt(4, livre.getAnnee());
            stmt.setInt(5, livre.getPages());
            stmt.setString(6, livre.getCathegorie());
           
            stmt.executeUpdate();
            ResultSet rs = stmt.getGeneratedKeys();

            if (rs.next()) {
                livre.setNum(rs.getInt(1));
            }
            return "Livre est bien enregistré ";
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            MdlLivre_Fermer(stmt);
           // MdlLivre_Fermer(conn);
        }
    }

    // Read
    public List<Livre> MdlLivre_GetAll() {
        PreparedStatement stmt = null;
        List<Livre> listeLivres = new ArrayList<Livre>();

        try {
            stmt = conn.prepareStatement(GET_ALL);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Livre livre = new Livre();
                livre.setNum(rs.getInt(1));
                livre.setTitre(rs.getString(2));
                livre.setAuteur(rs.getInt(3));
                livre.setAnnee(rs.getInt(4));
                livre.setPages(rs.getInt(5));
                livre.setCathegorie(rs.getString(6));

                listeLivres.add(livre);
            }
        } catch (SQLException e) {
            // e.printStackTrace();
            throw new RuntimeException(e);
        } finally {
            MdlLivre_Fermer(stmt);
           // MdlLivre_Fermer(conn);
        }

        return listeLivres;
    }

    public Livre MdlLivre_GetByID(int numLivre) {
        PreparedStatement stmt = null;

        try {

            stmt = conn.prepareStatement(GET_BY_ID);
            stmt.setInt(1, numLivre);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Livre livre = new Livre();
                livre.setNum(rs.getInt(1));
                livre.setTitre(rs.getString(2));
                livre.setAuteur(rs.getInt(3));
                livre.setAnnee(rs.getInt(4));
                livre.setPages(rs.getInt(5));
                livre.setCathegorie(rs.getString(6));

                return livre;
            } else {
                return null;
            }
        } catch (SQLException e) {
            // e.printStackTrace();
            throw new RuntimeException(e);
        } finally {
            MdlLivre_Fermer(stmt);
//            MdlLivre_Fermer(conn);
        }
    }

    public Livre MdlLivre_GetByTitre(String titre) {
        PreparedStatement stmt = null;

        try {
            stmt = conn.prepareStatement(GET_BY_TITRE);
            stmt.setString(1, titre);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Livre livre = new Livre();
                livre.setNum(rs.getInt(1));
                livre.setTitre(rs.getString(2));
                livre.setAuteur(rs.getInt(3));
                livre.setAnnee(rs.getInt(4));
                livre.setPages(rs.getInt(5));
                livre.setCathegorie(rs.getString(6));

                return livre;
            } else {
                return null;
            }
        } catch (SQLException e) {
            // e.printStackTrace();
            throw new RuntimeException(e);
        } finally {
            MdlLivre_Fermer(stmt);
            MdlLivre_Fermer(conn);
        }
    }

    // Update, faudrat avant appeler MdlF_GetById(idf) pour obtenir
    // les données du film à modifier via une interface et après envoyer 
    // ce film à MdlF_Modifier(film) pour faire la mise à  jour.
    public int MdlLivre_Modifier(Livre livre) {
        PreparedStatement stmt = null;
       
        try {
            stmt = conn.prepareStatement(MODIFIER);
            stmt.setInt(1, livre.getNum());
            stmt.setString(2, livre.getTitre());
            stmt.setInt(3, livre.getAuteur());
            stmt.setInt(4, livre.getAnnee());
            stmt.setInt(5, livre.getPages());
            stmt.setString(6, livre.getCathegorie());
            stmt.setInt(7, livre.getNum());
            return stmt.executeUpdate();
        } catch (SQLException e) {
            // e.printStackTrace();
            throw new RuntimeException(e);
        } finally {
            MdlLivre_Fermer(stmt);
            //MdlLivre_Fermer(conn);
        }
    }

    // Delete
    public int MdlLivre_Supprimer(int numLivre) {
        PreparedStatement stmt = null;

        try {
            stmt = conn.prepareStatement(SUPPRIMER);
            stmt.setInt(1, numLivre);

            return stmt.executeUpdate();
        } catch (SQLException e) {
            // e.printStackTrace();
            throw new RuntimeException(e);
        } finally {
            MdlLivre_Fermer(stmt);
           // MdlLivre_Fermer(conn);
        }
    }
   
    private static void MdlLivre_Fermer(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                // e.printStackTrace();
                throw new RuntimeException(e);
            }
        }
    }

    private static void MdlLivre_Fermer(Statement stmt) {
        if (stmt != null) {
            try {
                stmt.close();
            } catch (SQLException e) {
                // e.printStackTrace();
                throw new RuntimeException(e);
            }
        }
    }
    public Connection getConn() {
        return conn;
    }

}
