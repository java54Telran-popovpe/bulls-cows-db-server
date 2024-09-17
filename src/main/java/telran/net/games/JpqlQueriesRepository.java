package telran.net.games;
import java.time.*;
import java.util.List;

import jakarta.persistence.*;

public class JpqlQueriesRepository {
	
	private EntityManager em;

	public JpqlQueriesRepository(EntityManager em) {
		this.em = em;
	}
	
	public List<Game> getGamesFinished(boolean isFinished) {
		
		TypedQuery<Game> query = em.createQuery(
				"select game from Game game where is_finished = ?1",Game.class);
		List<Game> res = query.setParameter(1, isFinished).getResultList();
		return res;
	}
	
	public List<DateTimeSequence> getDateTimeSequence( LocalTime time) {
		TypedQuery<DateTimeSequence> query = em.createQuery(
				"select date, sequence from Game where cast(date as time) < :time", DateTimeSequence.class);
		List<DateTimeSequence> result = query.setParameter("time", time ).getResultList();
		return result;
	}
	
	public List<Integer> getBullsInMovesGamersBornAfter(LocalDate afterDate) {
		TypedQuery<Integer> query = em.createQuery(
				"select bulls from Move where gameGamer.gamer.birthdate > ?1", Integer.class);
		List<Integer> result = query.setParameter(1, afterDate).getResultList();
		return result;
	}
	
	public List<MinMaxAmount> getDistrubutionGamesMoves(int interval) {
		TypedQuery<MinMaxAmount> query = em.createQuery(
				"select floor(game_moves / :interval ) * :interval as min_moves," +
				"floor(game_moves / :interval ) * : interval + :interval - 1 as max_moves," +
						"count(*) as amount " +
				"from " +
				" select count(*) as game_moves from Move group by gameGamer.game.id ) group by min_moves order by min_moves",
				MinMaxAmount.class);
		List<MinMaxAmount> res = query.setParameter("interval", interval).getResultList();
		return res;
	}
	
	public List<Game> getGamesWithAverageGamerAgeGreaterThen( int age ) {
//		select * from game where id in (select game_id
//				from game_gamer
//				join gamer on gamer_id=username
//				group by game_id
//				having avg(extract(year from
//				age(birthdate))) > 60);
		String jpqlQuery = 	"SELECT game " +
							"FROM Game game " +
							"WHERE game.id IN (" +
								"SELECT gameGamer.game.id " +
								"FROM GameGamer gameGamer " +
								"GROUP BY gameGamer.game " +
								"HAVING AVG((CURRENT_DATE - ( gameGamer.gamer.birthdate)) by YEAR) > ?1" +
							")";
		return em.createQuery(jpqlQuery, Game.class).setParameter(1, age).getResultList();
	}
	
	public List<GameAndMoves> getGameAndMovesOfWinnerInLessThen(int movesNumber) {
//-- 2. display game_id and number of moves made by winner of games with number of moves made by winner less than 5
//-- select game_id, count(*)as moves
//-- from game_gamer
//-- join move on game_gamer.id=game_gamer_id
//-- where is_winner
//-- group by game_id having count(*) < 5
		String jpqlQuery = 	"SELECT gameGamer.game.id, count(*) " +
							"FROM Move move " + 
							"WHERE gameGamer.is_winner " +
							"GROUP BY gameGamer.game.id " +
							"HAVING count(*) < ?1";
		TypedQuery<GameAndMoves> query = em.createQuery(jpqlQuery, GameAndMoves.class);
		return query.setParameter(1, movesNumber).getResultList();
		
	}
	public List<String> getGamerNameMadeMovesLessThen( int movesNumber) {
	//-- 3. display gamer names who made in one game number of moves less than 4
	//-- select distinct gamer_id
	//-- from game_gamer
	//-- join move on game_gamer.id=game_gamer_id 
	//-- group by game_id, gamer_id having count(*) < 4
		String jpqlQuery = 	"SELECT DISTINCT gameGamer.gamer.username " +
							"FROM Move move " + 
							"GROUP BY gameGamer.game.id, gameGamer.gamer.username " +
							"HAVING count(*) < ?1";
		TypedQuery<String> query = em.createQuery(jpqlQuery, String.class);
		return query.setParameter(1, movesNumber).getResultList();
		
	}
	
	public List<GameAndAvgMoves> getGamesWithAvgMovesNumber() {
	//-- 4. display game_id and average number of moves made by each gamer. Example in game 100000 there are three gamers (gamer1, gamer2, gamer3)
	//-- select game_id, round(avg(moves), 1) from (select game_id, gamer_id, count(*) moves
	//-- from game_gamer
	//-- join move
	//-- on game_gamer.id=game_gamer_id 
	//-- group by game_id, gamer_id
	//-- order by game_id)
	//-- group by game_id
		String jpqlQuery = 		"SELECT gameId, AVG(moves) " +
								"FROM " +
								"(SELECT gameGamer.game.id gameId, gameGamer.gamer.username userId, count(*) moves " +
								"FROM Move move " +
								"GROUP BY gameGamer.game.id, gameGamer.gamer.username " +
								"ORDER BY gameGamer.game.id ) sq " +
								"GROUP BY gameId";
		TypedQuery<GameAndAvgMoves> query = em.createQuery(jpqlQuery, GameAndAvgMoves.class);
		return query.getResultList();
		
	}

}


