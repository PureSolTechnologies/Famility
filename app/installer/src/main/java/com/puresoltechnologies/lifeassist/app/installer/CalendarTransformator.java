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

public class CalendarTransformator implements ComponentTransformator {

    private static final String APPOINTMENT_SERIES_PARTICIPANTS_TABLE = "appointments_series_participants";
    private static final String APPOINTMENT_PARTICIPANTS_TABLE = "appointments_series_participants";
    private static final String APPOINTMENTS_TABLE = "appointments";
    private static final String APPOINTMENT_SERIES_TABLE = "appointment_series";

    @Override
    public String getComponentName() {
	return "Calendar";
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
		"CREATE TABLE IF NOT EXISTS " + APPOINTMENT_SERIES_TABLE //
			+ " (" //
			+ "id bigint not null, " //
			+ "type varchar(12) not null, " //
			+ "title varchar(250) not null, " //
			+ "description varchar, " //
			+ "first_occurrence timestamp, "//
			+ "timezone varchar, "//
			+ "duration_amount int, "//
			+ "duration_unit varchar, "//
			+ "reminder_amount int, " //
			+ "reminder_unit varchar, " //
			+ "occupancy varchar(9), " //
			+ "turnus varchar(7), " //
			+ "skipping int, " //
			+ "CONSTRAINT " + APPOINTMENT_SERIES_TABLE + "_PK PRIMARY KEY (id))",
		"Create events table."));
	sequence.appendTransformation(new JDBCTransformationStep(sequence, "Rick-Rainer Ludwig", //
		"CREATE TABLE IF NOT EXISTS " + APPOINTMENTS_TABLE //
			+ " (" //
			+ "id bigint not null, " //
			+ "appointment_series_id bigint, " //
			+ "type varchar(12) not null, " //
			+ "title varchar(250) not null, " //
			+ "description varchar, " //
			+ "occurrence timestamp, "//
			+ "timezone varchar, "//
			+ "duration_amount int, "//
			+ "duration_unit varchar, "//
			+ "reminder_amount int, " //
			+ "reminder_unit varchar, " //
			+ "occupancy varchar(9), " //
			+ "CONSTRAINT " + APPOINTMENTS_TABLE + "_PK PRIMARY KEY (id), "//
			+ "CONSTRAINT " + APPOINTMENTS_TABLE + "_" + APPOINTMENT_SERIES_TABLE
			+ "_FK FOREIGN KEY (appointment_series_id) REFERENCES appointment_series (id)" //
			+ " )",
		"Create events table."));
	sequence.appendTransformation(new JDBCTransformationStep(sequence, "Rick-Rainer Ludwig", //
		"CREATE TABLE IF NOT EXISTS " + APPOINTMENT_SERIES_PARTICIPANTS_TABLE //
			+ " (" //
			+ "appointment_series_id bigint not null, " //
			+ "user_id bigint not null, " //
			+ "CONSTRAINT " + APPOINTMENT_SERIES_PARTICIPANTS_TABLE
			+ "_PK PRIMARY KEY (appointment_series_id, user_id), " //
			+ "CONSTRAINT " + APPOINTMENT_SERIES_PARTICIPANTS_TABLE + "_" + APPOINTMENT_SERIES_TABLE
			+ "_FK FOREIGN KEY (appointment_series_id) REFERENCES appointment_series (id), " //
			+ "CONSTRAINT " + APPOINTMENT_SERIES_PARTICIPANTS_TABLE + "_users"
			+ "_FK FOREIGN KEY (user_id) REFERENCES users (id)" //
			+ ")",
		"Create events table."));
	sequence.appendTransformation(new JDBCTransformationStep(sequence, "Rick-Rainer Ludwig",
		"CREATE INDEX " //
			+ APPOINTMENT_SERIES_PARTICIPANTS_TABLE + "_user_id_idx"//
			+ " ON " + APPOINTMENT_SERIES_PARTICIPANTS_TABLE //
			+ " (user_id)",
		"Creating index on user_id."));

	sequence.appendTransformation(new JDBCTransformationStep(sequence, "Rick-Rainer Ludwig", //
		"CREATE TABLE IF NOT EXISTS " + APPOINTMENT_PARTICIPANTS_TABLE //
			+ " (" //
			+ "appointment_id bigint not null, " //
			+ "user_id bigint not null, " //
			+ "CONSTRAINT " + APPOINTMENT_PARTICIPANTS_TABLE + "_PK PRIMARY KEY (appointment_id, user_id), " //
			+ "CONSTRAINT " + APPOINTMENT_PARTICIPANTS_TABLE + "_" + APPOINTMENTS_TABLE
			+ "_FK FOREIGN KEY (appointment_id) REFERENCES " + APPOINTMENTS_TABLE + " (id), " //
			+ "CONSTRAINT " + APPOINTMENT_PARTICIPANTS_TABLE + "_users"
			+ "_FK FOREIGN KEY (user_id) REFERENCES users (id)" //
			+ ")",
		"Create events table."));
	sequence.appendTransformation(new JDBCTransformationStep(sequence, "Rick-Rainer Ludwig",
		"CREATE INDEX " //
			+ APPOINTMENT_PARTICIPANTS_TABLE + "_user_id_idx"//
			+ " ON " + APPOINTMENT_PARTICIPANTS_TABLE //
			+ " (user_id)",
		"Creating index on user_id."));
	sequence.appendTransformation(new JDBCTransformationStep(sequence, "Rick-Rainer Ludwig",
		"CREATE SEQUENCE appointment_series_id_seq INCREMENT BY 1 OWNED BY " + APPOINTMENT_SERIES_TABLE + ".id",
		"Sequence for appointment serie ids."));
	sequence.appendTransformation(new JDBCTransformationStep(sequence, "Rick-Rainer Ludwig",
		"CREATE SEQUENCE appointments_id_seq INCREMENT BY 1 OWNED BY " + APPOINTMENTS_TABLE + ".id",
		"Sequence for appointment ids."));
	return sequence;
    }

    @Override
    public void dropAll(Properties configuration) {
	try (Connection connection = PostgreSQLUtils.connect(configuration)) {
	    try (Statement statement = connection.createStatement()) {
		statement.execute("DROP INDEX IF EXISTS " + APPOINTMENTS_TABLE + "_user_id_idx");
		statement.execute("DROP INDEX IF EXISTS " + APPOINTMENT_SERIES_PARTICIPANTS_TABLE + "_user_id_idx");
		statement.execute("DROP TABLE IF EXISTS " + APPOINTMENT_SERIES_PARTICIPANTS_TABLE);
		statement.execute("DROP TABLE IF EXISTS " + APPOINTMENT_PARTICIPANTS_TABLE);
		statement.execute("DROP TABLE IF EXISTS " + APPOINTMENTS_TABLE);
		statement.execute("DROP TABLE IF EXISTS " + APPOINTMENT_SERIES_TABLE);
		statement.execute("DROP SEQUENCE IF EXISTS appointments_id_seq");
		statement.execute("DROP SEQUENCE IF EXISTS appointment_series_id_seq");
	    }
	    connection.commit();
	} catch (NumberFormatException | SQLException e) {
	    throw new RuntimeException("Could not drop all changes for '" + getComponentName() + "'.", e);
	}
    }

}
