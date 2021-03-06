package com.puresoltechnologies.famility.server.test;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.yaml.snakeyaml.Yaml;

import com.codahale.metrics.MetricRegistry;
import com.puresoltechnologies.famility.common.utils.Metrics;
import com.puresoltechnologies.famility.server.impl.db.DatabaseConnector;
import com.puresoltechnologies.famility.server.test.configuration.TestConfiguration;

import io.dropwizard.testing.ResourceHelpers;

/**
 * This is the base class for all Famility tests. This class ensures an empty
 * database and constant start conditions for call test classes (not for test
 * methods!). So, for {@link BeforeClass} the conditions are prepared, if it is
 * needed for {@link Before}, it is to be assured by the test class.
 * 
 * @author Rick-Rainer Ludwig
 *
 */
public abstract class AbstractFamilityTest {

    static String configurationFile = ResourceHelpers.resourceFilePath("configuration.yml");
    static TestConfiguration configuration = null;
    private static boolean handleDatabase = true;

    static void readConfiguration() throws IOException {
	Yaml yaml = new Yaml();
	try (FileReader fileReader = new FileReader(new File(configurationFile))) {
	    configuration = yaml.loadAs(fileReader, TestConfiguration.class);
	}
    }

    /**
     * Initializes the databases:
     * <ol>
     * <li>The configuration is read</li>
     * <li>The {@link DatabaseConnector} is initialized.</li>
     * <li>The database is cleared.</li>
     * </ol>
     * 
     * @throws IOException
     * @throws SQLException
     */
    @BeforeClass
    public static void initializeDB() throws IOException, SQLException {
	readConfiguration();
	if (!Metrics.isInitialized()) {
	    Metrics.initialize(new MetricRegistry());
	}
	if (DatabaseConnector.isInitialized()) {
	    handleDatabase = false;
	} else {
	    DatabaseConnector.initialize(configuration.getDatabase());
	}

    }

    @Before
    public void clean() throws SQLException {
	cleanupDB();
    }

    /**
     * Clears the databases.
     * 
     * @throws SQLException
     */
    protected static void cleanupDB() throws SQLException {
	try (Connection connection = DatabaseConnector.getConnection()) {

	    connection.createStatement().execute("TRUNCATE TABLE calendar.events CASCADE");
	    connection.createStatement().execute("TRUNCATE TABLE calendar.series CASCADE");
	    connection.createStatement().execute("TRUNCATE TABLE contacts.contacts CASCADE");
	    connection.commit();
	}
    }

    /**
     * Shuts down the database.
     */
    @AfterClass
    public static void shutdownDB() {
	if (handleDatabase) {
	    DatabaseConnector.shutdown();
	}
    }

}
