package telran.net.games;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import telran.net.games.entities.Game;
import telran.net.games.entities.Gamer;
import telran.net.games.exceptions.GamerNotFoundException;
import telran.net.games.model.MoveData;
import telran.net.games.model.MoveDto;
import telran.net.games.repo.BullsCowsRepository;
import telran.net.games.repo.BullsCowsRepositoryJpa;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class RepositoryTest {
	static BullsCowsRepository repository;
	static {
		HashMap<String, Object> hibernateProperties = new HashMap<>();
		hibernateProperties.put("hibernate.hbm2ddl.auto", "create");
		hibernateProperties.put("hibernate.show_sql", "true");
		hibernateProperties.put("hibernate.format_sql", "true");
		repository = new BullsCowsRepositoryJpa(new BullsCowsTestPersistenceUnitInfo(), hibernateProperties);
	}
	static long gameId;
	static String gamerUsername = "gamer1";
	@Test
	@Order(10)
	void createGameTest() {
		gameId = repository.createNewGame("1234");
		Game game = repository.getGame(gameId);
		assertNotNull(game);
		assertNull(game.getDate());
		assertFalse(game.isfinished());
		
	}
	@Test
	@Order(20)
	void createGamerTest() {
		repository.createNewGamer(gamerUsername, LocalDate.of(2000, 1, 1));
		Gamer gamer = repository.getGamer(gamerUsername);
		assertNotNull(gamer);
		assertIterableEquals(List.of(), repository.getUnfinishedGamesBasedOnUser(gamerUsername,true, true));
		assertIterableEquals(List.of(gameId), repository.getUnfinishedGamesBasedOnUser(gamerUsername,true, false));
		assertIterableEquals(List.of(), repository.getUnfinishedGamesBasedOnUser(gamerUsername,false, false));
		
	}
	
	@Order(30)
	@Test
	void createGameGamerTest() {
		repository.createGameGamer(gameId, gamerUsername);
		List<String> gamers = repository.getGameGamers(gameId);
		assertEquals(1, gamers.size());
		assertEquals(gamerUsername, gamers.get(0));
		assertIterableEquals(List.of(gameId), repository.getUnfinishedGamesBasedOnUser(gamerUsername,true, true));
		assertIterableEquals(List.of(), repository.getUnfinishedGamesBasedOnUser(gamerUsername,false, true));
		assertIterableEquals(List.of(), repository.getUnfinishedGamesBasedOnUser(gamerUsername,true, false));
		
	}
	
	@Order(50)
	@Test
	void isGameStartedTest() {
		assertFalse(repository.isGameStarted(gameId));
	}
	@Order(60)
	@Test
	void setStartDateTest() {
		repository.setStartDate(gameId, LocalDateTime.now());
		assertTrue(repository.isGameStarted(gameId));
		assertIterableEquals(List.of(), repository.getUnfinishedGamesBasedOnUser(gamerUsername,true, true));
		assertIterableEquals(List.of(gameId), repository.getUnfinishedGamesBasedOnUser(gamerUsername,false, true));
		assertIterableEquals(List.of(), repository.getUnfinishedGamesBasedOnUser(gamerUsername,false, false));
	}
	
	@Order(70)
	@Test
	void createGameGamerMoveAllGameGamersMovesTest() {
		repository.createGameGamerMove(new MoveDto(gameId, gamerUsername, "1243", 2, 2));
		repository.createGameGamerMove(new MoveDto(gameId, gamerUsername, "1234", 4, 0));
		
		List<MoveData> moves = repository.getAllGameGamerMoves(gameId, gamerUsername);
		assertEquals(new MoveData("1243", 2, 2), moves.get(0));
		assertEquals(new MoveData("1234", 4, 0), moves.get(1));
	}
	@Order(80)
	@Test
	void isGameFinishedTest() {
		assertFalse(repository.isGameFinished(gameId));
	}
	@Order(90)
	@Test
	void setIsFinishedTest() {
		repository.setIsFinished(gameId);
		assertTrue(repository.isGameFinished(gameId));
		assertIterableEquals(List.of(), repository.getUnfinishedGamesBasedOnUser(gamerUsername,true, true));
		assertIterableEquals(List.of(), repository.getUnfinishedGamesBasedOnUser(gamerUsername,false, true));
		assertIterableEquals(List.of(), repository.getUnfinishedGamesBasedOnUser(gamerUsername,false, false));
	}
	@Order(100)
	@Test
	void isWinnerTest() {
		assertFalse(repository.isWinner(gameId, gamerUsername));
	}
	@Order(110)
	@Test
	void setWinnerTest() {
		repository.setWinner(gameId, gamerUsername);
		assertTrue(repository.isWinner(gameId, gamerUsername));
	}
	
	
	


}
