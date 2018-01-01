package com.puresoltechnologies.famility.common.ldap;

import org.apache.commons.pool.impl.GenericObjectPool;
import org.apache.directory.ldap.client.api.DefaultLdapConnectionFactory;
import org.apache.directory.ldap.client.api.DefaultPoolableLdapConnectionFactory;
import org.apache.directory.ldap.client.api.LdapConnection;
import org.apache.directory.ldap.client.api.LdapConnectionConfig;
import org.apache.directory.ldap.client.api.LdapConnectionPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.Timer;
import com.codahale.metrics.Timer.Context;
import com.puresoltechnologies.famility.common.utils.Metrics;

public class LdapConnector {

    private static final Logger logger = LoggerFactory.getLogger(LdapConnector.class);

    private static final String HOSTNAME = "ldap";
    private static final int PORT = 389;
    private static final String ADMIN_DN = "cn=admin,dc=puresol-technologies,dc=net";
    private static final String ADMIN_PASSWORD = "TrustNo1";
    private static final long CONNECTION_TIMEOUT = 30;

    private static LdapConnectionPool pool;
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

    public static synchronized void initialize() {
	logger.info("Initialize DatabaseConnector...");
	checkIfNotInitialized();
	LdapConnectionConfig config = new LdapConnectionConfig();
	config.setLdapHost(HOSTNAME);
	config.setLdapPort(PORT);
	config.setName(ADMIN_DN);
	config.setCredentials(ADMIN_PASSWORD);

	DefaultLdapConnectionFactory factory = new DefaultLdapConnectionFactory(config);
	factory.setTimeOut(CONNECTION_TIMEOUT);

	// optional, values below are defaults
	GenericObjectPool.Config poolConfig = new GenericObjectPool.Config();
	poolConfig.lifo = true;
	poolConfig.maxActive = 8;
	poolConfig.maxIdle = 8;
	poolConfig.maxWait = -1L;
	poolConfig.minEvictableIdleTimeMillis = 1000L * 60L * 30L;
	poolConfig.minIdle = 0;
	poolConfig.numTestsPerEvictionRun = 3;
	poolConfig.softMinEvictableIdleTimeMillis = -1L;
	poolConfig.testOnBorrow = false;
	poolConfig.testOnReturn = false;
	poolConfig.testWhileIdle = false;
	poolConfig.timeBetweenEvictionRunsMillis = -1L;
	poolConfig.whenExhaustedAction = GenericObjectPool.WHEN_EXHAUSTED_BLOCK;

	pool = new LdapConnectionPool(new DefaultPoolableLdapConnectionFactory(factory), poolConfig);
	MetricRegistry metrics = Metrics.getMetrics();
	metrics.register(LdapConnector.class.getName(), new LdapConnectorPoolMetricsSet());
	getConnectionTimer = metrics.timer(MetricRegistry.name(LdapConnector.class, "getConnection"));

	initialized = true;
	logger.info("DatabaseConnector initialized.");
    }

    public static synchronized void shutdown() {
	try {
	    logger.info("Shutting down DatabaseConnector...");
	    checkIfInitialized();
	    initialized = false;
	    pool.close();
	    pool = null;
	    logger.info("DatabaseConnector shut down.");
	} catch (Exception e) {
	    throw new RuntimeException("Could not close LDAP connection pool.", e);
	}
    }

    public static LdapConnection getConnection() {
	try {
	    checkIfInitialized();
	    Context timeContext = getConnectionTimer.time();
	    LdapConnection connection = pool.borrowObject();
	    if (connection == null) {
		throw new IllegalStateException("Could not get a valid connection from pool.");
	    }
	    timeContext.stop();
	    return new PooledLdapConnection(pool, connection);
	} catch (Exception e) {
	    throw new RuntimeException("Could not return LDAP connection.", e);
	}
    }

    private LdapConnector() {
    }

}
