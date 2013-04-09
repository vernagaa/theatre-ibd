package accesBD;

import exceptions.ExceptionConnexion;
import exceptions.RepresentationException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.LinkedList;
import java.util.List;
import modele.Representation;
import modele.Utilisateur;

public class BDRepresentations {

	/**
	 * retourne la liste des catégories définies dans la bd
	 * @param Utilisateur
	 * @return LinkedList<Representation>
	 * @throws RepresentationException
	 * @throws ExceptionConnexion
	 */
	public static List<Representation> getRepresentation(Utilisateur user) throws RepresentationException, ExceptionConnexion {
		LinkedList<Representation> res = new LinkedList<Representation>();
		String requete;
		Statement stmt;
		ResultSet rs;
		Connection conn = BDConnexion.getConnexion(user.getLogin(), user.getmdp());

		requete = "select numS, DateRep from LesRepresentations order by DateRep";
		try {
			stmt = conn.createStatement();
			rs = stmt.executeQuery(requete);
			while (rs.next()) {
				res.add(new Representation(rs.getInt(1), rs.getTimestamp(2)));
			}
		} catch (SQLException e) {
			throw new RepresentationException(" Problème dans l'interrogation des représentations.."
					+ "Code Oracle " + e.getErrorCode()
					+ "Message " + e.getMessage());
		}
		BDConnexion.FermerTout(conn, stmt, rs);
		return res;
	}

	public static Representation getRepresentation(Utilisateur user, int numS, Date dateRep) throws RepresentationException, ExceptionConnexion {
		Representation res = null;
		String requete;
		PreparedStatement stmt;
		ResultSet rs;
		Connection conn = BDConnexion.getConnexion(user.getLogin(), user.getmdp());

		requete = "select numS, DateRep from LesRepresentations where numS=? AND DateRep=?";
		try {
			stmt = conn.prepareStatement(requete);
			stmt.setInt(1, numS);
			stmt.setTimestamp(2, new Timestamp(dateRep.getTime()));
			
			rs = stmt.executeQuery();
			if (rs.next()) {
				res = new Representation(rs.getInt(1), rs.getTimestamp(2));
			}
		} catch (SQLException e) {
			throw new RepresentationException(" Problème dans l'interrogation des représentations.."
					+ "Code Oracle " + e.getErrorCode()
					+ "Message " + e.getMessage());
		}
		BDConnexion.FermerTout(conn, stmt, rs);
		return res;
	}

	public static List<Representation> getRepresentations(Utilisateur user, int numS) throws ExceptionConnexion, RepresentationException {
		LinkedList<Representation> res = new LinkedList<Representation>();
		String requete;
		PreparedStatement stmt;
		ResultSet rs;
		Connection conn = BDConnexion.getConnexion(user.getLogin(), user.getmdp());

		requete = "select numS, DateRep from LesRepresentations where numS=?";
		try {
			stmt = conn.prepareStatement(requete);
			stmt.setInt(1, numS);
			
			rs = stmt.executeQuery();
			while (rs.next()) {
				res.add(new Representation(rs.getInt(1), rs.getTimestamp(2)));
			}
		} catch (SQLException e) {
			throw new RepresentationException(" Problème dans l'interrogation des représentations.."
					+ "Code Oracle " + e.getErrorCode()
					+ "Message " + e.getMessage());
		}
		BDConnexion.FermerTout(conn, stmt, rs);
		return res;
	}
	
	public static void addRepresentation(Utilisateur user, int numS, Date dateRep) throws ExceptionConnexion, RepresentationException, SQLException {
		String requete;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		Connection conn = BDConnexion.getConnexion(user.getLogin(), user.getmdp());
		conn.setAutoCommit(false);

		requete = "insert into LesRepresentations values (?, ?)";
		try {
			stmt = conn.prepareStatement(requete);
			stmt.setInt(1, numS);
			stmt.setTimestamp(2, new Timestamp(dateRep.getTime()));
			
			rs = stmt.executeQuery();
			conn.commit();
		} catch (SQLException e) {
			BDConnexion.FermerTout(conn, stmt, rs);
			throw new RepresentationException(" Problème dans l'ajout de la représentation.."
					+ "Code Oracle " + e.getErrorCode()
					+ "Message " + e.getMessage());
		}
		BDConnexion.FermerTout(conn, stmt, rs);
	}
}
