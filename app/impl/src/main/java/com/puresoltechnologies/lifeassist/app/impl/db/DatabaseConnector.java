package com.puresoltechnologies.lifeassist.app.impl.db;

import java.sql.Connection;
import java.sql.SQLException;

import org.apache.commons.pool2.impl.GenericObjectPool;

/**
 * This is the central conntector to the database.
 * 
 * @author Rick-Rainer Ludwig
 */
public class DatabaseConnector {

    private static GenericObjectPool<Connection> pool = new GenericObjectPool<>(new PooledConnectionFactory());
    static {
	pool.setMinIdle(5);
	pool.setMaxIdle(25);
	pool.setMaxTotal(100);
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
