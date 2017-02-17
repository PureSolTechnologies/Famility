package com.puresoltechnologies.lifeassist.app.installer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.puresoltechnologies.commons.types.EmailAddress;
import com.puresoltechnologies.genesis.commons.TransformationException;
import com.puresoltechnologies.genesis.commons.TransformationMetadata;
import com.puresoltechnologies.genesis.transformation.postgresql.PostgreSQLTransformationSequence;
import com.puresoltechnologies.genesis.transformation.spi.TransformationStep;
import com.puresoltechnologies.passwordstore.core.encrypter.Encrypter1;
import com.puresoltechnologies.passwordstore.domain.PasswordData;
import com.puresoltechnologies.passwordstore.domain.PasswordState;

public class AddUserStep implements TransformationStep {
    private static final Logger logger = LoggerFactory.getLogger(AddUserStep.class);

    private final PostgreSQLTransformationSequence sequence;
    private final EmailAddress user;
    private final String password;
    private final PasswordState state;
    private final String developer;
    private final String comment;

    public AddUserStep(PostgreSQLTransformationSequence sequence, EmailAddress user, String password,
	    PasswordState state, String developer, String comment) {
	super();
	this.sequence = sequence;
	this.user = user;
	this.password = password;
	this.state = state;
	this.developer = developer;
	this.comment = comment;
    }

    @Override
    public void transform() throws TransformationException {
	logger.info("Add first administrator account.");
	try {
	    Connection connection = sequence.getConnection();
	    try (PreparedStatement statement = connection
		    .prepareStatement("INSERT INTO " + PostgreSQLTransformator.PASSWORD_TABLE_NAME//
			    + " (created, " //
			    + "last_modified, " //
			    + "email," //
			    + "password, " //
			    + "state, " + "activation_key) VALUES (?,?,?,?,?,?) ")) {
		LocalDateTime now = LocalDateTime.now();
		PasswordData passwordData = new PasswordData(1, Encrypter1.encrypt(password));
		statement.setTimestamp(1, Timestamp.valueOf(now));
		statement.setTimestamp(2, Timestamp.valueOf(now));
		statement.setString(3, user.getAddress());
		statement.setString(4, passwordData.toString());
		statement.setString(5, state.name());
		statement.setString(6, "");
		statement.execute();
		connection.commit();
	    } finally {
		connection.rollback();
	    }
	} catch (SQLException e) {
	    throw new TransformationException("Could not add user '" + user + "'.", e);
	}
    }

    @Override
    public TransformationMetadata getMetadata() {
	return new TransformationMetadata(sequence.getMetadata(), developer, "Create password for " + user.toString(),
		comment);
    }

}
