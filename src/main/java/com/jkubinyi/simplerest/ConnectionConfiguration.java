package com.jkubinyi.simplerest;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.jkubinyi.simplepool.PoolConfiguration;
import com.jkubinyi.simplepool.database.ConnectionPool;
import com.jkubinyi.simplepool.database.ConnectionPoolConfiguration;
import com.jkubinyi.simplepool.database.JDBCUrl;
import com.jkubinyi.simplepool.database.dialect.MySqlDialect;

@Configuration
public class ConnectionConfiguration {
	
	@Bean(destroyMethod = "close")
	public ConnectionPool createConnectionPool() {
		ConnectionPoolConfiguration connectionConfig = 
				new ConnectionPoolConfiguration.Builder(
						JDBCUrl.from(new MySqlDialect(), "jdbc:mysql://localhost:3305/springTest?useSSL=false", "root", "")
				)
				.addFallbackUrl(
						JDBCUrl.from(new MySqlDialect(), "jdbc:mysql://localhost:3306/SpringTest?useSSL=false", "r00t", "")
				)
				.addFallbackUrl(
						JDBCUrl.from(new MySqlDialect(), "jdbc:mysql://localhost:3306/SpringTest?useSSL=false", "root", "password")
				)
				.setConnectionConfiguration(connection -> {
					connection.setAutoCommit(false);
				})
				.setEagerRetry(true)
				.setNumOfFailsToFallback(10)
				.setWaitTimeBetweenRetryInS(5)
				.build();
		
		PoolConfiguration poolConfig = new PoolConfiguration.Builder()
				.setMaxPoolSize(10)
				.setInitialPoolSize(5)
				.build();
		
		return new ConnectionPool(connectionConfig, poolConfig);
	}
}
