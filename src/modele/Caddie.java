package modele;

import java.util.ArrayList;
import java.util.List;

public class Caddie {

	private List<Reservation> resa = new ArrayList<Reservation>();

	public Reservation addReservation(Representation r, Categorie c, int nbPlaces) {
		Reservation res = new Reservation(r, c, nbPlaces);
		if (resa.contains(res)) {
			resa.get(resa.indexOf(res)).addPlaces(nbPlaces);
		} else {
			resa.add(res);
		}
		return res;
	}

	public List<Reservation> getReservations() {
		return resa;
	}
}
