package telran.net.games.repo;

import java.time.*;
import java.util.*;

import org.hibernate.jpa.*;

import jakarta.persistence.*;
import jakarta.persistence.spi.*;
import telran.net.games.entities.Game;
import telran.net.games.entities.GameGamer;
import telran.net.games.entities.Gamer;
import telran.net.games.entities.Move;
import telran.net.games.exceptions.GameGamerAlreadyExistsException;
import telran.net.games.exceptions.GameGamerNotFoundException;
import telran.net.games.exceptions.GameNotFoundException;
import telran.net.games.exceptions.GamerAlreadyExistsException;
import telran.net.games.exceptions.GamerNotFoundException;
import telran.net.games.model.MoveData;
import telran.net.games.model.MoveDto;

public class BullsCowsRepositoryJpa implements BullsCowsRepository {
	
	private EntityManager em;
	
	public BullsCowsRepositoryJpa( PersistenceUnitInfo persistenceUnitInfo, HashMap<String, Object> hibernateProperties) {
		EntityManagerFactory emf = new HibernatePersistenceProvider()
				.createContainerEntityManagerFactory(persistenceUnitInfo, hibernateProperties);
		em = emf.createEntityManager();
	}

	@Override
	public Game getGame(long id) {
		Game game = em.find(Game.class, id);
		if ( game == null ) {
			throw new GameNotFoundException(id);
		}
		return game;
	}

	@Override
	public Gamer getGamer(String username) {
		Gamer gamer = em.find(Gamer.class, username);
		if ( gamer == null) {
			throw new GamerNotFoundException(username);
		}
		return gamer;
	}

	@Override
	public long createNewGame(String sequence) {
		Game game = new Game(null, false, sequence);
		createObject(game);
		return game.getId();
	}

	private <T> void createObject(T obj) {
		EntityTransaction transaction = em.getTransaction();
		try {
			transaction.begin();
			em.persist(obj);
			transaction.commit();
		} catch (Exception e) {
			transaction.rollback();
			throw e;
		}
	}
	
	@Override
	public void createNewGamer(String username, LocalDate birthdate) {
		try {
			Gamer gamer = new Gamer(username, birthdate);
			createObject(gamer);
		} catch (Exception e) {
			throw new GamerAlreadyExistsException(username);
		}

	}

	@Override
	public boolean isGameStarted(long id) {
		Game game = getGame(id);
		return game.getDate() != null;
	}

	@Override
	public void setStartDate(long gameId, LocalDateTime dateTime) {
		EntityTransaction transaction = em.getTransaction();
		transaction.begin();
		Game game = getGame(gameId);
		game.setDate(dateTime);
		transaction.commit();
	}

	@Override
	public boolean isGameFinished(long id) {
		Game game = getGame(id);
		return game.isfinished();
	}

	@Override
	public void setIsFinished(long gameId) {
		EntityTransaction transaction = em.getTransaction();
		transaction.begin();
		Game game = getGame(gameId);
		game.setfinished(true);;
		transaction.commit();

	}

	@Override
	public List<Long> getGameIdsNotStarted() {
		TypedQuery<Long> query = em.createQuery(
				"select id from Game where dateTime is null", Long.class);
		return query.getResultList();
	}

	@Override
	public List<String> getGameGamers(long id) {
		TypedQuery<String> query = em.createQuery("select gamer.username from GameGamer where game.id = ?1", String.class);
		return query.setParameter(1, id).getResultList();
	}

	@Override
	public void createGameGamer(long gameId, String username) {
		
			Game game = getGame(gameId);
			Gamer gamer = getGamer(username);
		try {
			GameGamer gameGamer = new GameGamer(false, game, gamer);
			createObject(gameGamer);
		} catch (Exception e) {
			throw new GameGamerAlreadyExistsException(gameId, username);	
		}
	}

	@Override
	public void createGameGamerMove(MoveDto moveDto) {
		Move move = new Move(moveDto.sequence(), moveDto.bulls(), moveDto.cows(), 
				getGameGamer(moveDto.gameId(), moveDto.username()));
		createObject(move);

	}
	
	private GameGamer getGameGamer(long id, String username ) {
		TypedQuery<GameGamer> query = em.createQuery(
				"select gg from GameGamer gg where game.id=?1 and gamer.username=?2", GameGamer.class);
		List<GameGamer> list = query.setParameter(1, id).setParameter(2, username).getResultList();
		if ( list.isEmpty() )
			throw new GameGamerNotFoundException(id, username);
		return list.get(0);
	}

	@Override
	public List<MoveData> getAllGameGamerMoves(long gameId, String username) {
		TypedQuery<MoveData> query = em.createQuery(
				"select sequence, bulls, cows from Move where gameGamer.game.id=?1 and gameGamer.gamer.username=?2",
				MoveData.class);
		return query.setParameter(1, gameId).setParameter(2, username).getResultList();
	}

	@Override
	public void setWinner(long gameId, String username) {
		EntityTransaction transaction = em.getTransaction();
		transaction.begin();
		GameGamer gameGamer = getGameGamer(gameId, username);
		gameGamer.setWinner(true);
		transaction.commit();
	}

	@Override
	public boolean isWinner(long gameId, String username) {
		GameGamer gameGamer = getGameGamer(gameId, username);
		return gameGamer.isWinner();
	}

}
