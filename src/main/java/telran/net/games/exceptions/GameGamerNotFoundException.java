package telran.net.games.exceptions;

@SuppressWarnings("serial")
public class GameGamerNotFoundException extends IllegalArgumentException {
	public GameGamerNotFoundException(Long gameId, String username) {
		super("Not found gamer " + username + " in game " + gameId);
	}

}
