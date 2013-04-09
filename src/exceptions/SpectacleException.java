package exceptions;

import java.sql.SQLException;

public class SpectacleException extends SQLException {

	public SpectacleException() {
	}

	public SpectacleException (String m) {
		super(m);
	}
}
