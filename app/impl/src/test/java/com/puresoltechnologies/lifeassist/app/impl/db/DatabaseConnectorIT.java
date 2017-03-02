package com.puresoltechnologies.lifeassist.app.impl.db;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.SQLException;

import org.apache.commons.pool2.impl.GenericObjectPool;
import org.junit.BeforeClass;
import org.junit.Test;

import com.puresoltechnologies.lifeassist.app.impl.db.DatabaseConnector;

public class DatabaseConnectorIT {

    private static GenericObjectPool<Connection> pool;

    @SuppressWarnings("unchecked")
    @BeforeClass
    public static void initialize()
	    throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
	Field poolField = DatabaseConnector.class.getDeclaredField("pool");
	poolField.setAccessible(true);
	pool = (GenericObjectPool<Connection>) poolField.get(null);

	assertEquals(0, pool.getNumActive());
	assertEquals(0, pool.getNumIdle());
    }

    @Test
    public void test() throws SQLException {
	Connection connection = DatabaseConnector.getConnection();
	assertNotNull(connection);

	assertEquals(1, pool.getNumActive());
	assertEquals(0, pool.getNumIdle());

	Connection connection2 = DatabaseConnector.getConnection();
	assertNotNull(connection2);

	assertEquals(2, pool.getNumActive());
	assertEquals(0, pool.getNumIdle());

	assertNotSame(connection, connection2);

	connection.close();

	assertEquals(1, pool.getNumActive());
	assertEquals(1, pool.getNumIdle());

	connection2.close();

	assertEquals(0, pool.getNumActive());
	assertEquals(2, pool.getNumIdle());
    }

}
