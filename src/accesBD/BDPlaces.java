package accesBD;

import exceptions.ExceptionConnexion;
import exceptions.PlaceException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.LinkedList;
import java.util.List;
import modele.Place;
import modele.Utilisateur;

public class BDPlaces {
	
	public static List<Place> getPlacesLibres(Utilisateur user, int numS, Date dateRep) throws PlaceException, ExceptionConnexion {
		LinkedList<Place> res = new LinkedList<Place>();
		String requete ;
		PreparedStatement stmt ;
		ResultSet rs ;
		Connection conn = BDConnexion.getConnexion(user.getLogin(), user.getmdp());

		requete = "select noPlace, noRang, nomC "
				+ "from LesZones natural join LesPlaces "
				+ "where (noPlace, noRang) "
				+ "not in ( "
				+ "select noPlace, noRang "
				+ "from LesTickets "
				+ "where dateRep=? "
				+ "and numS=? "
				+ ") "
				+ "order by noRang, noPlace";
		try {
			stmt = conn.prepareStatement(requete);
			stmt.setTimestamp(1, new Timestamp(dateRep.getTime()));
			stmt.setInt(2, numS);
			
			rs = stmt.executeQuery();
			while (rs.next()) {
				res.add(new Place (rs.getInt(1), rs.getInt(2), rs.getString(3)));
			}
		} catch (SQLException e) {
			throw new PlaceException (" Problème dans l'interrogation des Places.."
					+ "Code Oracle " + e.getErrorCode()
					+ "Message " + e.getMessage());
		}
		BDConnexion.FermerTout(conn, stmt, rs);
		return res;
	}

	public static List<Place> getPlacesLibres(Utilisateur user, int numS, Date dateRep, String nomC) throws PlaceException, ExceptionConnexion {
		LinkedList<Place> res = new LinkedList<Place>();
		String requete ;
		PreparedStatement stmt ;
		ResultSet rs ;
		Connection conn = BDConnexion.getConnexion(user.getLogin(), user.getmdp());

		requete = "select noPlace, noRang "
				+ "from LesZones natural join LesPlaces "
				+ "where (noPlace, noRang) "
				+ "not in ( "
				+ "select noPlace, noRang "
				+ "from LesTickets "
				+ "where dateRep=? "
				+ "and numS=? "
				+ "and nomC=? "
				+ ") "
				+ "and nomC=? "
				+ "order by noRang, noPlace";
		try {
			stmt = conn.prepareStatement(requete);
			stmt.setTimestamp(1, new Timestamp(dateRep.getTime()));
			stmt.setInt(2, numS);
			stmt.setString(3, nomC);
			stmt.setString(4, nomC);
			
			rs = stmt.executeQuery();
			while (rs.next()) {
				res.add(new Place (rs.getInt(1), rs.getInt(2), nomC));
			}
		} catch (SQLException e) {
			throw new PlaceException (" Problème dans l'interrogation des Places.."
					+ "Code Oracle " + e.getErrorCode()
					+ "Message " + e.getMessage());
		}
		BDConnexion.FermerTout(conn, stmt, rs);
		return res;
	}

	public static int getNbPlacesLibres(Utilisateur user, int numS, Date dateRep, String nomC) throws PlaceException, ExceptionConnexion {
		int res = 0;
		String requete ;
		PreparedStatement stmt ;
		ResultSet rs ;
		Connection conn = BDConnexion.getConnexion(user.getLogin(), user.getmdp());

		requete = "select count(*) "
				+ "from LesZones natural join LesPlaces "
				+ "where (noPlace, noRang) "
				+ "not in ( "
				+ "select noPlace, noRang "
				+ "from LesTickets "
				+ "where dateRep=? "
				+ "and numS=? "
				+ "and nomC=? "
				+ ") "
				+ "and nomC=? "
				+ "order by noRang, noPlace";
		try {
			stmt = conn.prepareStatement(requete);
			stmt.setTimestamp(1, new Timestamp(dateRep.getTime()));
			stmt.setInt(2, numS);
			stmt.setString(3, nomC);
			stmt.setString(4, nomC);
			
			rs = stmt.executeQuery();
			if (rs.next()) {
				res = rs.getInt(1);
			}
		} catch (SQLException e) {
			throw new PlaceException (" Problème dans l'interrogation des Places.."
					+ "Code Oracle " + e.getErrorCode()
					+ "Message " + e.getMessage());
		}
		BDConnexion.FermerTout(conn, stmt, rs);
		return res;
	}

}
