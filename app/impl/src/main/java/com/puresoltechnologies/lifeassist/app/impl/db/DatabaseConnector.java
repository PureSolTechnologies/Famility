package com.puresoltechnologies.lifeassist.app.impl.db;

import java.sql.Connection;
import java.sql.SQLException;

import org.apache.commons.pool2.impl.GenericObjectPool;

/**
 * This is the central connector to the database.
 * 
 * @author Rick-Rainer Ludwig
 */
public class DatabaseConnector {

    private static GenericObjectPool<Connection> pool = null;

    public static synchronized void initialize(DatabaseConfiguration configuration) {
	pool = new GenericObjectPool<>(new PooledConnectionFactory(configuration));
	DatabasePoolConfiguration poolConfiguration = configuration.getPool();
	pool.setMinIdle(poolConfiguration.getMinIdle());
	pool.setMaxIdle(poolConfiguration.getSetMaxIdle());
	pool.setMaxTotal(poolConfiguration.getMaxTotal());
	pool.setMaxWaitMillis(poolConfiguration.getMaxWaitMillis());
    }

    public static Connection getConnection() throws SQLException {
	try {
	    Connection connection = pool.borrowObject();
	    if (connection == null) {
		throw new IllegalStateException("Could not get a valid connection from pool.");
	    }
	    return new PooledConnection(pool, connection);
	} catch (Exception e) {
	    throw new SQLException("Could not get connection.", e);
	}
    }

}
