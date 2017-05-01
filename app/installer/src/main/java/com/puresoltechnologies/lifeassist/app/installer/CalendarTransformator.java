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

    private static final String ENTRY_SERIE_PARTICIPANTS_TABLE = "entry_serie_participants";
    private static final String ENTRY_PARTICIPANTS_TABLE = "entry_articipants";
    private static final String ENTRIES_TABLE = "entries";
    static final String ENTRY_SERIES_TABLE = "entry_series";
    private static final String ENTRY_TYPES_TABLE = "entry_types";

    @Override
    public String getComponentName() {
	return "Calendar";
    }

    @Override
    public Set<String> getDependencies() {
	HashSet<String> dependencies = new HashSet<>();
	dependencies.add("People");
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
		"CREATE TABLE IF NOT EXISTS " + ENTRY_TYPES_TABLE //
			+ " (" //
			+ "type varchar not null unique, " //
			+ "name varchar not null, " //
			+ "CONSTRAINT " + ENTRY_TYPES_TABLE + "_PK PRIMARY KEY (type))",
		"Create events table."));
	sequence.appendTransformation(new JDBCTransformationStep(sequence, "Rick-Rainer Ludwig", //
		"CREATE TABLE IF NOT EXISTS " + ENTRY_SERIES_TABLE //
			+ " (" //
			+ "id bigint not null, " //
			+ "type varchar not null, " //
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
			+ "CONSTRAINT " + ENTRY_SERIES_TABLE + "_PK PRIMARY KEY (id)," //
			+ "CONSTRAINT " + ENTRY_SERIES_TABLE + "_" + ENTRY_TYPES_TABLE
			+ "_FK FOREIGN KEY (type) REFERENCES " + ENTRY_TYPES_TABLE + " (type)" //
			+ ")",
		"Create events table."));
	sequence.appendTransformation(new JDBCTransformationStep(sequence, "Rick-Rainer Ludwig", //
		"CREATE TABLE IF NOT EXISTS " + ENTRIES_TABLE //
			+ " (" //
			+ "id bigint not null, " //
			+ "entry_serie_id bigint, " //
			+ "type varchar not null, " //
			+ "title varchar(250) not null, " //
			+ "description varchar, " //
			+ "occurrence timestamp, "//
			+ "timezone varchar, "//
			+ "duration_amount int, "//
			+ "duration_unit varchar, "//
			+ "reminder_amount int, " //
			+ "reminder_unit varchar, " //
			+ "occupancy varchar(9), " //
			+ "CONSTRAINT " + ENTRIES_TABLE + "_PK PRIMARY KEY (id), "//
			+ "CONSTRAINT " + ENTRIES_TABLE + "_" + ENTRY_SERIES_TABLE
			+ "_FK FOREIGN KEY (entry_serie_id) REFERENCES entry_series (id), " //
			+ "CONSTRAINT " + ENTRIES_TABLE + "_" + ENTRY_TYPES_TABLE + "_FK FOREIGN KEY (type) REFERENCES "
			+ ENTRY_TYPES_TABLE + " (type)" //
			+ " )",
		"Create events table."));
	sequence.appendTransformation(new JDBCTransformationStep(sequence, "Rick-Rainer Ludwig", //
		"CREATE TABLE IF NOT EXISTS " + ENTRY_SERIE_PARTICIPANTS_TABLE //
			+ " (" //
			+ "entry_serie_id bigint not null, " //
			+ "person_id bigint not null, " //
			+ "CONSTRAINT " + ENTRY_SERIE_PARTICIPANTS_TABLE
			+ "_PK PRIMARY KEY (entry_serie_id, person_id), " //
			+ "CONSTRAINT " + ENTRY_SERIE_PARTICIPANTS_TABLE + "_" + ENTRY_SERIES_TABLE
			+ "_FK FOREIGN KEY (entry_serie_id) REFERENCES entry_series (id), " //
			+ "CONSTRAINT " + ENTRY_SERIE_PARTICIPANTS_TABLE + "_people"
			+ "_FK FOREIGN KEY (person_id) REFERENCES people (id)" //
			+ ")",
		"Create events table."));
	sequence.appendTransformation(new JDBCTransformationStep(sequence, "Rick-Rainer Ludwig",
		"CREATE INDEX " //
			+ ENTRY_SERIE_PARTICIPANTS_TABLE + "_person_id_idx"//
			+ " ON " + ENTRY_SERIE_PARTICIPANTS_TABLE //
			+ " (person_id)",
		"Creating index on person_id."));

	sequence.appendTransformation(new JDBCTransformationStep(sequence, "Rick-Rainer Ludwig", //
		"CREATE TABLE IF NOT EXISTS " + ENTRY_PARTICIPANTS_TABLE //
			+ " (" //
			+ "entry_id bigint not null, " //
			+ "person_id bigint not null, " //
			+ "CONSTRAINT " + ENTRY_PARTICIPANTS_TABLE + "_PK PRIMARY KEY (entry_id, person_id), " //
			+ "CONSTRAINT " + ENTRY_PARTICIPANTS_TABLE + "_" + ENTRIES_TABLE
			+ "_FK FOREIGN KEY (entry_id) REFERENCES " + ENTRIES_TABLE + " (id), " //
			+ "CONSTRAINT " + ENTRY_PARTICIPANTS_TABLE + "_people"
			+ "_FK FOREIGN KEY (person_id) REFERENCES people (id)" //
			+ ")",
		"Create events table."));
	sequence.appendTransformation(new JDBCTransformationStep(sequence, "Rick-Rainer Ludwig",
		"CREATE INDEX " //
			+ ENTRY_PARTICIPANTS_TABLE + "_person_id_idx"//
			+ " ON " + ENTRY_PARTICIPANTS_TABLE //
			+ " (person_id)",
		"Creating index on person_id."));
	sequence.appendTransformation(new JDBCTransformationStep(sequence, "Rick-Rainer Ludwig",
		"CREATE SEQUENCE entry_serie_id_seq INCREMENT BY 1 OWNED BY " + ENTRY_SERIES_TABLE + ".id",
		"Sequence for entry serie ids."));
	sequence.appendTransformation(new JDBCTransformationStep(sequence, "Rick-Rainer Ludwig",
		"CREATE SEQUENCE entry_id_seq INCREMENT BY 1 OWNED BY " + ENTRIES_TABLE + ".id",
		"Sequence for entry ids."));
	sequence.appendTransformation(new JDBCTransformationStep(sequence, "Rick-Rainer Ludwig",
		"INSERT INTO " + ENTRY_TYPES_TABLE + " (type, name) VALUES ('appointment', 'Appointment')",
		"Add appointment type."));
	sequence.appendTransformation(new JDBCTransformationStep(sequence, "Rick-Rainer Ludwig",
		"INSERT INTO " + ENTRY_TYPES_TABLE + " (type, name) VALUES ('todo', 'TODO')", "Add TODO type."));
	sequence.appendTransformation(new JDBCTransformationStep(sequence, "Rick-Rainer Ludwig",
		"INSERT INTO " + ENTRY_TYPES_TABLE + " (type, name) VALUES ('birthday', 'Birthday')",
		"Add birthday type."));
	sequence.appendTransformation(new JDBCTransformationStep(sequence, "Rick-Rainer Ludwig",
		"INSERT INTO " + ENTRY_TYPES_TABLE + " (type, name) VALUES ('anniversary', 'Anniversary')",
		"Add anniversary type."));
	sequence.appendTransformation(new JDBCTransformationStep(sequence, "Rick-Rainer Ludwig",
		"ALTER TABLE " + PeopleTransformator.PEOPLE_TABLE + " ADD CONSTRAINT "
			+ PeopleTransformator.PEOPLE_TABLE + "_" + ENTRY_SERIES_TABLE
			+ "_FK FOREIGN KEY (birthday_entry_serie_id) REFERENCES " + ENTRY_SERIES_TABLE + " (id)",
		"Add anniversary type."));

	return sequence;
    }

    @Override
    public void dropAll(Properties configuration) {
	try (Connection connection = PostgreSQLUtils.connect(configuration)) {
	    try (Statement statement = connection.createStatement()) {
		statement.execute(
			"ALTER TABLE IF EXISTS " + PeopleTransformator.PEOPLE_TABLE + " DROP CONSTRAINT IF EXISTS "
				+ PeopleTransformator.PEOPLE_TABLE + "_" + ENTRY_SERIES_TABLE + "_FK");
		statement.execute("DROP INDEX IF EXISTS " + ENTRIES_TABLE + "_person_id_idx");
		statement.execute("DROP INDEX IF EXISTS " + ENTRY_SERIE_PARTICIPANTS_TABLE + "_person_id_idx");
		statement.execute("DROP TABLE IF EXISTS " + ENTRY_SERIE_PARTICIPANTS_TABLE);
		statement.execute("DROP TABLE IF EXISTS " + ENTRY_PARTICIPANTS_TABLE);
		statement.execute("DROP TABLE IF EXISTS " + ENTRIES_TABLE);
		statement.execute("DROP TABLE IF EXISTS " + ENTRY_SERIES_TABLE);
		statement.execute("DROP TABLE IF EXISTS " + ENTRY_TYPES_TABLE);
		statement.execute("DROP SEQUENCE IF EXISTS entry_id_seq");
		statement.execute("DROP SEQUENCE IF EXISTS entry_serie_id_seq");
	    }
	    connection.commit();
	} catch (NumberFormatException | SQLException e) {
	    throw new RuntimeException("Could not drop all changes for '" + getComponentName() + "'.", e);
	}
    }

}
