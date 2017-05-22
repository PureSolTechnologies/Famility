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

    private static final String CALENDAR_SCHEMA = "calendar";
    private static final String CALENDAR_SERIES_PARTICIPANTS_TABLE = "series_participants";
    private static final String CALENDAR_ENTRY_PARTICIPANTS_TABLE = "entry_articipants";
    private static final String CALENDAR_ENTRIES_TABLE = "entries";
    static final String CALENDAR_SERIES_TABLE = "series";
    private static final String CALENDAR_ENTRY_TYPES_TABLE = "entry_types";
    private static final String CALENDAR_ENTRY_DEPENDENCIES_TABLE = "entry_dependencies";

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
	sequence.appendTransformation(new JDBCTransformationStep(sequence, "Rick-Rainer Ludwig",
		"CREATE SCHEMA IF NOT EXISTS " + CALENDAR_SCHEMA, "Creates the schema for calendar data."));
	sequence.appendTransformation(new JDBCTransformationStep(sequence, "Rick-Rainer Ludwig", //
		"CREATE TABLE IF NOT EXISTS " + CALENDAR_SCHEMA + "." + CALENDAR_ENTRY_TYPES_TABLE //
			+ " (" //
			+ "type varchar not null unique, " //
			+ "name varchar not null, " //
			+ "CONSTRAINT " + CALENDAR_ENTRY_TYPES_TABLE + "_PK PRIMARY KEY (type))",
		"Create entry types table."));
	sequence.appendTransformation(new JDBCTransformationStep(sequence, "Rick-Rainer Ludwig", //
		"CREATE TABLE IF NOT EXISTS " + CALENDAR_SCHEMA + "." + CALENDAR_SERIES_TABLE //
			+ " (" //
			+ "id bigint not null, " //
			+ "type varchar not null, " //
			+ "title varchar(250) not null, " //
			+ "description varchar, " //
			+ "first_occurrence timestamp not null, "//
			+ "last_occurrence date, "//
			+ "timezone varchar not null, "//
			+ "duration_amount int not null, "//
			+ "duration_unit varchar not null, "//
			+ "reminder_amount int, " //
			+ "reminder_unit varchar, " //
			+ "occupancy varchar(9) not null, " //
			+ "turnus varchar(9) not null, " //
			+ "skipping int not null, " //
			+ "last_entry_created date not null, "//
			+ "CONSTRAINT " + CALENDAR_SERIES_TABLE + "_PK PRIMARY KEY (id)," //
			+ "CONSTRAINT " + CALENDAR_SERIES_TABLE + "_" + CALENDAR_ENTRY_TYPES_TABLE
			+ "_FK FOREIGN KEY (type) REFERENCES " + CALENDAR_SCHEMA + "." + CALENDAR_ENTRY_TYPES_TABLE
			+ " (type)" //
			+ ")",
		"Create entry series table."));
	sequence.appendTransformation(new JDBCTransformationStep(sequence, "Rick-Rainer Ludwig", //
		"CREATE TABLE IF NOT EXISTS " + CALENDAR_SCHEMA + "." + CALENDAR_ENTRIES_TABLE //
			+ " (" //
			+ "id bigint not null, " //
			+ "series_id bigint, " //
			+ "type varchar not null, " //
			+ "title varchar(250) not null, " //
			+ "description varchar, " //
			+ "begin_time timestamp not null, "//
			+ "begin_timezone varchar not null, "//
			+ "end_time timestamp not null, "//
			+ "end_timezone varchar not null, "//
			+ "reminder_amount int, " //
			+ "reminder_unit varchar, " //
			+ "occupancy varchar(9) not null, " //
			+ "CONSTRAINT " + CALENDAR_ENTRIES_TABLE + "_PK PRIMARY KEY (id), "//
			+ "CONSTRAINT " + CALENDAR_ENTRIES_TABLE + "_" + CALENDAR_SERIES_TABLE
			+ "_FK FOREIGN KEY (series_id) REFERENCES " + CALENDAR_SCHEMA + "." + CALENDAR_SERIES_TABLE
			+ " (id), " //
			+ "CONSTRAINT " + CALENDAR_ENTRIES_TABLE + "_" + CALENDAR_ENTRY_TYPES_TABLE
			+ "_FK FOREIGN KEY (type) REFERENCES " + CALENDAR_SCHEMA + "." + CALENDAR_ENTRY_TYPES_TABLE
			+ " (type)" //
			+ " )",
		"Create entries table."));
	sequence.appendTransformation(new JDBCTransformationStep(sequence, "Rick-Rainer Ludwig", //
		"CREATE TABLE IF NOT EXISTS " + CALENDAR_SCHEMA + "." + CALENDAR_SERIES_PARTICIPANTS_TABLE //
			+ " (" //
			+ "calendar_series_id bigint not null, " //
			+ "person_id bigint not null, " //
			+ "CONSTRAINT " + CALENDAR_SERIES_PARTICIPANTS_TABLE
			+ "_PK PRIMARY KEY (calendar_series_id, person_id), " //
			+ "CONSTRAINT " + CALENDAR_SERIES_PARTICIPANTS_TABLE + "_" + CALENDAR_SERIES_TABLE
			+ "_FK FOREIGN KEY (calendar_series_id) REFERENCES " + CALENDAR_SCHEMA + "."
			+ CALENDAR_SERIES_TABLE + " (id), " //
			+ "CONSTRAINT " + CALENDAR_SERIES_PARTICIPANTS_TABLE + "_people"
			+ "_FK FOREIGN KEY (person_id) REFERENCES people.people (id)" //
			+ ")",
		"Create entry serie participants table."));
	sequence.appendTransformation(new JDBCTransformationStep(sequence, "Rick-Rainer Ludwig",
		"CREATE INDEX " //
			+ CALENDAR_SERIES_PARTICIPANTS_TABLE + "_person_id_idx"//
			+ " ON " + CALENDAR_SCHEMA + "." + CALENDAR_SERIES_PARTICIPANTS_TABLE //
			+ " (person_id)",
		"Creating index on person_id."));

	sequence.appendTransformation(new JDBCTransformationStep(sequence, "Rick-Rainer Ludwig", //
		"CREATE TABLE IF NOT EXISTS " + CALENDAR_SCHEMA + "." + CALENDAR_ENTRY_PARTICIPANTS_TABLE //
			+ " (" //
			+ "calendar_entry_id bigint not null, " //
			+ "person_id bigint not null, " //
			+ "CONSTRAINT " + CALENDAR_ENTRY_PARTICIPANTS_TABLE
			+ "_PK PRIMARY KEY (calendar_entry_id, person_id), " //
			+ "CONSTRAINT " + CALENDAR_ENTRY_PARTICIPANTS_TABLE + "_" + CALENDAR_ENTRIES_TABLE
			+ "_FK FOREIGN KEY (calendar_entry_id) REFERENCES " + CALENDAR_SCHEMA + "."
			+ CALENDAR_ENTRIES_TABLE + " (id), " //
			+ "CONSTRAINT " + CALENDAR_ENTRY_PARTICIPANTS_TABLE + "_people"
			+ "_FK FOREIGN KEY (person_id) REFERENCES people.people (id)" //
			+ ")",
		"Create entry participants table."));

	sequence.appendTransformation(new JDBCTransformationStep(sequence, "Rick-Rainer Ludwig", //
		"CREATE TABLE IF NOT EXISTS " + CALENDAR_SCHEMA + "." + CALENDAR_ENTRY_DEPENDENCIES_TABLE //
			+ " (" //
			+ "from_calendar_entry_id bigint not null, " //
			+ "to_calendar_entry_id bigint not null, " //
			+ "dependency_type varchar(10) not null, " //
			+ "CONSTRAINT " + CALENDAR_ENTRY_DEPENDENCIES_TABLE
			+ "_PK PRIMARY KEY (from_calendar_entry_id, to_calendar_entry_id), " //
			+ "CONSTRAINT " + CALENDAR_ENTRY_DEPENDENCIES_TABLE + "_" + CALENDAR_ENTRIES_TABLE
			+ "_TO_FK FOREIGN KEY (to_calendar_entry_id) REFERENCES " + CALENDAR_SCHEMA + "."
			+ CALENDAR_ENTRIES_TABLE + " (id), " //
			+ "CONSTRAINT " + CALENDAR_ENTRY_DEPENDENCIES_TABLE + "_" + CALENDAR_ENTRIES_TABLE
			+ "_FROM_FK FOREIGN KEY (from_calendar_entry_id) REFERENCES " + CALENDAR_SCHEMA + "."
			+ CALENDAR_ENTRIES_TABLE + " (id)" //
			+ ")",
		"Create entry dependencies table."));

	sequence.appendTransformation(new JDBCTransformationStep(sequence, "Rick-Rainer Ludwig",
		"CREATE INDEX " //
			+ CALENDAR_ENTRY_PARTICIPANTS_TABLE + "_person_id_idx"//
			+ " ON " + CALENDAR_SCHEMA + "." + CALENDAR_ENTRY_PARTICIPANTS_TABLE //
			+ " (person_id)",
		"Creating index on person_id."));
	sequence.appendTransformation(
		new JDBCTransformationStep(sequence, "Rick-Rainer Ludwig",
			"CREATE SEQUENCE " + CALENDAR_SCHEMA + "." + "series_id_seq INCREMENT BY 1 OWNED BY "
				+ CALENDAR_SCHEMA + "." + CALENDAR_SERIES_TABLE + ".id",
			"Sequence for entry serie ids."));
	sequence.appendTransformation(new JDBCTransformationStep(sequence, "Rick-Rainer Ludwig",
		"CREATE SEQUENCE " + CALENDAR_SCHEMA + "." + "entry_id_seq INCREMENT BY 1 OWNED BY "
			+ CALENDAR_SCHEMA + "." + CALENDAR_ENTRIES_TABLE + ".id",
		"Sequence for entry ids."));
	sequence.appendTransformation(
		new JDBCTransformationStep(
			sequence, "Rick-Rainer Ludwig", "INSERT INTO " + CALENDAR_SCHEMA + "."
				+ CALENDAR_ENTRY_TYPES_TABLE + " (type, name) VALUES ('appointment', 'Appointment')",
			"Add appointment type."));
	sequence.appendTransformation(
		new JDBCTransformationStep(
			sequence, "Rick-Rainer Ludwig", "INSERT INTO " + CALENDAR_SCHEMA + "."
				+ CALENDAR_ENTRY_TYPES_TABLE + " (type, name) VALUES ('todo', 'TODO')",
			"Add TODO type."));
	sequence.appendTransformation(
		new JDBCTransformationStep(
			sequence, "Rick-Rainer Ludwig", "INSERT INTO " + CALENDAR_SCHEMA + "."
				+ CALENDAR_ENTRY_TYPES_TABLE + " (type, name) VALUES ('birthday', 'Birthday')",
			"Add birthday type."));
	sequence.appendTransformation(
		new JDBCTransformationStep(
			sequence, "Rick-Rainer Ludwig", "INSERT INTO " + CALENDAR_SCHEMA + "."
				+ CALENDAR_ENTRY_TYPES_TABLE + " (type, name) VALUES ('anniversary', 'Anniversary')",
			"Add anniversary type."));
	sequence.appendTransformation(
		new JDBCTransformationStep(
			sequence, "Rick-Rainer Ludwig", "INSERT INTO " + CALENDAR_SCHEMA + "."
				+ CALENDAR_ENTRY_TYPES_TABLE + " (type, name) VALUES ('reminder', 'Reminder')",
			"Add reminder type."));
	sequence.appendTransformation(new JDBCTransformationStep(sequence, "Rick-Rainer Ludwig",
		"ALTER TABLE " + "people." + PeopleTransformator.PEOPLE_TABLE + " ADD CONSTRAINT "
			+ PeopleTransformator.PEOPLE_TABLE + "_" + CALENDAR_SERIES_TABLE
			+ "_FK FOREIGN KEY (birthday_calendar_series_id) REFERENCES " + CALENDAR_SCHEMA + "."
			+ CALENDAR_SERIES_TABLE + " (id)",
		"Add anniversary type."));

	return sequence;
    }

    @Override
    public void dropAll(Properties configuration) {
	try (Connection connection = PostgreSQLUtils.connect(configuration)) {
	    try (Statement statement = connection.createStatement()) {
		statement.execute("ALTER TABLE IF EXISTS people." + PeopleTransformator.PEOPLE_TABLE
			+ " DROP CONSTRAINT IF EXISTS " + PeopleTransformator.PEOPLE_TABLE + "_" + CALENDAR_SERIES_TABLE
			+ "_FK");
		statement.execute(
			"DROP INDEX IF EXISTS " + CALENDAR_SCHEMA + "_" + CALENDAR_ENTRIES_TABLE + "_person_id_idx");
		statement.execute("DROP INDEX IF EXISTS " + CALENDAR_SCHEMA + "_" + CALENDAR_SERIES_PARTICIPANTS_TABLE
			+ "_person_id_idx");
		statement.execute("DROP TABLE IF EXISTS " + CALENDAR_SCHEMA + "." + CALENDAR_ENTRY_DEPENDENCIES_TABLE);
		statement.execute("DROP TABLE IF EXISTS " + CALENDAR_SCHEMA + "." + CALENDAR_SERIES_PARTICIPANTS_TABLE);
		statement.execute("DROP TABLE IF EXISTS " + CALENDAR_SCHEMA + "." + CALENDAR_ENTRY_PARTICIPANTS_TABLE);
		statement.execute("DROP TABLE IF EXISTS " + CALENDAR_SCHEMA + "." + CALENDAR_ENTRIES_TABLE);
		statement.execute("DROP TABLE IF EXISTS " + CALENDAR_SCHEMA + "." + CALENDAR_SERIES_TABLE);
		statement.execute("DROP TABLE IF EXISTS " + CALENDAR_SCHEMA + "." + CALENDAR_ENTRY_TYPES_TABLE);
		statement.execute("DROP SEQUENCE IF EXISTS " + CALENDAR_SCHEMA + "." + "calendar_entry_id_seq");
		statement.execute("DROP SEQUENCE IF EXISTS " + CALENDAR_SCHEMA + "." + "calendar__series_id_seq");
	    }
	    connection.commit();
	} catch (NumberFormatException | SQLException e) {
	    throw new RuntimeException("Could not drop all changes for '" + getComponentName() + "'.", e);
	}
    }

}
