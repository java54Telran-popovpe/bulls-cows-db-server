package telran.net.games.exceptions;

import java.util.NoSuchElementException;

@SuppressWarnings("serial")
public class GamerNotFoundException extends NoSuchElementException {
	public GamerNotFoundException(String username) {
		super(String.format("Gamer with username %s not found", username));
	}

}
