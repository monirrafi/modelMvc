package Projet_livre.dao.vueLivre;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumnModel;

import Projet_livre.dao.controleurLivre.ControleurLivre;
import Projet_livre.dao.modelLivre.Livre;

import java.awt.*;
import java.io.*;
import java.util.*;
import java.awt.event.*;

public class VueLivre extends JFrame implements actionEvent{

/*============================================================================================================= */
/*										Declaration															*/
/*=============================================================================================================*/
	static JPanel contentPane = new JPanel();
	static JScrollPane scroll =new JScrollPane();
	private JTable table = new JTable();
	private ControleurLivre ctrlivre = ControleurLivre.getControleurLivre();

	static String nomFichier;
	static BufferedReader tmpReadTxt;
	static RandomAccessFile donnee;
	static String noAuteur=""; 

	JComboBox<String> cmbNumero =new JComboBox<>();
	JComboBox<String> cmbCathegorie = new  JComboBox<>();
	JComboBox<String> cmbAuteur = new  JComboBox<>();
	JComboBox<String>   champAuteur = new  JComboBox<>();
	static JButton btnLivres = new JButton("Afficher les livres");
	static JButton btnModifierTitre = new JButton("Modifier un titre");
	static JButton btnSuprimer = new JButton("Suprimer un livre");
	static JButton btnAjouter = new JButton("Ajouter un livre");
	static JButton btnQuitter = new JButton("Quitter");
	static JLabel lblSize;
	static GridBagConstraints gbc_tlBar;
	static 	JTableHeader entete;

/*============================================================================================================= */
/*										Constructeurs															*/
/*============================================================================================================= */
	public VueLivre() {
		ctrlivre.CtrLivre_ChargerDB();
		affichage();
		action();
		
	}
	
	public void affichage() {
		//ImageIcon logo = new ImageIcon(getClass().getResource("src\\main\\java\\Projet_livre\\dao\\images\\biblio.png"));
		//setIconImage(logo.getImage());
		contentPane = new JPanel();
		setTitle("Gestion de la bibliotheque");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(0, 0, 1550, 700);
		contentPane.setBorder(new EmptyBorder(5, 5, 1, 0));
		setContentPane(contentPane);
		GridBagLayout gbl_contentPane = new GridBagLayout();
		gbl_contentPane.columnWidths = new int[]{266, 62, 0};
		gbl_contentPane.rowHeights = new int[]{21, 0, 0, 0};
		gbl_contentPane.columnWeights = new double[]{1.0, 0.0, Double.MIN_VALUE};
		gbl_contentPane.rowWeights = new double[]{0.0, 1.0, 0.0, Double.MIN_VALUE};
		contentPane.setLayout(gbl_contentPane);

		table.setModel(imageTable());
		scroll = new JScrollPane(table);
		cmbNumero =new JComboBox<>(getListeCBox("num"));
		cmbCathegorie = new  JComboBox<>(getListeCBox("cathegorie"));
		cmbAuteur = new  JComboBox<>(getListeCBox("auteur"));
	
	
		btnStyle(btnLivres);
		btnStyle(btnAjouter);
		btnStyle(btnSuprimer);
		btnStyle(btnModifierTitre);
		btnStyle(btnQuitter);
		
		JToolBar tlBar = new JToolBar();
		
		tlBar.setToolTipText("Liste des livres");
		tlBar.setForeground(Color.BLACK);
		tlBar.setFont(new Font("Serif", Font.PLAIN, 16));
		tlBar.setBackground(Color.WHITE);
		gbc_tlBar = new GridBagConstraints();
		gbc_tlBar.insets = new Insets(0, 5, 10, 5);
		gbc_tlBar.anchor = GridBagConstraints.NORTHWEST;
		gbc_tlBar.gridx = 0;
		gbc_tlBar.gridy = 0;
		contentPane.add(tlBar, gbc_tlBar);
		
		//JLabel lblCath = new JLabel("Cathegorie");
		cmbCathegorie.setBackground(new Color(0,128,0));
		cmbCathegorie.setForeground(Color.white);
		cmbCathegorie.setFont( new Font("Serif", Font.BOLD, 20));
		cmbCathegorie.setOpaque(true);
		//JLabel lblNumero = new JLabel("Numero");
		cmbNumero.setBackground(new Color(123,104,238));
		cmbNumero.setForeground(Color.yellow);
		cmbNumero.setFont( new Font("Serif", Font.BOLD, 20));
		cmbNumero.setOpaque(true);
	
		cmbAuteur.setBackground(new Color(255,140,0));
		cmbAuteur.setForeground(Color.BLACK);
		cmbAuteur.setFont( new Font("Serif", Font.BOLD, 20));
		cmbAuteur.setOpaque(true);

		lblSize = new JLabel(" Le nombre des livres est " + calculerTaille() + " ");
		lblSize.setFont( new Font("Serif", Font.BOLD, 16)); 
		lblSize.setSize(new Dimension(350,20));

		
		tlBar.add(btnLivres);
		tlBar.add(btnModifierTitre);
		tlBar.add(btnAjouter);
		tlBar.add(btnSuprimer);

		tlBar.add(cmbCathegorie);
		//tlBar.add(lblCath);
		tlBar.add(cmbNumero);
		tlBar.add(cmbAuteur);
		//tlBar.add(lblNumero);
		tlBar.add(lblSize);
		tlBar.add(btnQuitter);
		
		scroll.setBackground(new Color(128,128,128));
		gbc_tlBar.gridwidth = 2;
		gbc_tlBar.fill = GridBagConstraints.BOTH;
		gbc_tlBar.gridx = 0;
		gbc_tlBar.gridy = 1;
		contentPane.add(scroll, gbc_tlBar);
	}

/*============================================================================================================= */
/*										Ecouetuers																*/
/*============================================================================================================= */

