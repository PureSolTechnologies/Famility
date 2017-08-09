package com.puresoltechnologies.famility.server.impl.db;

import java.sql.Connection;
import java.sql.SQLException;

import javax.inject.Provider;

import org.apache.commons.pool2.impl.GenericObjectPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.Timer;
import com.codahale.metrics.Timer.Context;
import com.puresoltechnologies.famility.common.utils.Metrics;
import com.querydsl.sql.Configuration;
import com.querydsl.sql.SQLTemplates;

/**
 * This is the central connector to the database.
 * 
 * @author Rick-Rainer Ludwig
 */
public class DatabaseConnector {

    private static final Logger logger = LoggerFactory.getLogger(DatabaseConnector.class);

    private static final SQLTemplates dialect = new ExtendedPostgresTemplates();
    private static final Configuration configuration = new Configuration(dialect);

    private static GenericObjectPool<Connection> pool = null;
    private static boolean initialized = false;
    private static Timer getConnectionTimer;

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

    public static boolean isInitialized() {
	return initialized;
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

	MetricRegistry metrics = Metrics.getMetrics();
	metrics.register(DatabaseConnector.class.getName(), new DatabaseConnectorPoolMetricsSet());
	getConnectionTimer = metrics.timer(MetricRegistry.name(DatabaseConnector.class, "getConnection"));

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
	    Context timeContext = getConnectionTimer.time();
	    Connection connection = pool.borrowObject();
	    if (connection == null) {
		throw new IllegalStateException("Could not get a valid connection from pool.");
	    }
	    timeContext.stop();
	    return new PooledConnection(pool, connection);
	} catch (Exception e) {
	    throw new SQLException("Could not get connection.", e);
	}
    }

    public static ExtendedSQLQueryFactory createQueryFactory() throws SQLException {
	checkIfInitialized();
	return new ExtendedSQLQueryFactory(configuration, new Provider<Connection>() {

	    private final Connection connection = getConnection();

	    @Override
	    public Connection get() {
		return connection;
	    }
	});
    }

    public static <T> ExtendedSQLQuery<T> createQuery() throws SQLException {
	checkIfInitialized();
	return new ExtendedSQLQuery<>(getConnection(), configuration);
    }

    /**
     * Empty constructor to avoid instantiation.
     */
    private DatabaseConnector() {
    }
}
