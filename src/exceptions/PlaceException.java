package exceptions;

import java.sql.SQLException;

public class PlaceException extends SQLException {

	public PlaceException() {
	}

	public PlaceException (String m) {
		super(m);
	}
}
