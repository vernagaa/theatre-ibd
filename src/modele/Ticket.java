package modele;

import java.sql.Date;

public class Ticket {

	private int noSerie;
	private int numS;
	private Date dateRep;
	private int noPlace;
	private int noRang;
	private Date dateEmission;
	private int noDossier;

	public Ticket(int noSerie, int numS, Date dateRep, int noPlace, int noRang, Date dateEmission, int noDossier) {
		this.noSerie = noSerie;
		this.numS = numS;
		this.dateRep = dateRep;
		this.noPlace = noPlace;
		this.noRang = noRang;
		this.dateEmission = dateEmission;
		this.noDossier = noDossier;
	}

	public int getNoSerie() {
		return noSerie;
	}

	public void setNoSerie(int noSerie) {
		this.noSerie = noSerie;
	}

	public int getNumS() {
		return numS;
	}

	public void setNumS(int numS) {
		this.numS = numS;
	}

	public Date getDateRep() {
		return dateRep;
	}

	public void setDateRep(Date dateRep) {
		this.dateRep = dateRep;
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

	public Date getDateEmission() {
		return dateEmission;
	}

	public void setDateEmission(Date dateEmission) {
		this.dateEmission = dateEmission;
	}

	public int getNoDossier() {
		return noDossier;
	}

	public void setNoDossier(int noDossier) {
		this.noDossier = noDossier;
	}

}
