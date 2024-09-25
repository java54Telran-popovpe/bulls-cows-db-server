package telran.net.games.exceptions;

@SuppressWarnings("serial")
public class GameNotStartedException extends IllegalStateException {
	public GameNotStartedException(long gameId) {
		super(String.format("Illegal game state. Game %d didn't started yet", gameId));
	}
}
