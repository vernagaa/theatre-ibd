package exceptions;

import java.sql.SQLException;

public class CategorieException extends SQLException {

	public CategorieException() {
	}

	public CategorieException (String m) {
		super(m);
	}
}
