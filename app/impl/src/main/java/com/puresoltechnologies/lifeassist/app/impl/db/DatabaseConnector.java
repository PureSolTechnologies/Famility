package com.puresoltechnologies.lifeassist.app.impl.db;

import java.sql.Connection;
import java.sql.SQLException;

import org.apache.commons.pool2.impl.GenericObjectPool;
import org.skife.jdbi.v2.DBI;
import org.skife.jdbi.v2.tweak.ConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This is the central connector to the database.
 * 
 * @author Rick-Rainer Ludwig
 */
public class DatabaseConnector {

    private static final Logger logger = LoggerFactory.getLogger(DatabaseConnector.class);

    private static GenericObjectPool<Connection> pool = null;
    private static DBI dbi = null;
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

	ConnectionFactory factory = new ConnectionFactory() {
	    @Override
	    public Connection openConnection() throws SQLException {
		return getConnection();
	    }
	};
	dbi = new DBI(factory);
	initialized = true;
	logger.info("DatabaseConnector initialized.");
    }

    public static synchronized void shutdown() {
	logger.info("Shutting down DatabaseConnector...");
	checkIfInitialized();
	initialized = false;
	dbi = null;
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

    public static DBI getDBI() throws SQLException {
	checkIfInitialized();
	return dbi;
    }

    /**
     * Empty constructor to avoid instantiation.
     */
    private DatabaseConnector() {
    }
}
