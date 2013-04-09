package exceptions;

import java.sql.SQLException;

public class RepresentationException extends SQLException {

	public RepresentationException() {
	}

	public RepresentationException (String m) {
		super(m);
	}
}
