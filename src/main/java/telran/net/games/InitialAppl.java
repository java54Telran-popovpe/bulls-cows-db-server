package telran.net.games;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;

import org.hibernate.jpa.HibernatePersistenceProvider;

import jakarta.persistence.*;
public class InitialAppl {

	public static void main(String[] args) {
		HashMap<String, Object> map = new HashMap<>();
		map.put("hibernate.hbm2ddl.auto", "update");//using existing table
		map.put("hibernate.show_sql", true);
		map.put("hibernate.format_sql", true);
		EntityManagerFactory emFactory = new HibernatePersistenceProvider()
				.createContainerEntityManagerFactory(new BullsCowsPersistenceUnitInfo(), map);
		EntityManager em = emFactory.createEntityManager();
		JpqlQueriesRepository repository = new JpqlQueriesRepository(em);
//		List<Game> games = repository.getGamesFinished(false);
//		displayResult(games);
//		List<DateTimeSequence> list =
//				repository.getDateTimeSequence(LocalTime.of(12, 0));
//		displayResult(list);
//		List<Integer> list =
//				repository.getBullsInMovesGamersBornAfter(LocalDate.ofYearDay(2000, 1));
//		displayResult(list);
		List<MinMaxAmount> list = 
				repository.getDistributionGamesMoves(6);
		displayResult(list);

	}

	private static <T >void displayResult(List<T> list) {
		list.forEach(System.out::println);
		
	}

}
