package utils;

import java.text.ParseException;
import java.util.Properties;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.IOException;
import java.sql.Connection;

import accesBD.BDCategories;
import accesBD.BDConnexion;

import modele.Utilisateur;
import modele.Categorie;
import exceptions.ExceptionUtilisateur;
import exceptions.ExceptionConnexion;
import exceptions.CategorieException;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

/**
 * les operations de l'application
 * 
 * @author fauvet
 * 
 */
public class Utilitaires {

	public Utilitaires() {
	}

	/**
	 * Affiche les categories du theatre avec pour chacune son prix
	 * 
	 * @param user
	 *            l'utilisateur identifie
	 * @throws ExceptionConnexion
	 * @throws IOException
	 */
	public static void AfficherCategories(Utilisateur user) throws IOException {
		List<Categorie> res = new LinkedList<Categorie>();
		try {
			System.out.println("===================");
			System.out.println("Listes des categories tarifaires");
			res = BDCategories.getCategorie(user);
			if (res.isEmpty()) {
				System.out.println(" Liste vide ");
			} else {
				for (int i = 0; i < res.size(); i++) {
					System.out.println(res.get(i).getCategorie() + " (prix : "
							+ res.get(i).getPrix() + ")");
				}
			}
			System.out.println("===================");
		} catch (CategorieException e) {
			System.out.println(" Erreur dans l'affichage des categories : "
					+ e.getMessage());
		} catch (ExceptionConnexion e) {
			System.out.println(" Erreur dans l'affichage des categories : "
					+ e.getMessage());
		}

	}

	/**
	 * effectue la connexion pour l'utilisateur
	 * 
	 * @return l'oid de l'objet utilisateur
	 * @throws ExceptionUtilisateur
	 */
	public static Utilisateur Identification() throws ExceptionConnexion,
			ExceptionUtilisateur, IOException {
		Utilisateur user = null;
		String login;
		String passwd;
		// lecture des parametres de connexion dans connection.conf
		Properties p = new Properties();
		InputStream is = null;
		is = new FileInputStream(utils.Constantes.Home + utils.Constantes.Config);
		p.load(is);
		login = p.getProperty("user");
		passwd = p.getProperty("mdp");
//		if (login == null || login.equals("MYUSERNAME")) {
//			UserNamePasswordDialog login_dialog = new UserNamePasswordDialog(
//					new Frame(""));
//			login_dialog.setVisible(true);
//			login = login_dialog.getUid();
//			passwd = login_dialog.getPwd();
//		}
		/* test de la connexion */
		Connection conn = BDConnexion.getConnexion(login, passwd);
		if (conn != null) {
			System.out.println("Connexion reussie...");
			BDConnexion.FermerTout(conn, null, null);
			user = new Utilisateur(login, passwd);
		} else {
			throw new ExceptionConnexion("Connexion impossible\n");
		}
		return user;
	}

	public static Date toDate(String date, String format) throws ParseException {
		return new Date(new SimpleDateFormat(format, Locale.ENGLISH).parse(date).getTime());
	}
	
	public static String toString(Date date) {
		SimpleDateFormat df = new SimpleDateFormat("dd MMMMMMMMMMM yyyy HH:mm");  
		return df.format(date);
	}
	
	public static String toStringBd(Date date) {
		SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy HH:mm");  
		return df.format(date);
	}
	
}
