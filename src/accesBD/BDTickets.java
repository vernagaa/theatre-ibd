package accesBD;

import exceptions.ExceptionConnexion;
import exceptions.TicketException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.LinkedList;
import java.util.List;
import modele.Categorie;
import modele.Place;
import modele.Reservation;
import modele.Ticket;
import modele.Utilisateur;

public class BDTickets {

	public static Ticket reserver(Utilisateur user, Integer numS, Date dateR, String categorie) throws TicketException, ExceptionConnexion {
		int noDossier;
		int noSerie;
		String r1, r2, r3, r4;
		Place place;
		Categorie cat;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		Connection conn = BDConnexion.getConnexion(user.getLogin(), user.getmdp());

		r1 = "select max(noDossier) from LesDossiers";
		r2 = "select max(noSerie) from LesTickets";
		r3 = "insert into LesDossiers values (?, ?)";
		r4 = "insert into LesTickets values (?, ?, ?, ?, ?, SysTimestamp, ?)";
		try {
			conn.setAutoCommit(false);
			cat = BDCategories.getCategorie(user, categorie);
			if (cat == null) {
				throw new TicketException("Catégorie inexistante");
			}
			// place libre
			place = BDPlaces.getPlaceLibre(user, numS, dateR, categorie);
			if (place == null) {
				throw new TicketException("Aucuine place libre");
			}

			// noDossier
			stmt = conn.prepareStatement(r1);
			rs = stmt.executeQuery();
			if (rs.next()) {
				noDossier = rs.getInt(1) + 1;
			} else {
				noDossier = 1;
			}

			// noSerie
			stmt = conn.prepareStatement(r2);
			rs = stmt.executeQuery();
			if (rs.next()) {
				noSerie = rs.getInt(1) + 1;
			} else {
				noSerie = 1;
			}

			// nouveau dossier
			stmt = conn.prepareStatement(r3);
			stmt.setInt(1, noDossier);
			stmt.setFloat(2, cat.getPrix());
			rs = stmt.executeQuery();

			// nouveau ticket
			stmt = conn.prepareStatement(r4);
			stmt.setInt(1, noSerie);
			stmt.setInt(2, numS);
			stmt.setTimestamp(3, new Timestamp(dateR.getTime()));
			stmt.setInt(4, place.getNoPlace());
			stmt.setInt(5, place.getNoRang());
			stmt.setInt(6, noDossier);
			rs = stmt.executeQuery();
			conn.commit();
		} catch (SQLException e) {
			BDConnexion.FermerTout(conn, stmt, rs);
			throw new TicketException(" Problème dans l'interrogation des Tickets.."
					+ "Code Oracle " + e.getErrorCode()
					+ "Message " + e.getMessage());
		}
		BDConnexion.FermerTout(conn, stmt, rs);
		return new Ticket(noSerie, numS, dateR, place.getNoPlace(), place.getNoRang(), null, noDossier);
	}

	public static List<Ticket> reserver(Utilisateur user, Integer numS, Date dateR, String categorie, int nbPlaces) throws TicketException, ExceptionConnexion {
		int noDossier;
		int noSerie;
		String r1, r2, r3, r4;
		List<Place> places;
		Categorie cat;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		List<Ticket> tickets = new LinkedList<Ticket>();
		Connection conn = BDConnexion.getConnexion(user.getLogin(), user.getmdp());

		r1 = "select max(noDossier) from LesDossiers";
		r2 = "select max(noSerie) from LesTickets";
		r3 = "insert into LesDossiers values (?, ?)";
		r4 = "insert into LesTickets values (?, ?, ?, ?, ?, SysTimestamp, ?)";
		try {
			conn.setAutoCommit(false);
			cat = BDCategories.getCategorie(user, categorie);
			if (cat == null) {
				throw new TicketException("Catégorie inexistante");
			}
			// place libre
			places = BDPlaces.getPlacesLibres(user, numS, dateR, categorie, nbPlaces);
			if (places.size() < nbPlaces) {
				throw new TicketException("Nombre de places libres insuffisant");
			}

			// noDossier
			stmt = conn.prepareStatement(r1);
			rs = stmt.executeQuery();
			if (rs.next()) {
				noDossier = rs.getInt(1) + 1;
			} else {
				noDossier = 1;
			}

			// noSerie
			stmt = conn.prepareStatement(r2);
			rs = stmt.executeQuery();
			if (rs.next()) {
				noSerie = rs.getInt(1) + 1;
			} else {
				noSerie = 1;
			}

			// nouveau dossier
			stmt = conn.prepareStatement(r3);
			stmt.setInt(1, noDossier);
			stmt.setFloat(2, cat.getPrix() * nbPlaces);
			rs = stmt.executeQuery();

			// nouveaux tickets
			Timestamp ts = new Timestamp(dateR.getTime());
			for (Place p : places) {
				stmt = conn.prepareStatement(r4);
				stmt.setInt(1, noSerie);
				stmt.setInt(2, numS);
				stmt.setTimestamp(3, ts);
				stmt.setInt(4, p.getNoPlace());
				stmt.setInt(5, p.getNoRang());
				stmt.setInt(6, noDossier);
				rs = stmt.executeQuery();

				tickets.add(new Ticket(noSerie, numS, dateR, p.getNoPlace(), p.getNoRang(), null, noDossier));
				noSerie++;
			}
			conn.commit();
		} catch (SQLException e) {
			BDConnexion.FermerTout(conn, stmt, rs);
			if (e instanceof TicketException) {
				throw (TicketException) e;
			} else {
				throw new TicketException(" Problème dans l'interrogation des Tickets.."
						+ "Code Oracle " + e.getErrorCode()
						+ "Message " + e.getMessage());
			}
		}
		BDConnexion.FermerTout(conn, stmt, rs);
		return tickets;
	}

	public static List<Ticket> reserver(Utilisateur user, Reservation r) throws TicketException, ExceptionConnexion {
		return reserver(user, r.getRepres().getSpectacle(), r.getRepres().getDate(), r.getCateg().getCategorie(), r.getNbPlaces());
	}
}
