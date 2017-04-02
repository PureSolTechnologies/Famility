package com.puresoltechnologies.lifeassist.app.impl.db;

import java.sql.Connection;
import java.sql.SQLException;

import org.apache.commons.pool2.impl.GenericObjectPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.querydsl.sql.Configuration;
import com.querydsl.sql.PostgreSQLTemplates;
import com.querydsl.sql.SQLTemplates;

/**
 * This is the central connector to the database.
 * 
 * @author Rick-Rainer Ludwig
 */
public class DatabaseConnector {

    private static final Logger logger = LoggerFactory.getLogger(DatabaseConnector.class);

    private static final SQLTemplates dialect = new PostgreSQLTemplates();
    private static final Configuration configuration = new Configuration(dialect);

    private static GenericObjectPool<Connection> pool = null;
    private static boolean initialized = false;

    private static void checkIfInitialized() {
	if (!initialized) {
	    throw new IllegalStateException("DatabaseConnector was not initialized.");
	}
    }

    private static void checkIfNotInitialized() {
	if (initialized) {
	    throw new IllegalStateException("DatabaseConnector already initialized.");
	}
    }

    public static synchronized void initialize(DatabaseConfiguration configuration) {
	logger.info("Initialize DatabaseConnector...");
	checkIfNotInitialized();
	pool = new GenericObjectPool<>(new PooledConnectionFactory(configuration));
	DatabasePoolConfiguration poolConfiguration = configuration.getPool();
	pool.setMinIdle(poolConfiguration.getMinIdle());
	pool.setMaxIdle(poolConfiguration.getSetMaxIdle());
	pool.setMaxTotal(poolConfiguration.getMaxTotal());
	pool.setMaxWaitMillis(poolConfiguration.getMaxWaitMillis());

	initialized = true;
	logger.info("DatabaseConnector initialized.");
    }

    public static synchronized void shutdown() {
	logger.info("Shutting down DatabaseConnector...");
	checkIfInitialized();
	initialized = false;
	pool.close();
	pool = null;
	logger.info("DatabaseConnector shut down.");
    }

    public static Connection getConnection() throws SQLException {
	checkIfInitialized();
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

    public static <T> CloseableSQLQuery<T> getQuery() throws SQLException {
	checkIfInitialized();
	CloseableSQLQuery<T> query = new CloseableSQLQuery<>(getConnection(), configuration);
	return query;
    }

    /**
     * Empty constructor to avoid instantiation.
     */
    private DatabaseConnector() {
    }
}
