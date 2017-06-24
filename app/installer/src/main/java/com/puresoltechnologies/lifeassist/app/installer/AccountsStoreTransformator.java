package com.puresoltechnologies.lifeassist.app.installer;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

import com.puresoltechnologies.genesis.commons.ProvidedVersionRange;
import com.puresoltechnologies.genesis.commons.SequenceMetadata;
import com.puresoltechnologies.genesis.commons.postgresql.PostgreSQLUtils;
import com.puresoltechnologies.genesis.transformation.jdbc.JDBCTransformationStep;
import com.puresoltechnologies.genesis.transformation.postgresql.PostgreSQLTransformationSequence;
import com.puresoltechnologies.genesis.transformation.spi.ComponentTransformator;
import com.puresoltechnologies.genesis.transformation.spi.TransformationSequence;
import com.puresoltechnologies.versioning.Version;

public class AccountsStoreTransformator implements ComponentTransformator {

    static final String ACCOUNT_SCHEMA = "accounts";
    static final String PASSWORD_TABLE_NAME = "passwords";

    @Override
    public String getComponentName() {
	return "Accounts Store";
    }

    @Override
    public Set<String> getDependencies() {
	HashSet<String> dependencies = new HashSet<>();
	dependencies.add("Contacts");
	return dependencies;
    }

    @Override
    public boolean isHostBased() {
	return false;
    }

    @Override
    public Set<TransformationSequence> getSequences() {
	Set<TransformationSequence> sequences = new HashSet<>();
	sequences.add(addVersion_0_1_0());
	return sequences;
    }

    private TransformationSequence addVersion_0_1_0() {
	Version startVersion = Version.valueOf("0.0.0");
	Version targetVersion = Version.valueOf("0.1.0");
	ProvidedVersionRange providedVersionRange = new ProvidedVersionRange(targetVersion, null);
	SequenceMetadata metadata = new SequenceMetadata(getComponentName(), startVersion, providedVersionRange);
	PostgreSQLTransformationSequence sequence = new PostgreSQLTransformationSequence(metadata);

	sequence.appendTransformation(new JDBCTransformationStep(sequence, "Rick-Rainer Ludwig",
		"CREATE SCHEMA " + ACCOUNT_SCHEMA, "Creates the schema for account data."));

	sequence.appendTransformation(new JDBCTransformationStep(sequence, "Rick-Rainer Ludwig", //
		"CREATE TABLE " + ACCOUNT_SCHEMA + "." + PASSWORD_TABLE_NAME//
			+ " (created timestamp, " //
			+ "last_modified timestamp, " //
			+ "email varchar not null," //
			+ "password varchar, " //
			+ "state varchar, "//
			+ "activation_key varchar, "//
			+ "CONSTRAINT " + PASSWORD_TABLE_NAME + "_PK PRIMARY KEY (email), " //
			+ "CONSTRAINT " + PASSWORD_TABLE_NAME + "_" + ContactsTransformator.EMAIL_ADDRESSES_TABLE
			+ "_FK FOREIGN KEY (email) REFERENCES " + ContactsTransformator.CONTACTS_SCHEMA + "."
			+ ContactsTransformator.EMAIL_ADDRESSES_TABLE + " (address)" //
			+ ")",
		"Create passwords table."));
	sequence.appendTransformation(new JDBCTransformationStep(sequence, "Rick-Rainer Ludwig",
		"CREATE INDEX " //
			+ PASSWORD_TABLE_NAME + "_state_idx"//
			+ " ON " + ACCOUNT_SCHEMA + "." + PASSWORD_TABLE_NAME //
			+ " (state)",
		"Creating index on state."));

	return sequence;
    }

    @Override
    public void dropAll(Properties configuration) {
	try (Connection connection = PostgreSQLUtils.connect(configuration)) {
	    try (Statement statement = connection.createStatement()) {
		statement.execute("DROP TABLE IF EXISTS " + ACCOUNT_SCHEMA + "." + PASSWORD_TABLE_NAME);
		statement.execute("DROP SCHEMA IF EXISTS " + ACCOUNT_SCHEMA);
	    }
	    connection.commit();
	} catch (NumberFormatException | SQLException e) {
	    throw new RuntimeException("Could not drop all changes for '" + getComponentName() + "'.", e);
	}
    }

}
