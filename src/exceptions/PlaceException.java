package exceptions;

import java.sql.SQLException;

public class PlaceException extends SQLException {

	public PlaceException() {
		// TODO Auto-generated constructor stub
	}

	public PlaceException (String m) {
		super(m);
	}
}
