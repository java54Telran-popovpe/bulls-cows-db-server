package telran.net.games.exceptions;

@SuppressWarnings("serial")
public class GamerAlreadyExistsException extends IllegalStateException {
	public GamerAlreadyExistsException(String username) {
		super(String.format("Gamer %s already exists", username));
	}

}
