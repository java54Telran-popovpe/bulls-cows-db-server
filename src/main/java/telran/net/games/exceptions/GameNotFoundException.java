package telran.net.games.exceptions;

import java.util.NoSuchElementException;

@SuppressWarnings("serial")
public class GameNotFoundException extends NoSuchElementException {
	public GameNotFoundException(Long gameId) {
		super("Not found game " + gameId);
	}

}
