package telran.net.games.exceptions;

@SuppressWarnings("serial")
public class GamerAlreadyExistsException extends IllegalArgumentException {
	public GamerAlreadyExistsException(String username) {
		super("Not found gamer " + username);
	}

}
