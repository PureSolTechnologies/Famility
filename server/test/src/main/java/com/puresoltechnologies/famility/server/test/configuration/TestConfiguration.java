package com.puresoltechnologies.famility.server.test.configuration;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import com.puresoltechnologies.famility.server.impl.db.DatabaseConfiguration;
import com.puresoltechnologies.famility.server.rest.impl.config.FamilityConfiguration;

/**
 * This is a class to read the {@link FamilityConfiguration} into an own
 * object for testing. It is needed to create classes for the needed Dropwizard
 * configuration.
 * <p>
 * <b>The Famility configuration objects are to be reduced for testing!</b>
 * 
 * @author Rick-Rainer Ludwig
 *
 */
public class TestConfiguration {

    @Valid
    @NotNull
    private DatabaseConfiguration database = new DatabaseConfiguration();

    @Valid
    private TestLoggingConfiguration logging = new TestLoggingConfiguration();

    public DatabaseConfiguration getDatabase() {
	return database;
    }

    public void setDatabase(DatabaseConfiguration databaseConfiguration) {
	this.database = databaseConfiguration;
    }

    public TestLoggingConfiguration getLogging() {
	return logging;
    }

    public void setLogging(TestLoggingConfiguration logging) {
	this.logging = logging;
    }

}
