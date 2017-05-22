package com.puresoltechnologies.lifeassist.app.rest.server.config;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import com.puresoltechnologies.lifeassist.app.impl.db.DatabaseConfiguration;

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
