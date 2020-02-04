package com.jkubinyi.simplerest;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.jkubinyi.simplepool.PoolConfiguration;
import com.jkubinyi.simplepool.common.PoolEventHandler;
import com.jkubinyi.simplepool.database.ConnectionPool;
import com.jkubinyi.simplepool.database.ConnectionPoolConfiguration;
import com.jkubinyi.simplepool.database.ConnectionPoolConfiguration.ReturnStrategy;
import com.jkubinyi.simplepool.database.JDBCUrl;
import com.jkubinyi.simplepool.database.dialect.ImmutableDriverDialect;
import com.mysql.jdbc.Driver;

@Configuration
public class ConnectionConfiguration {

	@Value("${sop.pool.maxPoolSize}")
	private int maxPoolSize;

	@Value("${sop.pool.initPoolSize}")
	private int initPoolSize;

	@Value("${sop.pool.minIdlePoolSize}")
	private int minIdlePoolSize;
	
	@Value("${sop.connection.numerOfFailsToFallback}")
	private int numerOfFailsToFallback;

	@Value("${sop.connection.waitTimeBetweenRetryS}")
	private int waitTimeBetweenRetryS;

	@Value("${sop.connection.eagerRetry}")
	private boolean eagerRetry;

	@Value("${sop.connection.autoCommit}")
	private boolean autoCommit;
	
	@Value("#{'${sop.connection.returnStrategy}'.toUpperCase()}")
	private ReturnStrategy returnStrategy;

	@Value("${sop.connection.primary.url}")
	private String primaryUrl;

	@Value("${sop.connection.primary.username}")
	private String primaryUsername;

	@Value("${sop.connection.primary.password}")
	private String primaryPassword;

	@Value("${sop.connection.fallback.url}")
	private String fallbackUrl;

	@Value("${sop.connection.fallback.username}")
	private String fallbackUsername;

	@Value("${sop.connection.fallback.password}")
	private String fallbackPassword;
	
	private static final ImmutableDriverDialect DEFAULT_DIALECT = new GenericDialect();

	@Bean(destroyMethod = "close")
	public ConnectionPool createConnectionPool() {
		ConnectionPoolConfiguration connectionConfig = new ConnectionPoolConfiguration.Builder(
					this.jdbcUrlFrom(ConnectionConfiguration.DEFAULT_DIALECT, this.primaryUrl, this.primaryUsername, this.primaryPassword)
				)
				.addFallbackUrl(
					this.jdbcUrlFrom(ConnectionConfiguration.DEFAULT_DIALECT, this.fallbackUrl, this.fallbackUsername, this.fallbackPassword)
				)
				.setConnectionConfiguration(connection -> connection.setAutoCommit(this.autoCommit))
				.setEagerRetry(this.eagerRetry)
				.setNumOfFailsToFallback(this.numerOfFailsToFallback)
				.setWaitTimeBetweenRetryInS(this.waitTimeBetweenRetryS)
				.setReturnStrategy(this.returnStrategy)
				.build();

		PoolConfiguration poolConfig = new PoolConfiguration.Builder()
				.setMaxPoolSize(this.maxPoolSize)
				.setInitialPoolSize(this.initPoolSize)
				.setMinPoolIdleSize(this.minIdlePoolSize)
				.setEventHandler(new L4JPoolEventHandler())
				.build();

		return new ConnectionPool(connectionConfig, poolConfig);
	}
	
	private JDBCUrl jdbcUrlFrom(ImmutableDriverDialect dialect, String url, String username, String password) {
		return JDBCUrl.from(dialect, url, username, password);
	}
	
	/**
	 * Used to create a generic dialect which will force {@link ConnectionPool} to
	 * use JDBC {@link Driver} to validate the link using its built in mechanism. Each
	 * {@link Driver} has its own way to validate the connection so up to the creators
	 * how they chose to validate it.
	 * 
	 * @author jurajkubinyi
	 */
	private static class GenericDialect implements ImmutableDriverDialect {
		@Override
		public Optional<String> getLinkValidityQuery() {
			return Optional.empty();
		}
	}
	
	private static class L4JPoolEventHandler implements PoolEventHandler {

		private Logger log = LoggerFactory.getLogger(L4JPoolEventHandler.class);
		
		@Override
		public void newEvent(Severity severity, String format, Object... objects) {
			switch (severity) {
			case info:
				log.info(format, objects);
				break;

			case debug:
				log.debug(format, objects);
				break;
				
			case warn:
				log.warn(format, objects);
				break;

			case error:
				log.error(format, objects);
				break;
			}
		}
		
	}
}
