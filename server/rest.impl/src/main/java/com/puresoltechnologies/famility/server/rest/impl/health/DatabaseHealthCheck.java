package com.puresoltechnologies.famility.server.rest.impl.health;

import java.sql.Connection;
import java.sql.SQLException;

import com.codahale.metrics.health.HealthCheck;
import com.puresoltechnologies.famility.server.impl.db.DatabaseConnector;

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
