package modele;

import java.sql.Date;
import java.sql.Timestamp;

public class Representation {

	private int spectacle;
//	private Timestamp date;
	private Date date;

	public Representation(int spectacle, Timestamp date) {
//		java.util.Date d = new SimpleDateFormat("dd-MMM-yy", Locale.ENGLISH).parse(dateS);
//		return new Date(d.getTime());
		this.spectacle = spectacle;
		this.date = new Date(date.getTime());
	}

	public int getSpectacle() {
		return spectacle;
	}

	public void setSpectacle(int spectacle) {
		this.spectacle = spectacle;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof Representation))
			return false;
		
		Representation obj2 = (Representation)obj;
		return spectacle == obj2.spectacle && date.equals(obj2.date);
	}
}
