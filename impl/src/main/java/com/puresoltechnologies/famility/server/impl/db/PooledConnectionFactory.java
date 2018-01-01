package com.puresoltechnologies.famility.server.impl.db;

import java.sql.Connection;
import java.sql.SQLException;

import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.PooledObjectFactory;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.codahale.metrics.Meter;
import com.codahale.metrics.MetricRegistry;
import com.puresoltechnologies.famility.common.utils.Metrics;
import com.puresoltechnologies.genesis.commons.postgresql.PostgreSQLUtils;

/**
 * This class provides the {@link PooledObjectFactory} for {@link Connection}.
 * 
 * @author Rick-Rainer Ludwig
 *
 */
public class PooledConnectionFactory implements PooledObjectFactory<Connection> {

    private static final Logger logger = LoggerFactory.getLogger(PooledConnectionFactory.class);

    private final DatabaseConfiguration configuration;

    private final Meter connectMeter;
    private final Meter disconnectMeter;

    public PooledConnectionFactory(DatabaseConfiguration configuration) {
	super();
	this.configuration = configuration;
	this.connectMeter = Metrics.getMetrics().meter(MetricRegistry.name(getClass(), "connect"));
	this.disconnectMeter = Metrics.getMetrics().meter(MetricRegistry.name(getClass(), "disconnect"));
    }

    @Override
    public PooledObject<Connection> makeObject() throws SQLException {
	Connection connection = PostgreSQLUtils.connect(configuration.getHost(), configuration.getPort(),
		configuration.getDatabase(), configuration.getUser(), configuration.getPassword(),
		configuration.isSsl());
	connection.setAutoCommit(false);
	connectMeter.mark();
	return new DefaultPooledObject<Connection>(connection);
    }

    @Override
    public void destroyObject(PooledObject<Connection> p) throws SQLException {
	p.getObject().close();
	disconnectMeter.mark();
    }

    @Override
    public boolean validateObject(PooledObject<Connection> p) {
	try {
	    return p.getObject().isValid(10);
	} catch (SQLException e) {
	    logger.warn("Could not validate object.", e);
	    return false;
	}
    }

    @Override
    public void activateObject(PooledObject<Connection> p) throws Exception {
	// intentionally left empty
    }

    @Override
    public void passivateObject(PooledObject<Connection> p) throws Exception {
	// intentionally left empty
    }

}
