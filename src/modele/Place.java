package modele;

public class Place {

	int noPlace;
	int noRang;
	String categorie;

	public Place(int noPlace, int noRang, String categorie) {
		this.noPlace = noPlace;
		this.noRang = noRang;
		this.categorie = categorie;
	}

	public String getCategorie() {
		return categorie;
	}

	public void setCategorie(String categorie) {
		this.categorie = categorie;
	}

	public int getNoPlace() {
		return noPlace;
	}

	public void setNoPlace(int noPlace) {
		this.noPlace = noPlace;
	}

	public int getNoRang() {
		return noRang;
	}

	public void setNoRang(int noRang) {
		this.noRang = noRang;
	}
	
	
	
}
