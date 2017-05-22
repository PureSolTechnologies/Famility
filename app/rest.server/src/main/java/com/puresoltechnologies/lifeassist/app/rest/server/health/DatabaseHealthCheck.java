package com.puresoltechnologies.lifeassist.app.rest.server.health;

import java.sql.Connection;
import java.sql.SQLException;

import com.codahale.metrics.health.HealthCheck;
import com.puresoltechnologies.lifeassist.app.impl.db.DatabaseConnector;

/**
 * This is a simple healtch check to check the database connection.
 * 
 * @author Rick-Rainer Ludwig
 */
public class DatabaseHealthCheck extends HealthCheck {

    @Override
    protected Result check() {
	try (Connection connection = DatabaseConnector.getConnection()) {
	    return Result.healthy();
	} catch (SQLException e) {
	    return Result.unhealthy(e);
	}
    }

}
