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

public class ContactsTransformator implements ComponentTransformator {

    static final String CONTACTS_SCHEMA = "contacts";
    static final String CONTACTS_TABLE = "contacts";
    static final String EMAILS_TABLE = "emails";
    static final String EMAIL_TYPES_TABLE = "email_types";

    @Override
    public String getComponentName() {
	return "Contacts";
    }

    @Override
    public Set<String> getDependencies() {
	Set<String> dependencies = new HashSet<>();
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
		"CREATE SCHEMA IF NOT EXISTS " + CONTACTS_SCHEMA, "Creates the schema for people data."));
	sequence.appendTransformation(new JDBCTransformationStep(sequence, "Rick-Rainer Ludwig", //
		"CREATE TABLE IF NOT EXISTS " + CONTACTS_SCHEMA + "." + CONTACTS_TABLE //
			+ " (" //
			+ "id bigint not null, " //
			+ "name varchar not null, " //
			+ "birthday date, " //
			+ "birthday_calendar_series_id bigint, " //
			+ "CONSTRAINT " + CONTACTS_TABLE + "_PK PRIMARY KEY (id)" //
			+ ")",
		"Create cnontacts table."));
	sequence.appendTransformation(new JDBCTransformationStep(sequence, "Rick-Rainer Ludwig", //
		"CREATE TABLE IF NOT EXISTS " + CONTACTS_SCHEMA + "." + EMAIL_TYPES_TABLE //
			+ " (" //
			+ "id bigint not null, " //
			+ "name varchar(12) not null unique, " //
			+ "CONSTRAINT " + EMAIL_TYPES_TABLE + "_PK PRIMARY KEY (id)" //
			+ ")",
		"Create email types table."));
	sequence.appendTransformation(new JDBCTransformationStep(sequence, "Rick-Rainer Ludwig", //
		"CREATE TABLE IF NOT EXISTS " + CONTACTS_SCHEMA + "." + EMAILS_TABLE //
			+ " (" //
			+ "contact_id bigint not null, " //
			+ "address varchar not null unique, " //
			+ "type_id bigint not null, " //
			+ "CONSTRAINT " + EMAILS_TABLE + "_PK PRIMARY KEY (contact_id, address), " //
			+ "CONSTRAINT " + EMAILS_TABLE + "_" + CONTACTS_TABLE
			+ "_FK FOREIGN KEY (contact_id) REFERENCES " + CONTACTS_SCHEMA + "." + CONTACTS_TABLE
			+ " (id), " //
			+ "CONSTRAINT " + EMAILS_TABLE + "_" + EMAIL_TYPES_TABLE
			+ "_FK FOREIGN KEY (type_id) REFERENCES " + CONTACTS_SCHEMA + "." + EMAIL_TYPES_TABLE + " (id)" //
			+ ")",
		"Create emails table."));
	sequence.appendTransformation(new JDBCTransformationStep(sequence,
		"Rick-Rainer Ludwig", "CREATE SEQUENCE " + CONTACTS_SCHEMA + "."
			+ "person_id_seq INCREMENT BY 1 OWNED BY " + CONTACTS_SCHEMA + "." + CONTACTS_TABLE + ".id",
		"Sequence for person ids."));
	sequence.appendTransformation(new JDBCTransformationStep(sequence, "Rick-Rainer Ludwig",
		"CREATE SEQUENCE " + CONTACTS_SCHEMA + "." + "email_type_id_seq INCREMENT BY 1 OWNED BY "
			+ CONTACTS_SCHEMA + "." + EMAIL_TYPES_TABLE + ".id",
		"Sequence for person ids."));
	return sequence;
    }

    @Override
    public void dropAll(Properties configuration) {
	try (Connection connection = PostgreSQLUtils.connect(configuration)) {
	    try (Statement statement = connection.createStatement()) {
		statement.execute("DROP SEQUENCE IF EXISTS " + CONTACTS_SCHEMA + "." + "person_id_seq");
		statement.execute("DROP TABLE IF EXISTS " + CONTACTS_SCHEMA + "." + EMAILS_TABLE);
		statement.execute("DROP TABLE IF EXISTS " + CONTACTS_SCHEMA + "." + EMAIL_TYPES_TABLE);
		statement.execute("DROP TABLE IF EXISTS " + CONTACTS_SCHEMA + "." + CONTACTS_TABLE);
	    }
	    connection.commit();
	} catch (NumberFormatException | SQLException e) {
	    throw new RuntimeException("Could not drop all changes for '" + getComponentName() + "'.", e);
	}
    }

}
