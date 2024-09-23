package telran.net.games;

import javax.sql.DataSource;

import com.zaxxer.hikari.HikariDataSource;


public class BullsCowsTestPercictenceUnitInfo extends BullsCowsPersistenceUnitInfo {
	public DataSource getNonJtaDataSource() {
		HikariDataSource ds = new HikariDataSource();
		ds.setJdbcUrl("jdbc:h2:mem:testdb");
		ds.setPassword("");
		ds.setUsername("sa");
		ds.setDriverClassName("org.h2.Driver");
		
		return ds;
	}
}
