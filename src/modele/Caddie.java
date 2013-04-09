package modele;

import java.util.ArrayList;
import java.util.List;

public class Caddie {

	private List<Reservation> resa = new ArrayList<Reservation>();

	public Reservation addReservation(Representation r, Categorie c, int nbPlaces) {
		Reservation res = new Reservation(r, c, nbPlaces);
		int i = resa.indexOf(res);
		if (i != -1) {
			resa.get(i).addPlaces(nbPlaces);
		} else {
			resa.add(res);
		}
		return res;
	}

	public List<Reservation> getReservations() {
		return resa;
	}

	public Reservation getReservation(Representation repres, Categorie categ) {
		Reservation res = new Reservation(repres, categ, 0);
		int i = resa.indexOf(res);
		return i == -1 ? null : resa.get(i);
	}

	public void removeReservation(Representation repres, Categorie categ) {
		removeReservation(new Reservation(repres, categ, 0));
	}

	public void removeReservation(Reservation res) {
		resa.remove(res);
	}

	public void removeReservations(List<Reservation> valide) {
		for (Reservation r : valide) {
			removeReservation(r);
		}
	}
}
