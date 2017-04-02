package com.puresoltechnologies.lifeassist.app.installer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.puresoltechnologies.genesis.commons.TransformationException;
import com.puresoltechnologies.genesis.commons.TransformationMetadata;
import com.puresoltechnologies.genesis.transformation.postgresql.PostgreSQLTransformationSequence;
import com.puresoltechnologies.genesis.transformation.spi.TransformationStep;
import com.puresoltechnologies.lifeassist.common.db.ParameterType;

public class AddSystemParameter implements TransformationStep {

    private static final Logger logger = LoggerFactory.getLogger(AddSystemParameter.class);

    private final PostgreSQLTransformationSequence sequence;
    private final String parameter;
    private final ParameterType type;
    private final String value;
    private final String defaultValue;
    private final String unit;
    private final String description;
    private final String developer;
    private final String comment;

    public AddSystemParameter(PostgreSQLTransformationSequence sequence, String parameter, ParameterType type,
	    String value, String defaultValue, String unit, String description, String developer, String comment) {
	super();
	this.sequence = sequence;
	this.parameter = parameter;
	this.type = type;
	this.value = value;
	this.defaultValue = defaultValue;
	this.unit = unit;
	this.description = description;
	this.developer = developer;
	this.comment = comment;
    }

    @Override
    public TransformationMetadata getMetadata() {
	return new TransformationMetadata(sequence.getMetadata(), developer,
		"Add system parameter '" + parameter + "'.", comment);
    }

    @Override
    public void transform() throws TransformationException {
	logger.info("Add system parameter '" + parameter + "'.");
	try {
	    Connection connection = sequence.getConnection();

	    try (PreparedStatement statement = connection
		    .prepareStatement("INSERT INTO " + SettingsStoreTransformator.SYSTEM_SETTINGS_TABLE//
			    + " (" //
			    + "parameter, " //
			    + "type, " //
			    + "value, " //
			    + "default_value, " //
			    + "unit, " //
			    + "description " //
			    + ") VALUES (?,?,?,?,?,?) ")) {
		statement.setString(1, parameter);
		statement.setString(2, type.name());
		statement.setString(3, value);
		statement.setString(4, defaultValue);
		statement.setString(5, unit);
		statement.setString(6, description);
		statement.execute();
		connection.commit();
	    } finally {
		connection.rollback();
	    }
	} catch (SQLException e) {
	    throw new TransformationException("Could not add system parameter '" + parameter + "'.", e);
	}
    }

}
