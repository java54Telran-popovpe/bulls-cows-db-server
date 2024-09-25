package telran.net.games.exceptions;

@SuppressWarnings("serial")
public class GameFinishedException extends IllegalStateException {
	public GameFinishedException(long gameId) {
		super(String.format("Illegal game state. Game %d is finished", gameId) );
	}
}
