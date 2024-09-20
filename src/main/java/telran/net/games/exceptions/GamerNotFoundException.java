package telran.net.games.exceptions;

@SuppressWarnings("serial")
public class GamerNotFoundException extends IllegalArgumentException {
	public GamerNotFoundException(String username) {
		super("Already exists gamer " + username);
	}

}
