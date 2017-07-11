package com.puresoltechnologies.famility.server.impl.db;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.SQLException;

import org.apache.commons.pool2.impl.GenericObjectPool;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.yaml.snakeyaml.Yaml;

import com.puresoltechnologies.famility.server.impl.db.DatabaseConfiguration;
import com.puresoltechnologies.famility.server.impl.db.DatabaseConnector;
import com.puresoltechnologies.famility.server.impl.db.ExtendedSQLQuery;
import com.puresoltechnologies.famility.server.impl.db.ExtendedSQLQueryFactory;

public class DatabaseConnectorIT {

    private static File configurationFile = new File("src/test/resources/database-configuration.yml");

    private static GenericObjectPool<Connection> pool;

    @SuppressWarnings("unchecked")
    @BeforeClass
    public static void initialize() throws NoSuchFieldException, SecurityException, IllegalArgumentException,
	    IllegalAccessException, FileNotFoundException, IOException {
	Yaml yaml = new Yaml();
	try (FileReader fileReader = new FileReader(configurationFile)) {
	    DatabaseConfiguration configuration = yaml.loadAs(fileReader, DatabaseConfiguration.class);
	    DatabaseConnector.initialize(configuration);
	}
	Field poolField = DatabaseConnector.class.getDeclaredField("pool");
	poolField.setAccessible(true);
	pool = (GenericObjectPool<Connection>) poolField.get(null);

	assertEquals(0, pool.getNumActive());
	assertEquals(0, pool.getNumIdle());
    }

    @AfterClass
    public static void shutdown() {
	DatabaseConnector.shutdown();
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

    @Test
    public void testCreateQuery() throws IOException, SQLException {
	assertEquals(0, pool.getNumActive());
	try (ExtendedSQLQuery<?> query = DatabaseConnector.createQuery()) {
	    assertEquals(1, pool.getNumActive());
	}
	assertEquals(0, pool.getNumActive());
    }

    @Test
    public void testCreateQueryFactory() throws IOException, SQLException {
	assertEquals(0, pool.getNumActive());
	try (ExtendedSQLQueryFactory queryFactory = DatabaseConnector.createQueryFactory()) {
	    assertEquals(1, pool.getNumActive());
	}
	assertEquals(0, pool.getNumActive());
    }
}
