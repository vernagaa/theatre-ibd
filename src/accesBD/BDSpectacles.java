package accesBD;

import exceptions.ExceptionConnexion;
import exceptions.SpectacleException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.LinkedList;
import java.util.List;
import modele.Spectacle;
import modele.Utilisateur;

public class BDSpectacles {

	/**
	 * retourne la liste des catégories définies dans la bd
	 * @param Utilisateur
	 * @return LinkedList<Spectacle>
	 * @throws SpectacleException
	 * @throws ExceptionConnexion
	 */
	public static List<Spectacle> getSpectacle (Utilisateur user) throws SpectacleException, ExceptionConnexion {
		LinkedList<Spectacle> res = new LinkedList<Spectacle>();
		String requete ;
		Statement stmt ;
		ResultSet rs ;
		Connection conn = BDConnexion.getConnexion(user.getLogin(), user.getmdp());
		
		requete = "select numS, nomS from LesSpectacles order by nomS";
		try {
			stmt = conn.createStatement();
			rs = stmt.executeQuery(requete);
			while (rs.next()) {
				res.add(new Spectacle (rs.getInt(1), rs.getString(2)));
			}
		} catch (SQLException e) {
			throw new SpectacleException (" Problème dans l'interrogation des spectacles.."
					+ "Code Oracle " + e.getErrorCode()
					+ "Message " + e.getMessage());
		}
		BDConnexion.FermerTout(conn, stmt, rs);
		return res;
	}
	
	public static Spectacle getSpectacle(Utilisateur user, int specId) throws SpectacleException, ExceptionConnexion {
		Spectacle res = null;
		String requete ;
		Statement stmt ;
		ResultSet rs ;
		Connection conn = BDConnexion.getConnexion(user.getLogin(), user.getmdp());
		
		requete = "select numS, nomS from LesSpectacles where numS="+ specId;
		try {
			stmt = conn.createStatement();
			rs = stmt.executeQuery(requete);
			if (rs.next()) {
				res = new Spectacle (rs.getInt(1), rs.getString(2));
			}
		} catch (SQLException e) {
			throw new SpectacleException (" Problème dans l'interrogation des spectacles.."
					+ "Code Oracle " + e.getErrorCode()
					+ "Message " + e.getMessage());
		}
		BDConnexion.FermerTout(conn, stmt, rs);
		return res;
	}

	public static List<Spectacle> getSpectacle(Utilisateur user, Date date) throws SpectacleException, ExceptionConnexion {
		LinkedList<Spectacle> res = new LinkedList<Spectacle>();
		String requete ;
		PreparedStatement stmt ;
		ResultSet rs ;
		Connection conn = BDConnexion.getConnexion(user.getLogin(), user.getmdp());
		requete = "select s.numS, nomS from LesSpectacles s, LesRepresentations r "
				+ "where s.numS=r.numS"
				+ " AND dateRep=? order by nomS";
		try {
			stmt = conn.prepareStatement(requete);
			stmt.setTimestamp(1, new Timestamp(date.getTime()));
			
			rs = stmt.executeQuery();
			while (rs.next()) {
				res.add(new Spectacle (rs.getInt(1), rs.getString(2)));
			}
		} catch (SQLException e) {
			throw new SpectacleException (" Problème dans l'interrogation des spectacles.."
					+ "Code Oracle " + e.getErrorCode()
					+ "Message " + e.getMessage());
		}
		BDConnexion.FermerTout(conn, stmt, rs);
		return res;
	}

}
