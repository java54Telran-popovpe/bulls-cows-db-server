package telran.net.games.service;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import javax.sound.midi.Sequence;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import telran.net.games.BullsCowsTestPersistenceUnitInfo;
import telran.net.games.entities.Game;
import telran.net.games.entities.Gamer;
import telran.net.games.exceptions.GameAlreadyStartedException;
import telran.net.games.exceptions.GameFinishedException;
import telran.net.games.exceptions.GameNotStartedException;
import telran.net.games.exceptions.GamerNotFoundException;
import telran.net.games.exceptions.IncorrectMoveSequenceException;
import telran.net.games.exceptions.NoGamerInGameException;
import telran.net.games.model.MoveData;
import telran.net.games.repo.BullsCowsRepository;
import telran.net.games.repo.BullsCowsRepositoryJpa;
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class BullsCowsServiceTest {
	private static final int N_DIGITS = 4;
	private static final int N_CORRECT_MOVES = 5;
	static BullsCowsRepository repository;
	static BullsCowsService bcService;
	static BullsCowsGameRunner bcRunner;
	static {
		HashMap<String, Object> hibernateProperties = new HashMap<>();
		hibernateProperties.put("hibernate.hbm2ddl.auto", "create");
		repository = new BullsCowsRepositoryJpa
				(new BullsCowsTestPersistenceUnitInfo(), hibernateProperties);
		bcRunner = new BullsCowsGameRunner(N_DIGITS);
		bcService = new BullsCowsServiceImpl(repository, bcRunner);
		
	}
	//TODO Tests
	//access the to be guessed sequence -
	// ((BullsCowsServiceImpl)bcService).getSequence(gameId)
	static long gameId;
	static String gamerUsername = "gamer1";
	static String gameSequence;
	@Test
	@Order(10)
	void createGameTest() {
		gameId = bcService.createGame();
		gameSequence = ((BullsCowsServiceImpl)bcService).getSequence(gameId);
		gameNotStarted();
	}

	private void gameNotStarted() {
		assertIterableEquals(List.of(gameId), bcService.getNotStartedGames());
		assertFalse(bcService.gameOver(gameId));
	}
	
	private void gameStarted() {
		assertEquals(0, bcService.getNotStartedGames().size());
		assertFalse(bcService.gameOver(gameId));
	}
	
	private void gameFinished() {
		assertEquals(0, bcService.getNotStartedGames().size());
		assertTrue(bcService.gameOver(gameId));
	}
	
	@Test
	@Order(20)
	void registerGamerTest() {
		bcService.registerGamer(gamerUsername, LocalDate.of(1990, 4, 8));
		Gamer gamer = repository.getGamer(gamerUsername);
		assertNotNull(gamer);
	}
	
	@Test
	@Order(30)
	void startGameWithoutGamerTest() {
		assertThrowsExactly(NoGamerInGameException.class, () -> bcService.startGame(gameId));
		gameNotStarted();
	}
	
	@Test
	@Order(40)
	void joinGamerTest() {
		assertEquals(0, bcService.getGameGamers(gameId).size());
		bcService.gamerJoinGame(gameId, gamerUsername);
		assertIterableEquals(List.of(gamerUsername),bcService.getGameGamers(gameId) );
	}
	@Test
	@Order(45)
	void getNotStartedGamesWithGamerTestBeforeGameStart() {
		assertIterableEquals(List.of(gameId), bcService.getNotStartedGamesWithGamer(gamerUsername));
		assertThrowsExactly(GamerNotFoundException.class, () -> bcService.getNotStartedGamesWithGamer(gamerUsername + "1"));
	}
	
	@Test
	@Order(50)
	void makeMoveInNonstartedGameTest() {
		assertThrowsExactly( GameNotStartedException.class,
				() -> bcService.moveProcessing(getNonWinningSequence(), gameId, gamerUsername));
	}
	
	@Test
	@Order(60)
	void startGameTest() {
		bcService.startGame(gameId);
		gameStarted();
	}
	@Test
	@Order(65)
	void getNotStartedGamesWithGamerTestAfterGameStart() {
		assertIterableEquals(List.of(), bcService.getNotStartedGamesWithGamer(gamerUsername));
	}
	@Test
	@Order(70)
	void startAlreadyStartedGameTest() {
		assertThrowsExactly(GameAlreadyStartedException.class, () -> bcService.startGame(gameId));
		
	}
	
	@Test
	@Order(80)
	void movesTest() {
		List<MoveData> moveDataList = new LinkedList<>();
		for ( int i = 0; i < N_CORRECT_MOVES; i++) {
			String sequence = getNonWinningSequence();
			moveDataList.add(bcRunner.moveProcessing(sequence, gameSequence));
			assertIterableEquals(moveDataList, bcService.moveProcessing(sequence, gameId, gamerUsername));
			gameStarted();
		}
		assertThrowsExactly(IncorrectMoveSequenceException.class,
				() -> bcService.moveProcessing(gameSequence.concat("a"), gameId, gamerUsername));
		
		moveDataList.add(bcRunner.moveProcessing(gameSequence, gameSequence));
		assertIterableEquals(moveDataList, bcService.moveProcessing(gameSequence, gameId, gamerUsername));
		gameFinished();
		
		assertThrowsExactly(GameFinishedException.class,
				() -> bcService.moveProcessing(gameSequence, gameId, gamerUsername));
	}
	
	private String getNonWinningSequence() {
		String result = null;
		do {
			result = bcRunner.getRandomSequence();
		} while ( result.equals(gameSequence));
		return result;
	}
	
	
	
	

}