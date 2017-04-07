package com.puresoltechnologies.lifeassist.app.test;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.yaml.snakeyaml.Yaml;

import com.puresoltechnologies.lifeassist.app.impl.db.DatabaseConnector;
import com.puresoltechnologies.lifeassist.app.test.configuration.TestConfiguration;

import io.dropwizard.testing.ResourceHelpers;

public abstract class AbstractLifeAssistantTest {

    private static String configurationFile = ResourceHelpers.resourceFilePath("configuration.yml");
    private static TestConfiguration configuration = null;

    private static void readConfiguration() throws IOException {
	Yaml yaml = new Yaml();
	try (FileReader fileReader = new FileReader(new File(configurationFile))) {
	    configuration = yaml.loadAs(fileReader, TestConfiguration.class);
	}
    }

    @BeforeClass
    public static void initializeDB() throws IOException, SQLException {
	readConfiguration();
	DatabaseConnector.initialize(configuration.getDatabase());
	cleanupDB();
    }

    protected static void cleanupDB() throws SQLException {
	try (Connection connection = DatabaseConnector.getConnection()) {
	    connection.prepareStatement("TRUNCATE TABLE appointments CASCADE");
	    connection.prepareStatement("TRUNCATE TABLE appointment_series CASCADE");
	    connection.commit();
	}
    }

    @AfterClass
    public static void shutdownDB() {
	DatabaseConnector.shutdown();
    }

}
