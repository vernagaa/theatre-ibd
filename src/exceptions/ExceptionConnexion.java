package exceptions;

public class ExceptionConnexion extends ExceptionTheatre {

	public ExceptionConnexion() {
	}

	public ExceptionConnexion(String message) {
		super(message);
	}

	public ExceptionConnexion(Throwable cause) {
		super(cause);
	}

	public ExceptionConnexion(String message, Throwable cause) {
		super(message, cause);
	}

}
