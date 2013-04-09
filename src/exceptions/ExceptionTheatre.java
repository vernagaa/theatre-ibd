package exceptions;

public class ExceptionTheatre extends Exception {

	public ExceptionTheatre() {
	}

	public ExceptionTheatre(String message) {
		super(message);
	}

	public ExceptionTheatre(Throwable cause) {
		super(cause);
	}

	public ExceptionTheatre(String message, Throwable cause) {
		super(message, cause);
	}

}
