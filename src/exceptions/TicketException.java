package exceptions;

import java.sql.SQLException;

public class TicketException extends SQLException {

	public TicketException() {
	}

	public TicketException (String m) {
		super(m);
	}
}
