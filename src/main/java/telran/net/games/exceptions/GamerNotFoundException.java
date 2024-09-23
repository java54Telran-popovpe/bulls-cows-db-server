package telran.net.games.exceptions;

import java.util.NoSuchElementException;

@SuppressWarnings("serial")
public class GamerNotFoundException extends NoSuchElementException {
	public GamerNotFoundException(String username) {
		super("Already exists gamer " + username);
	}

}
