package accesBD;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import exceptions.CategorieException;
import exceptions.ExceptionConnexion;

import java.util.LinkedList;
import java.util.List;
import modele.Categorie;
import modele.Utilisateur;

public class BDCategories {

	public BDCategories () {
		
	}
	/**
	 * retourne la liste des catégories définies dans la bd
	 * @param Utilisateur
	 * @return Vector<Categorie>
	 * @throws CategorieException
	 * @throws ExceptionConnexion
	 */
	public static List<Categorie> getCategorie (Utilisateur user)
	throws CategorieException, ExceptionConnexion {
		List<Categorie> res = new LinkedList<Categorie>();
		String requete ;
		Statement stmt ;
		ResultSet rs ;
		Connection conn = BDConnexion.getConnexion(user.getLogin(), user.getmdp());
		
		requete = "select nomc, prix from LesCategories order by nomc";
		try {
			stmt = conn.createStatement();
			rs = stmt.executeQuery(requete);
			while (rs.next()) {
				res.add(new Categorie (rs.getString(1), rs.getFloat(2)));
			}
		} catch (SQLException e) {
			throw new CategorieException (" Problème dans l'interrogation des catégories.."
					+ "Code Oracle " + e.getErrorCode()
					+ "Message " + e.getMessage());
		}
		BDConnexion.FermerTout(conn, stmt, rs);
		return res;
	}
}