	public void actionBtn(ActionEvent ev){
		if(ev.getSource()== btnLivres){
			//DefaultTableModel model = remplirTable("","0");
			table.setModel(remplirTable("","0"));
			

		}else if(ev.getSource()== btnModifierTitre){
			modifierLivre();

		}else if(ev.getSource()== btnSuprimer){
			Suprimer();

		}else if(ev.getSource()== btnAjouter){
			ajouter();

		}else if(ev.getSource()== btnQuitter){
		
			System.exit(0);

		}
		styleTable(new Color(12,128,144), Color.white);
	}

	public void itemStateChanged(ItemEvent e) {
		if(e.getSource()== cmbCathegorie){
			DefaultTableModel model = remplirTable((String)cmbCathegorie.getSelectedItem(),"0");
			table.setModel(model);
			styleTable(new Color(0, 128, 0), Color.white);

		}else if(e.getSource()== cmbNumero){
			DefaultTableModel model = remplirTable("",(String)cmbNumero.getSelectedItem());
			table.setModel(model);
			styleTable(new Color(123,104,238), Color.yellow);
			
		}else if(e.getSource()== cmbAuteur){
			DefaultTableModel model = remplirTable("",(String)cmbAuteur.getSelectedItem());
			table.setModel(model);
			styleTable(new Color(255,140,0), Color.BLACK);
			
		}
	}
	@Override
	public void action() {
		cmbCathegorie.addItemListener(this::itemStateChanged);
		cmbNumero.addItemListener(this::itemStateChanged);
		cmbAuteur.addItemListener(this::itemStateChanged);
		//champAuteur.addItemListener(this::itemStateChanged);

		btnLivres.addActionListener(this::actionBtn);
		btnAjouter.addActionListener(this::actionBtn);
		btnModifierTitre.addActionListener(this::actionBtn);
		btnSuprimer.addActionListener(this::actionBtn);
		btnQuitter.addActionListener(this::actionBtn);
		
	}

/*============================================================================================================= */
/*										S-A-R     															    */
/*=============================================================================================================*/
public void Suprimer() {
	String strCle = JOptionPane.showInputDialog(null, "Entrez le numéro du livre à modifier");
	int cle= Integer.parseInt(strCle);
	//Livre livreSprimer = new Livre();

	if(ctrlivre.CtrLivre_GetById(cle)==null){
			JOptionPane.showMessageDialog(null, "le livre du numero "+ cle +" n' existe pas!!");
			
	}else{
		ctrlivre.CtrLivre_Enlever(cle);
		DefaultComboBoxModel<String> modelAuteur = new DefaultComboBoxModel<>(getListeCBox("auteur"));
		cmbAuteur.removeAll();
		cmbAuteur.setModel(modelAuteur);

		DefaultComboBoxModel<String> modelNum = new DefaultComboBoxModel<>(getListeCBox("num"));
		cmbNumero.removeAll();
		cmbNumero.setModel(modelNum);

		DefaultComboBoxModel<String> modelCath = new DefaultComboBoxModel<>(getListeCBox("cathegorie"));
		cmbCathegorie.removeAll();
		cmbCathegorie.setModel(modelCath);
		lblSize.setText(" Le nombre des livres est " + calculerTaille() + " ");
		//sauvgarder();
		JOptionPane.showMessageDialog(null,"le livre du numero "+ cle + " est suprimer avec succès");
		DefaultTableModel modelTable = remplirTable("","0");
		table.setModel(modelTable);

	}
	
}
public void ajouter() {
	String strCle = JOptionPane.showInputDialog(null, "Entrez le numéro du livre a ajouter");
	int cle= Integer.parseInt(strCle);
	if(ctrlivre.CtrLivre_GetById(cle) != null){
			JOptionPane.showMessageDialog(null, "le livre du numéro "+ cle +"  existe déjà!!");
			
	}else{
		ArrayList<String> data = new ArrayList<String>(){{add(strCle);add(null);add(null);add(null);add(null);add(null);}};
		String[] retour = paneString(data,new ArrayList<String>(){{add("Numéro");add("Titre");add("Année");add("Pages");}},"                         Entrez les informations du votre nouveau livre");
		if (retour != null){
			Livre livre = new Livre(Integer.parseInt(retour[0]),
			retour[1],
			Integer.parseInt(retour[4]),
			Integer.parseInt(retour[2]),
			Integer.parseInt(retour[3]),
			retour[5]);
			ctrlivre.CtrLivre_Enregistrer(livre);
			DefaultComboBoxModel<String> modelAuteur = new DefaultComboBoxModel<>(getListeCBox("auteur"));
			cmbAuteur.removeAll();
			cmbAuteur.setModel(modelAuteur);

			DefaultComboBoxModel<String> modelNum = new DefaultComboBoxModel<>(getListeCBox("num"));
			cmbNumero.removeAll();
			cmbNumero.setModel(modelNum);

			DefaultComboBoxModel<String> modelCath = new DefaultComboBoxModel<>(getListeCBox("cathegorie"));
			cmbCathegorie.removeAll();
			cmbCathegorie.setModel(modelCath);
			lblSize.setText(" Le nombre des livres est " + calculerTaille() + " ");

			//sauvgarder();
			DefaultTableModel modelTable = remplirTable("",String.valueOf(cle));
			table.setModel(modelTable);
		}	
	}
//	sauvgarder();

}
public void modifierLivre() {
	//ArrayList<Livre> listeLivres = remplirArrayliste();
	String strCle = JOptionPane.showInputDialog(null, "Entrez le numéro du livre a modifier");
	int cle= Integer.parseInt(strCle);
	Livre livre = ctrlivre.CtrLivre_GetById(cle);
	if(livre == null){
			JOptionPane.showMessageDialog(null, "le livre du numéro "+ cle +" n' existe pas!!");
			
	}else{
		
		ArrayList<String> data = new ArrayList<String>(){{add(strCle);add(livre.getTitre());
			add(String.valueOf(livre.getAnnee()));add(String.valueOf(livre.getPages()));
			add(String.valueOf(livre.getAuteur()));add(livre.getCathegorie());}};
		String[] retour = paneString(data,new ArrayList<String>(){{add("Numéro");add("Titre");add("Année");add("Pages");}},"                         Entrez les informations du votre nouveau livre");
		if (retour != null){
			Livre livreModifier = new Livre(Integer.parseInt(retour[0]),
			retour[1],
			Integer.parseInt(retour[4]),
			Integer.parseInt(retour[2]),
			Integer.parseInt(retour[3]),
			retour[5]);
			ctrlivre.CtrLivre_Modifier(livreModifier);
		}
		DefaultComboBoxModel<String> modelAuteur = new DefaultComboBoxModel<>(getListeCBox("auteur"));
		cmbAuteur.removeAll();
		cmbAuteur.setModel(modelAuteur);

		DefaultComboBoxModel<String> modelNum = new DefaultComboBoxModel<>(getListeCBox("num"));
		cmbNumero.removeAll();
		cmbNumero.setModel(modelNum);

		DefaultComboBoxModel<String> modelCath = new DefaultComboBoxModel<>(getListeCBox("cathegorie"));
		cmbCathegorie.removeAll();
		cmbCathegorie.setModel(modelCath);
		lblSize.setText(" Le nombre des livres est " + calculerTaille() + " ");

		//sauvgarder();
		DefaultTableModel modelTable = remplirTable("",String.valueOf(cle));
		table.setModel(modelTable);
	}
//	sauvgarder();

}
public String[] paneString(ArrayList<String> data,ArrayList<String> listeChamps,String titre) {
	String[] retour = new String[6];
			Dimension d =new Dimension(350,20);
			Color cl = new Color(102,178,255);
			ArrayList<JTextField> listeJtxt = new ArrayList<>();

			JPanel panePrincipal = new JPanel(new GridBagLayout());
			JPanel gPane = new JPanel(new GridLayout(listeChamps.size()+2,1,0,5));
			GridBagConstraints c = new GridBagConstraints();	
			JLabel lblTitre = new JLabel(titre);
			lblTitre.setFont(new Font("Serif", Font.BOLD, 20));
			lblTitre.setForeground(Color.blue);
				ButtonGroup groupeWeb = new ButtonGroup();
				gPane.add(lblTitre);
				for(int i=0;i<listeChamps.size();i++){
					JPanel pane = new JPanel();
					JTextField jtxt = new JTextField(data.get(i));
					jtxt.setPreferredSize(d);
					JLabel lbl = new JLabel(listeChamps.get(i));
					lbl.setPreferredSize(new Dimension(50,20));
					lbl.setLabelFor(jtxt);
					listeJtxt.add(jtxt);
					pane.add(lbl);
					pane.add(jtxt);
					gPane.add(pane);
	
				}
				c.weightx = 0.0;
				c.gridx = 0;
				c.gridy = 0;
				c.gridwidth=1;
				panePrincipal.add(gPane,c);
				
				if(listeChamps.size()>2){	
		
				JLabel lblChoix = new JLabel("                    Choisissez une cathegorie ");
				JPanel paneRadio = new JPanel(new GridLayout(2,1,0,5));
				paneRadio.setBackground(cl);
				JPanel paneElementradio = new JPanel();
				paneElementradio.setBackground(cl);
				JRadioButton vide = new JRadioButton("");
				
				vide.setBackground(cl);
				groupeWeb.add(vide);
				paneElementradio.add(vide);
				String[] listeCathegorie = getListeCBox("cathegorie");
				int longueur = listeCathegorie.length;
				String choixBtnRadio = data.get(5);
				for(int i=1;i<longueur;i++){
					JRadioButton rBtn = new JRadioButton(listeCathegorie[i]);
					if(listeCathegorie[i].equals(choixBtnRadio)){
						rBtn.setSelected(true);

					}
					rBtn.setBackground(cl);
					groupeWeb.add(rBtn);
					paneElementradio.add(rBtn);
				}
				//remplir choix radioBoutton
				if(choixBtnRadio == null || choixBtnRadio.equals("") || choixBtnRadio.equals(" ") ){
					vide.setSelected(true);
				}

				paneRadio.add(lblChoix);
				paneRadio.add(paneElementradio);
				paneRadio.setPreferredSize(new Dimension(300,10));
				c.ipadx = 150;      
				c.ipady = 50;      
				c.weightx = 0.0;
				c.gridx = 0;
				c.gridy = 1;
				c.gridwidth=2;
				panePrincipal.add(paneRadio,c);
				JPanel paneAuteur = new JPanel();
				champAuteur = new  JComboBox<>(getListeCBox("auteur"));
				if(data.get(4) != null && !data.get(4).equals("")){
					champAuteur.setSelectedItem(data.get(4));
				}
				champAuteur.setPreferredSize(d);
				champAuteur.addActionListener(new ActionListener() {     
					@Override
					public void actionPerformed(ActionEvent e) {
					   noAuteur = champAuteur.getSelectedItem().toString();      
					}
				  });
				JLabel lblAuteur = new JLabel("Auteur");
				lblAuteur.setPreferredSize(new Dimension(50,20));
				lblAuteur.setLabelFor(champAuteur);
				paneAuteur.add(lblAuteur);
				paneAuteur.add(champAuteur);
				gPane.add(paneAuteur);
				}	

	
			int res = JOptionPane.showConfirmDialog(null,panePrincipal,"Modification Livre",JOptionPane.YES_NO_CANCEL_OPTION);
			if(res == JOptionPane.YES_OPTION){
				for(int i=0;i<listeJtxt.size();i++){
					retour[i]= listeJtxt.get(i).getText();
				}
				if(noAuteur.equals("")){
					retour[listeJtxt.size()] = "0";

				}else{
					retour[listeJtxt.size()] = noAuteur;
				}
				Enumeration<AbstractButton> allRadioButton=groupeWeb.getElements();  
				while(allRadioButton.hasMoreElements())  
				{  
				   JRadioButton temp=(JRadioButton)allRadioButton.nextElement();  
				   if(temp.isSelected())  
				   { 
						retour[listeJtxt.size()+1]= temp.getText();  
				   }  
				}            
				
			}else{
				retour = null;
			}  
		//}
	
	
	return retour;        

}

/*============================================================================================================= */
/*										Fonctions																*/
/*=============================================================================================================*/
public int calculerTaille() {
	return ctrlivre.CtrLivre_GetAll().size();
}

public DefaultTableModel remplirTable(String entree,String strCle) {
	ArrayList<Livre> listeLivres = (ArrayList<Livre>) ctrlivre.CtrLivre_GetAll(); 	
	String[] column = {"Numero","Titre","Numero Auteur","Annee","Nombre des pages","Cathegorie"};
	DefaultTableModel model = new DefaultTableModel(column,0);
	if(entree.equals("Cathegorie") || strCle.equals("Numero Auteur") || strCle.equals("Numero Livre") ){
		for(Livre livre:listeLivres){
			model.addRow(new Object[]{livre.getNum(),livre.getTitre(),livre.getAuteur(),livre.getAnnee(),livre.getPages(),livre.getCathegorie()});				
		}

	}else{
		int cle = Integer.parseInt(strCle);
		if(cle==0){
			if(entree.equals("")){
				for(Livre livre:listeLivres){
					model.addRow(new Object[]{livre.getNum(),livre.getTitre(),livre.getAuteur(),livre.getAnnee(),livre.getPages(),livre.getCathegorie()});				
				}
			}else{
				for(Livre livre:listeLivres){
					String str = livre.getCathegorie();
					if(entree.equals(str)){
						model.addRow(new Object[]{livre.getNum(),livre.getTitre(),livre.getAuteur(),livre.getAnnee(),livre.getPages(),livre.getCathegorie()});				

					}
				}
			}

		}else{
			for(Livre livre:listeLivres){
				int num = livre.getNum();
				if(cle ==num){
					model.addRow(new Object[]{livre.getNum(),livre.getTitre(),livre.getAuteur(),livre.getAnnee(),livre.getPages(),livre.getCathegorie()});				

				}
			}
			for(Livre livre:listeLivres){
				int num = livre.getAuteur();
				if(cle ==num){
					model.addRow(new Object[]{livre.getNum(),livre.getTitre(),livre.getAuteur(),livre.getAnnee(),livre.getPages(),livre.getCathegorie()});				

				}
			}
		}
	}
	return model;

}

public  String[] getListeCBox(String choix){

		//chargerLivres();
		String[] retour =new String[1];
		ArrayList<String>  liste = new ArrayList<String>();
		ArrayList<String>  listeTmp = new ArrayList<String>();
		for(Livre livre:ctrlivre.CtrLivre_GetAll()){		
					if(choix.equals("cathegorie")) {
						liste.add(livre.getCathegorie());
					}else if(choix.equals("auteur")) {
						liste.add(String.valueOf(livre.getAuteur()));
					}else  if(choix.equals("num")) {
						liste.add(String.valueOf(livre.getNum()));
					}
		}
		//enlever les doublants
		if(liste.size() != 0){
			listeTmp.add(liste.get(0));
			for(String current:liste){
				if(listeTmp.indexOf(current)==-1){
					listeTmp.add(current);
				}
			}
			//les premiers elements de la liste deroulante
			retour = new String[listeTmp.size()+1];
			if(choix.equals("cathegorie")) {
				retour[0]="Choisissez Cathegorie";
			}else if(choix.equals("auteur")) {
				retour[0]="Choisissez Auteur";
			}else if(choix.equals("num")) {
				retour[0]="Choisissez Livre";
			}
			//le tableau sans doublant
			for(int i=0;i<listeTmp.size();i++){
				retour[i+1]=listeTmp.get(i);
			}
		}
		return retour;
	
	}

public DefaultTableModel imageTable() {
	entete = table.getTableHeader();
	entete.setFont(new Font("Serif", Font.BOLD, 20));
	entete.setBackground(Color.orange);//new Color(128,128,128));//new Color(105,105,105));
	entete.setForeground(Color.BLACK);

	String[] column = {"Bienvenue à la gestion d'une bibliothéque "};
	table.setRowHeight(558);
	DefaultTableModel model = new DefaultTableModel(column,0)
	{
		
		public Class getColumnClass(int column)
		{
			switch (column)
			{
				case 0: return Icon.class;
				default: return super.getColumnClass(column);
			}
		}
	};

	ImageIcon img =  new ImageIcon("src\\images\\livre2.jpg");
	model.addRow(new Object[]{img});

	return model;	
}
public void btnStyle(JButton btn){
	btn.setSize(new Dimension(200,20));
	btn.setBackground(new Color(12,128,144));		
	btn.setForeground(Color.white);
	btn.setFont( new Font("Serif", Font.BOLD, 18));
	btn.setOpaque(true);

}

public void styleTable(Color bgColor,Color pColor) {
	JTableHeader entete = table.getTableHeader();
	entete.setFont(new Font("Serif", Font.BOLD, 18));
	entete.setBackground(new Color(128,128,128));//new Color(105,105,105));
	entete.setForeground(Color.white);
	TableColumnModel columnModelEntete = entete.getColumnModel();
	columnModelEntete.getColumn(0).setPreferredWidth(5);
	columnModelEntete.getColumn(1).setPreferredWidth(400);
	columnModelEntete.getColumn(2).setPreferredWidth(5);
	columnModelEntete.getColumn(3).setPreferredWidth(5);
	columnModelEntete.getColumn(4).setPreferredWidth(5);
	columnModelEntete.getColumn(5).setPreferredWidth(100);
	TableColumnModel columnModel = table.getColumnModel();
	columnModel.getColumn(0).setPreferredWidth(5);
	columnModel.getColumn(1).setPreferredWidth(400);
	columnModel.getColumn(2).setPreferredWidth(5);
	columnModel.getColumn(3).setPreferredWidth(5);
	columnModel.getColumn(4).setPreferredWidth(5);
	columnModel.getColumn(5).setPreferredWidth(100);
	table.setBackground(bgColor);
	table.setForeground(pColor);
	table.setRowHeight(20);
	table.setFont(new Font("Serif", Font.BOLD, 18));
	table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
}

}

