package telran.net.games.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import telran.net.games.entities.Game;
import telran.net.games.exceptions.GameAlreadyStartedException;
import telran.net.games.exceptions.GameFinishedException;
import telran.net.games.exceptions.GameGamerNotFoundException;
import telran.net.games.exceptions.GameNotFoundException;
import telran.net.games.exceptions.GameNotStartedException;
import telran.net.games.exceptions.GamerNotFoundException;
import telran.net.games.exceptions.IncorrectMoveSequenceException;
import telran.net.games.exceptions.NoGamerInGameException;
import telran.net.games.model.MoveData;
import telran.net.games.model.MoveDto;
import telran.net.games.repo.BullsCowsRepository;

public class BullsCowsServiceImpl implements BullsCowsService {
	private BullsCowsRepository bcRepository;
	private BullsCowsGameRunner bcRunner;
	
	
	public BullsCowsServiceImpl(BullsCowsRepository bcRepository, BullsCowsGameRunner bcRunner) {
		this.bcRepository = bcRepository;
		this.bcRunner = bcRunner;
	}
	@Override
	/**
	 * Creates new game
	 * returns ID of the created game
	 */
	public long createGame() {
		return bcRepository.createNewGame(bcRunner.getRandomSequence());
	}
	@Override
	/**
	 * starts game
	 * returns list of gamers (user names)
	 * exceptions:
	 * GameNotFoundException
	 * GameAlreadyStartedException
	 * NoGamerInGameException
	 */
	public List<String> startGame(long gameId) {
		if(bcRepository.isGameStarted(gameId)) {
			throw new GameAlreadyStartedException(gameId);
		}
		List<String> result = bcRepository.getGameGamers(gameId);
		if (result.isEmpty()) {
			throw new NoGamerInGameException(gameId);
		}
		bcRepository.setStartDate(gameId, LocalDateTime.now());
		return result;
	}
	@Override
	/**
	 * adds new gamer
	 * Exceptions:
	 * GamerAlreadyExistsException
	 */
	public void registerGamer(String username, LocalDate birthDate) {
		bcRepository.createNewGamer(username, birthDate);
	}
	@Override
	/**
	 * join a given gamer to a given game
	 * Exceptions:
	 * GameNotFoundException
	 * GameAlreadyStartedException
	 * GamerNotFoundException
	 */
	public void gamerJoinGame(long gameId, String username) {
		if(bcRepository.isGameStarted(gameId)) {
			throw new GameAlreadyStartedException(gameId);
		}
		bcRepository.createGameGamer(gameId, username);
	}
	@Override
	/**
	 * returns list of ID's for not started games
	 * no exceptions (empty list is allowed)
	 */
	public List<Long> getNotStartedGames() {
		return bcRepository.getGameIdsNotStarted();
	}
	@Override
	/**
	 * returns all objects of MoveData of a given game and given gamer
	 * including the last with the given parameters
	 * in the case of the winner's move the game should be set as finished
	 * and the gamer in the game should be set as the winner
	 * Exceptions:
	 * IncorrectMoveSequenceException
	 * GameNotFoundException
	 * GamerNotFoundException
	 * GameNotStartedException
	 * GameFinishedException
	 */
	public List<MoveData> moveProcessing(String sequence, long gameId, String username) {
		checkMakingMovePossibility(sequence, gameId, username);
		MoveData moveData = bcRunner.moveProcessing(sequence, bcRepository.getGame(gameId).getSequence());
		
		if ( bcRunner.checkGameFinished(moveData) ) {
			bcRepository.setIsFinished(gameId);
			bcRepository.setWinner(gameId, username);
		}
			
		bcRepository.createGameGamerMove(new MoveDto(gameId, username, sequence, moveData.bulls(), moveData.cows()));
		
		return bcRepository.getAllGameGamerMoves(gameId, username);
	}
	private void checkMakingMovePossibility(String sequence, long gameId, String username) {
		if ( bcRepository.getGame(gameId) == null ) {
			throw new GameNotFoundException(gameId);
		}
		
		if ( bcRepository.getGamer(username) == null ) {
			throw new GamerNotFoundException(username);
		}
		
		if ( !bcRepository.getGameGamers(gameId).contains(username) ) {
			throw new GameGamerNotFoundException(gameId, username);
		}
		
		if ( !bcRunner.checkGuess(sequence) ) {
			throw new IncorrectMoveSequenceException(sequence);
		}
		if ( !bcRepository.isGameStarted(gameId)) {
			throw new GameNotStartedException(gameId);
		}
		if ( bcRepository.isGameFinished(gameId)) {
			throw new GameFinishedException(gameId);
		}
		
	}
	@Override
	/**
	 * returns true if game is finished
	 * Exceptions:
	 * GameNotFoundException
	 */
	public boolean gameOver(long gameId) {
		return bcRepository.isGameFinished(gameId);
	}
	@Override
	/**
	 * returns list of gamers in a given game
	 * Exceptions: 
	 * GameNotFoundException
	 */
	public List<String> getGameGamers(long gameId) {
		return bcRepository.getGameGamers(gameId);
	}
	/**
	 * Only for testing
	 * Implied that the test class resides at the same package (to access the method)
	 * 
	 * @param gameId
	 * @return To be guessed sequence
	 * No Exceptions, that is implied that at the test gameId exists
	 */
	String getSequence(long gameId) {
		Game game = bcRepository.getGame(gameId);
		return game.getSequence();
	}
	
}
