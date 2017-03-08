package com.puresoltechnologies.lifeassist.app.installer;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.ZoneId;
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
import com.puresoltechnologies.lifeassist.app.api.settings.ParameterType;
import com.puresoltechnologies.versioning.Version;

public class SettingsStoreTransformator implements ComponentTransformator {

    static final String SYSTEM_SETTINGS_TABLE = "system_settings";
    private static final String USER_SETTINGS_TABLE = "user_settings";

    @Override
    public String getComponentName() {
	return "Settings Store";
    }

    @Override
    public Set<String> getDependencies() {
	HashSet<String> dependencies = new HashSet<>();
	dependencies.add("Users");
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
	sequence.appendTransformation(new JDBCTransformationStep(sequence, "Rick-Rainer Ludwig", //
		"CREATE TABLE IF NOT EXISTS " + SYSTEM_SETTINGS_TABLE //
			+ " (" //
			+ "parameter varchar not null, " //
			+ "type varchar(8) not null, " //
			+ "value varchar not null, " //
			+ "default_value varchar not null, " //
			+ "unit varchar not null, " //
			+ "description varchar not null, " //
			+ "CONSTRAINT " + SYSTEM_SETTINGS_TABLE + "_PK PRIMARY KEY (parameter))",
		"Create system settings table."));
	sequence.appendTransformation(new JDBCTransformationStep(sequence, "Rick-Rainer Ludwig", //
		"CREATE TABLE IF NOT EXISTS " + USER_SETTINGS_TABLE //
			+ " (" //
			+ "user_id bigint not null, " //
			+ "parameter varchar not null, " //
			+ "value varchar, " //
			+ "CONSTRAINT " + USER_SETTINGS_TABLE + "_PK PRIMARY KEY (user_id, parameter), "//
			+ "CONSTRAINT " + USER_SETTINGS_TABLE
			+ "_users_PK FOREIGN KEY (user_id) REFERENCES users (id), "//
			+ "CONSTRAINT " + USER_SETTINGS_TABLE + "_" + SYSTEM_SETTINGS_TABLE
			+ "_PK FOREIGN KEY (parameter) REFERENCES " + SYSTEM_SETTINGS_TABLE + " (parameter) "//
			+ ")",
		"Create user settings table."));
	sequence.appendTransformation(new AddSystemParameter(sequence, "timezone", ParameterType.STRING,
		ZoneId.systemDefault().getId(), "UTC", "", "The system time zone.", "Rick-Rainer Ludwig",
		"Adds the system time zone and set it to current time zone."));
	return sequence;
    }

    @Override
    public void dropAll(Properties configuration) {
	try (Connection connection = PostgreSQLUtils.connect(configuration)) {
	    try (Statement statement = connection.createStatement()) {
		statement.execute("DROP TABLE IF EXISTS " + USER_SETTINGS_TABLE);
		statement.execute("DROP TABLE IF EXISTS " + SYSTEM_SETTINGS_TABLE);
	    }
	    connection.commit();
	} catch (NumberFormatException | SQLException e) {
	    throw new RuntimeException("Could not drop all changes for '" + getComponentName() + "'.", e);
	}
    }

}
