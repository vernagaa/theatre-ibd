package exceptions;

import java.sql.SQLException;

public class SpectacleException extends SQLException {

	public SpectacleException() {
		// TODO Auto-generated constructor stub
	}

	public SpectacleException (String m) {
		super(m);
	}
}
