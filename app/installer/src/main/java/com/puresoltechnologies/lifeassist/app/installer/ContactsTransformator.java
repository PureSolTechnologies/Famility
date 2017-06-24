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
    static final String EMAIL_ADDRESSES_TABLE = "email_addresses";
    static final String EMAIL_ADDRESS_TYPES_TABLE = "email_adress_types";
    static final String PHONE_NUMBERS_TABLE = "phone_numbers";
    static final String PHONE_NUMBER_TYPES_TABLE = "phone_number_types";
    static final String POSTAL_ADDRESSES_TABLE = "postal_addresses";
    static final String POSTAL_ADDRESS_TYPES_TABLE = "postal_address_types";
    static final String BANK_ACCOUNTS_TABLE = "bank_accounts";
    static final String BANK_ACCOUNT_TYPES_TABLE = "bank_account_types";
    static final String OTHER_CONTACTS_TABLE = "other_contacts";
    static final String OTHER_CONTACT_TYPES_TABLE = "other_contact_types";

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
		"CREATE SCHEMA " + CONTACTS_SCHEMA, "Creates the schema for people data."));
	sequence.appendTransformation(new JDBCTransformationStep(sequence, "Rick-Rainer Ludwig", //
		"CREATE TABLE " + CONTACTS_SCHEMA + "." + CONTACTS_TABLE //
			+ " (" //
			+ "id bigint not null, " //
			+ "name varchar not null, " //
			+ "birthday date, " //
			+ "birthday_calendar_series_id bigint, " //
			+ "CONSTRAINT " + CONTACTS_TABLE + "_PK PRIMARY KEY (id)" //
			+ ")",
		"Create contacts table."));
	sequence.appendTransformation(new JDBCTransformationStep(sequence, "Rick-Rainer Ludwig", //
		"CREATE TABLE " + CONTACTS_SCHEMA + "." + EMAIL_ADDRESS_TYPES_TABLE //
			+ " (" //
			+ "id bigint not null, " //
			+ "name varchar(32) not null unique, " //
			+ "CONSTRAINT " + EMAIL_ADDRESS_TYPES_TABLE + "_PK PRIMARY KEY (id)" //
			+ ")",
		"Create table email types."));
	sequence.appendTransformation(new JDBCTransformationStep(sequence, "Rick-Rainer Ludwig", //
		"CREATE TABLE " + CONTACTS_SCHEMA + "." + PHONE_NUMBER_TYPES_TABLE //
			+ " (" //
			+ "id bigint not null, " //
			+ "name varchar(32) not null unique, " //
			+ "CONSTRAINT " + PHONE_NUMBER_TYPES_TABLE + "_PK PRIMARY KEY (id)" //
			+ ")",
		"Create table for phone number types."));
	sequence.appendTransformation(new JDBCTransformationStep(sequence, "Rick-Rainer Ludwig", //
		"CREATE TABLE " + CONTACTS_SCHEMA + "." + OTHER_CONTACT_TYPES_TABLE //
			+ " (" //
			+ "id bigint not null, " //
			+ "name varchar(32) not null unique, " //
			+ "CONSTRAINT " + OTHER_CONTACT_TYPES_TABLE + "_PK PRIMARY KEY (id)" //
			+ ")",
		"Create table for other contact types."));
	sequence.appendTransformation(new JDBCTransformationStep(sequence, "Rick-Rainer Ludwig", //
		"CREATE TABLE " + CONTACTS_SCHEMA + "." + POSTAL_ADDRESS_TYPES_TABLE //
			+ " (" //
			+ "id bigint not null, " //
			+ "name varchar(32) not null unique, " //
			+ "CONSTRAINT " + POSTAL_ADDRESS_TYPES_TABLE + "_PK PRIMARY KEY (id)" //
			+ ")",
		"Create table for postal address types."));
	sequence.appendTransformation(new JDBCTransformationStep(sequence, "Rick-Rainer Ludwig", //
		"CREATE TABLE " + CONTACTS_SCHEMA + "." + BANK_ACCOUNT_TYPES_TABLE //
			+ " (" //
			+ "id bigint not null, " //
			+ "name varchar(32) not null unique, " //
			+ "CONSTRAINT " + BANK_ACCOUNT_TYPES_TABLE + "_PK PRIMARY KEY (id)" //
			+ ")",
		"Create table for bank account types."));
	sequence.appendTransformation(new JDBCTransformationStep(sequence, "Rick-Rainer Ludwig", //
		"CREATE TABLE " + CONTACTS_SCHEMA + "." + EMAIL_ADDRESSES_TABLE //
			+ " (" //
			+ "contact_id bigint not null, " //
			+ "address varchar not null unique, " //
			+ "type_id bigint not null, " //
			+ "CONSTRAINT " + EMAIL_ADDRESSES_TABLE + "_PK PRIMARY KEY (contact_id, address), " //
			+ "CONSTRAINT " + EMAIL_ADDRESSES_TABLE + "_" + CONTACTS_TABLE
			+ "_FK FOREIGN KEY (contact_id) REFERENCES " + CONTACTS_SCHEMA + "." + CONTACTS_TABLE
			+ " (id), " //
			+ "CONSTRAINT " + EMAIL_ADDRESSES_TABLE + "_" + EMAIL_ADDRESS_TYPES_TABLE
			+ "_FK FOREIGN KEY (type_id) REFERENCES " + CONTACTS_SCHEMA + "." + EMAIL_ADDRESS_TYPES_TABLE
			+ " (id)" //
			+ ")",
		"Create table for email addresses."));
	sequence.appendTransformation(new JDBCTransformationStep(sequence, "Rick-Rainer Ludwig", //
		"CREATE TABLE " + CONTACTS_SCHEMA + "." + PHONE_NUMBERS_TABLE //
			+ " (" //
			+ "id bigint not null, " //
			+ "contact_id bigint not null, " //
			+ "type_id bigint not null, " //
			+ "CONSTRAINT " + PHONE_NUMBERS_TABLE + "_PK PRIMARY KEY (id, contact_id), " //
			+ "CONSTRAINT " + PHONE_NUMBERS_TABLE + "_" + CONTACTS_TABLE
			+ "_FK FOREIGN KEY (contact_id) REFERENCES " + CONTACTS_SCHEMA + "." + CONTACTS_TABLE
			+ " (id), " //
			+ "CONSTRAINT " + PHONE_NUMBERS_TABLE + "_" + PHONE_NUMBER_TYPES_TABLE
			+ "_FK FOREIGN KEY (type_id) REFERENCES " + CONTACTS_SCHEMA + "." + PHONE_NUMBER_TYPES_TABLE
			+ " (id)" //
			+ ")",
		"Create table for phone numbers."));
	sequence.appendTransformation(new JDBCTransformationStep(sequence, "Rick-Rainer Ludwig", //
		"CREATE TABLE " + CONTACTS_SCHEMA + "." + POSTAL_ADDRESSES_TABLE //
			+ " (" //
			+ "id bigint not null, " //
			+ "contact_id bigint not null, " //
			+ "type_id bigint not null, " //
			+ "CONSTRAINT " + POSTAL_ADDRESSES_TABLE + "_PK PRIMARY KEY (id, contact_id), " //
			+ "CONSTRAINT " + POSTAL_ADDRESSES_TABLE + "_" + CONTACTS_TABLE
			+ "_FK FOREIGN KEY (contact_id) REFERENCES " + CONTACTS_SCHEMA + "." + CONTACTS_TABLE
			+ " (id), " //
			+ "CONSTRAINT " + POSTAL_ADDRESSES_TABLE + "_" + POSTAL_ADDRESS_TYPES_TABLE
			+ "_FK FOREIGN KEY (type_id) REFERENCES " + CONTACTS_SCHEMA + "." + POSTAL_ADDRESS_TYPES_TABLE
			+ " (id)" //
			+ ")",
		"Create table for postal addresses."));

	sequence.appendTransformation(new JDBCTransformationStep(sequence, "Rick-Rainer Ludwig", //
		"CREATE TABLE " + CONTACTS_SCHEMA + "." + BANK_ACCOUNTS_TABLE //
			+ " (" //
			+ "id bigint not null, " //
			+ "contact_id bigint not null, " //
			+ "type_id bigint not null, " //
			+ "CONSTRAINT " + BANK_ACCOUNTS_TABLE + "_PK PRIMARY KEY (id, contact_id), " //
			+ "CONSTRAINT " + BANK_ACCOUNTS_TABLE + "_" + CONTACTS_TABLE
			+ "_FK FOREIGN KEY (contact_id) REFERENCES " + CONTACTS_SCHEMA + "." + CONTACTS_TABLE
			+ " (id), " //
			+ "CONSTRAINT " + BANK_ACCOUNTS_TABLE + "_" + BANK_ACCOUNT_TYPES_TABLE
			+ "_FK FOREIGN KEY (type_id) REFERENCES " + CONTACTS_SCHEMA + "." + BANK_ACCOUNT_TYPES_TABLE
			+ " (id)" //
			+ ")",
		"Create table for bank accounts."));

	sequence.appendTransformation(new JDBCTransformationStep(sequence, "Rick-Rainer Ludwig", //
		"CREATE TABLE " + CONTACTS_SCHEMA + "." + OTHER_CONTACTS_TABLE //
			+ " (" //
			+ "id bigint not null, " //
			+ "contact_id bigint not null, " //
			+ "type_id bigint not null, " //
			+ "CONSTRAINT " + OTHER_CONTACTS_TABLE + "_PK PRIMARY KEY (id, contact_id), " //
			+ "CONSTRAINT " + OTHER_CONTACTS_TABLE + "_" + CONTACTS_TABLE
			+ "_FK FOREIGN KEY (contact_id) REFERENCES " + CONTACTS_SCHEMA + "." + CONTACTS_TABLE
			+ " (id), " //
			+ "CONSTRAINT " + OTHER_CONTACTS_TABLE + "_" + OTHER_CONTACT_TYPES_TABLE
			+ "_FK FOREIGN KEY (type_id) REFERENCES " + CONTACTS_SCHEMA + "." + OTHER_CONTACT_TYPES_TABLE
			+ " (id)" //
			+ ")",
		"Create table for other contacts."));

	sequence.appendTransformation(new JDBCTransformationStep(sequence,
		"Rick-Rainer Ludwig", "CREATE SEQUENCE " + CONTACTS_SCHEMA + "."
			+ "contact_id_seq INCREMENT BY 1 OWNED BY " + CONTACTS_SCHEMA + "." + CONTACTS_TABLE + ".id",
		"Sequence for contact ids."));
	sequence.appendTransformation(
		new JDBCTransformationStep(sequence, "Rick-Rainer Ludwig",
			"CREATE SEQUENCE " + CONTACTS_SCHEMA + "." + "phone_number_id_seq INCREMENT BY 1 OWNED BY "
				+ CONTACTS_SCHEMA + "." + PHONE_NUMBERS_TABLE + ".id",
			"Sequence for phone number ids."));
	sequence.appendTransformation(
		new JDBCTransformationStep(sequence, "Rick-Rainer Ludwig",
			"CREATE SEQUENCE " + CONTACTS_SCHEMA + "." + "postal_address_id_seq INCREMENT BY 1 OWNED BY "
				+ CONTACTS_SCHEMA + "." + POSTAL_ADDRESSES_TABLE + ".id",
			"Sequence for postal address ids."));
	sequence.appendTransformation(
		new JDBCTransformationStep(sequence, "Rick-Rainer Ludwig",
			"CREATE SEQUENCE " + CONTACTS_SCHEMA + "." + "bank_account_id_seq INCREMENT BY 1 OWNED BY "
				+ CONTACTS_SCHEMA + "." + BANK_ACCOUNTS_TABLE + ".id",
			"Sequence for bank accout ids."));
	sequence.appendTransformation(
		new JDBCTransformationStep(sequence, "Rick-Rainer Ludwig",
			"CREATE SEQUENCE " + CONTACTS_SCHEMA + "." + "other_contact_id_seq INCREMENT BY 1 OWNED BY "
				+ CONTACTS_SCHEMA + "." + OTHER_CONTACTS_TABLE + ".id",
			"Sequence for other contact ids."));
	sequence.appendTransformation(new JDBCTransformationStep(sequence, "Rick-Rainer Ludwig",
		"CREATE SEQUENCE " + CONTACTS_SCHEMA + "." + "email_address_type_id_seq INCREMENT BY 1 OWNED BY "
			+ CONTACTS_SCHEMA + "." + EMAIL_ADDRESS_TYPES_TABLE + ".id",
		"Sequence for email type ids."));
	sequence.appendTransformation(new JDBCTransformationStep(sequence, "Rick-Rainer Ludwig",
		"CREATE SEQUENCE " + CONTACTS_SCHEMA + "." + "phone_number_type_id_seq INCREMENT BY 1 OWNED BY "
			+ CONTACTS_SCHEMA + "." + PHONE_NUMBER_TYPES_TABLE + ".id",
		"Sequence for phone number type ids."));
	sequence.appendTransformation(new JDBCTransformationStep(sequence, "Rick-Rainer Ludwig",
		"CREATE SEQUENCE " + CONTACTS_SCHEMA + "." + "postal_address_type_id_seq INCREMENT BY 1 OWNED BY "
			+ CONTACTS_SCHEMA + "." + POSTAL_ADDRESS_TYPES_TABLE + ".id",
		"Sequence for postal address type ids."));
	sequence.appendTransformation(new JDBCTransformationStep(sequence, "Rick-Rainer Ludwig",
		"CREATE SEQUENCE " + CONTACTS_SCHEMA + "." + "bank_account_type_id_seq INCREMENT BY 1 OWNED BY "
			+ CONTACTS_SCHEMA + "." + BANK_ACCOUNT_TYPES_TABLE + ".id",
		"Sequence for bank account type ids."));
	sequence.appendTransformation(new JDBCTransformationStep(sequence, "Rick-Rainer Ludwig",
		"CREATE SEQUENCE " + CONTACTS_SCHEMA + "." + "other_contact_type_id_seq INCREMENT BY 1 OWNED BY "
			+ CONTACTS_SCHEMA + "." + OTHER_CONTACT_TYPES_TABLE + ".id",
		"Sequence for othe contact type ids."));
	return sequence;
    }

    @Override
    public void dropAll(Properties configuration) {
	try (Connection connection = PostgreSQLUtils.connect(configuration)) {
	    try (Statement statement = connection.createStatement()) {
		statement.execute("DROP SEQUENCE IF EXISTS " + CONTACTS_SCHEMA + "." + "contact_id_seq");
		statement.execute("DROP SEQUENCE IF EXISTS " + CONTACTS_SCHEMA + "." + "email_type_id_seq");
		statement.execute("DROP SEQUENCE IF EXISTS " + CONTACTS_SCHEMA + "." + "phone_number_type_id_seq");
		statement.execute("DROP SEQUENCE IF EXISTS " + CONTACTS_SCHEMA + "." + "postal_address_type_id_seq");
		statement.execute("DROP SEQUENCE IF EXISTS " + CONTACTS_SCHEMA + "." + "bank_account_type_id_seq");
		statement.execute("DROP SEQUENCE IF EXISTS " + CONTACTS_SCHEMA + "." + "other_contact_type_id_seq");
		statement.execute("DROP TABLE IF EXISTS " + CONTACTS_SCHEMA + "." + EMAIL_ADDRESSES_TABLE);
		statement.execute("DROP TABLE IF EXISTS " + CONTACTS_SCHEMA + "." + PHONE_NUMBERS_TABLE);
		statement.execute("DROP TABLE IF EXISTS " + CONTACTS_SCHEMA + "." + POSTAL_ADDRESSES_TABLE);
		statement.execute("DROP TABLE IF EXISTS " + CONTACTS_SCHEMA + "." + BANK_ACCOUNTS_TABLE);
		statement.execute("DROP TABLE IF EXISTS " + CONTACTS_SCHEMA + "." + OTHER_CONTACTS_TABLE);
		statement.execute("DROP TABLE IF EXISTS " + CONTACTS_SCHEMA + "." + EMAIL_ADDRESS_TYPES_TABLE);
		statement.execute("DROP TABLE IF EXISTS " + CONTACTS_SCHEMA + "." + PHONE_NUMBER_TYPES_TABLE);
		statement.execute("DROP TABLE IF EXISTS " + CONTACTS_SCHEMA + "." + POSTAL_ADDRESS_TYPES_TABLE);
		statement.execute("DROP TABLE IF EXISTS " + CONTACTS_SCHEMA + "." + BANK_ACCOUNT_TYPES_TABLE);
		statement.execute("DROP TABLE IF EXISTS " + CONTACTS_SCHEMA + "." + OTHER_CONTACT_TYPES_TABLE);
		statement.execute("DROP TABLE IF EXISTS " + CONTACTS_SCHEMA + "." + CONTACTS_TABLE);
		statement.execute("DROP SCHEMA IF EXISTS " + CONTACTS_SCHEMA);
	    }
	    connection.commit();
	} catch (NumberFormatException | SQLException e) {
	    throw new RuntimeException("Could not drop all changes for '" + getComponentName() + "'.", e);
	}
    }

}
