package exceptions;

import java.sql.SQLException;

public class RepresentationException extends SQLException {

	public RepresentationException() {
		// TODO Auto-generated constructor stub
	}

	public RepresentationException (String m) {
		super(m);
	}
}
