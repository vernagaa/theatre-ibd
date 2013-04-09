package exceptions;

public class ExceptionUtilisateur extends ExceptionTheatre {

	public ExceptionUtilisateur() {
	}

	public ExceptionUtilisateur(String message) {
		super(message);
	}

	public ExceptionUtilisateur(Throwable cause) {
		super(cause);
	}

	public ExceptionUtilisateur(String message, Throwable cause) {
		super(message, cause);
	}

}
