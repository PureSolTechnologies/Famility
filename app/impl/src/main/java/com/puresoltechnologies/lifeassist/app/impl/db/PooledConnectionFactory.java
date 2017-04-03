package com.puresoltechnologies.lifeassist.app.impl.db;

import java.sql.Connection;
import java.sql.SQLException;

import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.PooledObjectFactory;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

    public PooledConnectionFactory(DatabaseConfiguration configuration) {
	super();
	this.configuration = configuration;
    }

    @Override
    public PooledObject<Connection> makeObject() throws SQLException {
	Connection connection = PostgreSQLUtils.connect(configuration.getHost(), configuration.getPort(),
		configuration.getDatabase(), configuration.getUser(), configuration.getPassword(),
		configuration.isSsl());
	connection.setAutoCommit(false);
	return new DefaultPooledObject<Connection>(connection);
    }

    @Override
    public void destroyObject(PooledObject<Connection> p) throws SQLException {
	p.getObject().close();
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
