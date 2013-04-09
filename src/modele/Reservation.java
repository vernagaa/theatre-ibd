package modele;

public class Reservation {

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

	public void addPlaces(int nbPlaces) {
		if (nbPlaces > 0) {
			this.nbPlaces += nbPlaces;
		} else {
			throw new IllegalArgumentException("Le nombre de places doit être strictement positif");
		}
	}

	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof Reservation))
			return false;

		Reservation obj2 = (Reservation) obj;
		return repres != null && obj2.repres != null
				&& categ != null && obj2.categ != null
				&& repres.equals(obj2.repres)
				&& categ.equals(obj2.categ);
	}
	
	
}
