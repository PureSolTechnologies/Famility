package com.puresoltechnologies.famility.server.rest.impl.config;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import com.puresoltechnologies.famility.server.impl.db.DatabaseConfiguration;

import io.dropwizard.Configuration;

public class LifeAssistantConfiguration extends Configuration {

    @Valid
    @NotNull
    private DatabaseConfiguration database = new DatabaseConfiguration();

    public DatabaseConfiguration getDatabase() {
	return database;
    }

    public void setDatabase(DatabaseConfiguration databaseConfiguration) {
	this.database = databaseConfiguration;
    }

}
