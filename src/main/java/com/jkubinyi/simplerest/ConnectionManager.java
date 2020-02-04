package com.jkubinyi.simplerest;

import com.jkubinyi.simplepool.PoolConfiguration;
import com.jkubinyi.simplepool.database.ConnectionPool;
import com.jkubinyi.simplepool.database.ConnectionPoolConfiguration;

public class ConnectionManager extends ConnectionPool {

	public ConnectionManager(ConnectionPoolConfiguration config, PoolConfiguration poolConfig) {
		super(config, poolConfig);
	}

}
