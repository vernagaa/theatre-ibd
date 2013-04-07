package accesBD;

import exceptions.ExceptionConnexion;
import exceptions.PlaceException;
import exceptions.TicketException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.LinkedList;
import java.util.List;
import modele.Place;
import modele.Ticket;
import modele.Utilisateur;

public class BDTickets {

	public static Ticket reserver(Utilisateur user, Integer numS, Date dateR, String categorie)  throws TicketException, ExceptionConnexion {
		//TODO réservation
		return null;
	}
	

}
