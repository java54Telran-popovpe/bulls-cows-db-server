package telran.net.games.exceptions;

import java.util.NoSuchElementException;

@SuppressWarnings("serial")
public class GameGamerNotFoundException extends NoSuchElementException {
	public GameGamerNotFoundException(Long gameId, String username) {
		super("Not found gamer " + username + " in game " + gameId);
	}

}
