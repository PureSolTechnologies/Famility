package com.puresoltechnologies.famility.server.installer;

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

public class EventLoggerTransformator implements ComponentTransformator {

    private static final String MONITORING_SCHEMA = "monitoring";
    private static final String EVENT_LOG_TABLE = "eventlog";

    @Override
    public String getComponentName() {
	return "Event Logger";
    }

    @Override
    public Set<String> getDependencies() {
	return new HashSet<>();
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
		"CREATE SCHEMA " + MONITORING_SCHEMA, "Creates the schema for monitoring data."));
	sequence.appendTransformation(new JDBCTransformationStep(sequence, "Rick-Rainer Ludwig", //
		"CREATE TABLE " + MONITORING_SCHEMA + "." + EVENT_LOG_TABLE //
			+ " (" //
			+ "server varchar not null, " //
			+ "time timestamp not null, " //
			+ "severity varchar not null, "//
			+ "type varchar not null, " //
			+ "component varchar not null, " //
			+ "event_id bigint not null, " //
			+ "message varchar not null, "//
			+ "user_name varchar, " //
			+ "user_id bigint," //
			+ "client varchar, " //
			+ "exception_message varchar, " //
			+ "exception_stacktrace varchar, "//
			+ "CONSTRAINT " + EVENT_LOG_TABLE
			+ "_PK PRIMARY KEY (server, time, severity, type, component, event_id, message))",
		"Create events table."));
	return sequence;
    }

    @Override
    public void dropAll(Properties configuration) {
	try (Connection connection = PostgreSQLUtils.connect(configuration)) {
	    try (Statement statement = connection.createStatement()) {
		statement.execute("DROP TABLE IF EXISTS " + MONITORING_SCHEMA + "." + EVENT_LOG_TABLE);
		statement.execute("DROP SCHEMA IF EXISTS " + MONITORING_SCHEMA);
	    }
	    connection.commit();
	} catch (NumberFormatException | SQLException e) {
	    throw new RuntimeException("Could not drop all changes for '" + getComponentName() + "'.", e);
	}
    }

}
