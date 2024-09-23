package telran.net.games.exceptions;

@SuppressWarnings("serial")
public class GameGamerAlreadyExistsException extends IllegalStateException {
	public GameGamerAlreadyExistsException( long gameId, String username) {
		super("Gamer " + username + " already exists in game " + gameId);
	}

}
