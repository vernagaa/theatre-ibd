package exceptions;

import java.sql.SQLException;

public class TicketException extends SQLException {

	public TicketException() {
		// TODO Auto-generated constructor stub
	}

	public TicketException (String m) {
		super(m);
	}
}
