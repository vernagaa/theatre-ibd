package modele;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

public class Caddie {

	public static class Reservation {

		private Representation repres;
		private Categorie categ;
		private int nbPlaces;

		public Reservation(Representation repres, Categorie categ, int nbPlaces) {
			this.repres = repres;
			this.categ = categ;
			this.nbPlaces = nbPlaces;
		}

		public Categorie getCateg() {
			return categ;
		}

		public void setCateg(Categorie categ) {
			this.categ = categ;
		}

		public int getNbPlaces() {
			return nbPlaces;
		}

		public void setNbPlaces(int nbPlaces) {
			this.nbPlaces = nbPlaces;
		}

		public Representation getRepres() {
			return repres;
		}

		public void setRepres(Representation repres) {
			this.repres = repres;
		}

		public float getPrixTotal() {
			return nbPlaces * categ.getPrix();
		}
	}
	private Set<Reservation> resa = new HashSet<Reservation>();

	public Caddie() {
		//TODO supprimer caddie initialisé
		addReservation(new Representation(104, new Timestamp(584565)), new Categorie("categname", 25), 5);
	}

	
	public void addReservation(Representation r, Categorie c, int nbPlaces) {
		resa.add(new Reservation(r, c, nbPlaces));
	}

	public Set<Reservation> getReservations() {
		return resa;
	}
}
