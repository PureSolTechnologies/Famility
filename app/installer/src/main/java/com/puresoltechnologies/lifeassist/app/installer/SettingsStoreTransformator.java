package com.puresoltechnologies.lifeassist.app.installer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.ZoneId;
import java.util.HashSet;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.Properties;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.puresoltechnologies.genesis.commons.ProvidedVersionRange;
import com.puresoltechnologies.genesis.commons.SequenceMetadata;
import com.puresoltechnologies.genesis.commons.TransformationException;
import com.puresoltechnologies.genesis.commons.TransformationMetadata;
import com.puresoltechnologies.genesis.commons.postgresql.PostgreSQLUtils;
import com.puresoltechnologies.genesis.transformation.jdbc.JDBCTransformationStep;
import com.puresoltechnologies.genesis.transformation.postgresql.PostgreSQLTransformationSequence;
import com.puresoltechnologies.genesis.transformation.spi.ComponentTransformator;
import com.puresoltechnologies.genesis.transformation.spi.TransformationSequence;
import com.puresoltechnologies.genesis.transformation.spi.TransformationStep;
import com.puresoltechnologies.lifeassist.common.db.ParameterType;
import com.puresoltechnologies.versioning.Version;

public class SettingsStoreTransformator implements ComponentTransformator {

    private static final Logger logger = LoggerFactory.getLogger(SettingsStoreTransformator.class);

    static final String PASSWORD_TABLE_NAME = "passwords";
    static final String SETTINGS_SCHEMA = "settings";
    static final String SYSTEM_TABLE = "system";
    static final String ACCOUNTS_TABLE = "accounts";
    static final String COUNTRIES_TABLE = "countries";
    static final String LANGUAGES_TABLE = "languages";

    @Override
    public String getComponentName() {
	return "Settings Store";
    }

    @Override
    public Set<String> getDependencies() {
	HashSet<String> dependencies = new HashSet<>();
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
		"CREATE SCHEMA " + SETTINGS_SCHEMA, "Creates the schema for settings."));

	sequence.appendTransformation(new JDBCTransformationStep(sequence, "Rick-Rainer Ludwig", //
		"CREATE TABLE " + SETTINGS_SCHEMA + "." + PASSWORD_TABLE_NAME//
			+ " (created timestamp, " //
			+ "last_modified timestamp, " //
			+ "email varchar not null," //
			+ "password varchar, " //
			+ "state varchar, "//
			+ "activation_key varchar, "//
			+ "CONSTRAINT " + PASSWORD_TABLE_NAME + "_PK PRIMARY KEY (email)" //
			+ ")",
		"Create passwords table."));
	sequence.appendTransformation(new JDBCTransformationStep(sequence, "Rick-Rainer Ludwig",
		"CREATE INDEX " //
			+ PASSWORD_TABLE_NAME + "_state_idx"//
			+ " ON " + SETTINGS_SCHEMA + "." + PASSWORD_TABLE_NAME //
			+ " (state)",
		"Creating index on state."));

	sequence.appendTransformation(new JDBCTransformationStep(sequence, "Rick-Rainer Ludwig", //
		"CREATE TABLE " + SETTINGS_SCHEMA + "." + SYSTEM_TABLE //
			+ " (" //
			+ "parameter varchar not null, " //
			+ "type varchar(8) not null, " //
			+ "value varchar not null, " //
			+ "default_value varchar not null, " //
			+ "unit varchar not null, " //
			+ "description varchar not null, " //
			+ "CONSTRAINT " + SYSTEM_TABLE + "_PK PRIMARY KEY (parameter))",
		"Create system settings table."));
	sequence.appendTransformation(new JDBCTransformationStep(sequence, "Rick-Rainer Ludwig", //
		"CREATE TABLE " + SETTINGS_SCHEMA + "." + ACCOUNTS_TABLE //
			+ " (" //
			+ "person_id bigint not null, " //
			+ "parameter varchar not null, " //
			+ "value varchar, " //
			+ "CONSTRAINT " + ACCOUNTS_TABLE + "_PK PRIMARY KEY (person_id, parameter), "//
			+ "CONSTRAINT " + ACCOUNTS_TABLE + "_" + SYSTEM_TABLE
			+ "_PK FOREIGN KEY (parameter) REFERENCES " + SETTINGS_SCHEMA + "." + SYSTEM_TABLE
			+ " (parameter) "//
			+ ")",
		"Create person settings table."));
	sequence.appendTransformation(new AddSystemParameter(sequence, "timezone", ParameterType.STRING,
		ZoneId.systemDefault().getId(), "UTC", "", "The system time zone.", "Rick-Rainer Ludwig",
		"Adds the system time zone and set it to current time zone."));
	sequence.appendTransformation(new JDBCTransformationStep(sequence, "Rick-Rainer Ludwig", //
		"CREATE TABLE " + SETTINGS_SCHEMA + "." + COUNTRIES_TABLE //
			+ " (" //
			+ "iso2 varchar(2) not null unique, " //
			+ "iso3 varchar(3) not null unique, " //
			+ "name varchar(128) not null unique, " //
			+ "CONSTRAINT " + COUNTRIES_TABLE + "_PK PRIMARY KEY (iso2)" //
			+ ")",
		"Create table for countries (https://de.wikipedia.org/wiki/ISO-3166-1-Kodierliste)."));
	sequence.appendTransformation(new TransformationStep() {

	    @Override
	    public void transform() throws TransformationException {
		Set<String> isos = new HashSet<>();
		Connection connection = sequence.getConnection();
		try (PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO " + SETTINGS_SCHEMA
			+ "." + COUNTRIES_TABLE + "(iso2, iso3, name) VALUES (?, ?, ?)")) {
		    for (Locale locale : Locale.getAvailableLocales()) {
			try {
			    String name = locale.getDisplayCountry(Locale.US);
			    if ((name == null) || (name.isEmpty())) {
				continue;
			    }
			    String iso2 = locale.getCountry();
			    if ((iso2 == null) || (iso2.isEmpty())) {
				continue;
			    }
			    if (isos.contains(iso2)) {
				continue;
			    }
			    isos.add(iso2);
			    logger.info("Adding '" + name + "'/'" + iso2 + "'.");
			    preparedStatement.setString(1, iso2);
			    preparedStatement.setString(2, locale.getISO3Country());
			    preparedStatement.setString(3, name);
			    preparedStatement.execute();
			} catch (MissingResourceException e) {
			    logger.warn("Skip '" + locale + "'.", e);
			}
		    }
		    connection.commit();
		} catch (SQLException e) {
		    throw new TransformationException("Could not add countries to countries table.", e);
		}
	    }

	    @Override
	    public TransformationMetadata getMetadata() {
		return new TransformationMetadata(sequence.getMetadata(), "Rick-Rainer Ludwig", "Add countries",
			"Adding initial countries based on java.util.Locale");
	    }
	});

	sequence.appendTransformation(new JDBCTransformationStep(sequence, "Rick-Rainer Ludwig", //
		"CREATE TABLE " + SETTINGS_SCHEMA + "." + LANGUAGES_TABLE //
			+ " (" //
			+ "iso2 varchar(2) not null unique, " //
			+ "iso3 varchar(3) not null unique, " //
			+ "name varchar(128) not null unique, " //
			+ "CONSTRAINT " + LANGUAGES_TABLE + "_PK PRIMARY KEY (iso2)" //
			+ ")",
		"Create table for languages."));
	sequence.appendTransformation(new TransformationStep() {

	    @Override
	    public void transform() throws TransformationException {
		Set<String> isos = new HashSet<>();
		Connection connection = sequence.getConnection();
		try (PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO " + SETTINGS_SCHEMA
			+ "." + LANGUAGES_TABLE + "(iso2, iso3, name) VALUES (?, ?, ?)")) {
		    for (Locale locale : Locale.getAvailableLocales()) {
			try {
			    String name = locale.getDisplayLanguage(Locale.US);
			    if ((name == null) || (name.isEmpty())) {
				continue;
			    }
			    String iso2 = locale.getLanguage();
			    if ((iso2 == null) || (iso2.isEmpty())) {
				continue;
			    }
			    if (isos.contains(iso2)) {
				continue;
			    }
			    isos.add(iso2);
			    logger.info("Adding '" + name + "'/'" + iso2 + "'.");
			    preparedStatement.setString(1, iso2);
			    preparedStatement.setString(2, locale.getISO3Language());
			    preparedStatement.setString(3, name);
			    preparedStatement.execute();
			} catch (MissingResourceException e) {
			    logger.warn("Skip '" + locale + "'.", e);
			}
		    }
		    connection.commit();
		} catch (SQLException e) {
		    throw new TransformationException("Could not add languages to languages table.", e);
		}
	    }

	    @Override
	    public TransformationMetadata getMetadata() {
		return new TransformationMetadata(sequence.getMetadata(), "Rick-Rainer Ludwig", "Add languages.",
			"Adding initial languages based on java.util.Locale");
	    }
	});

	return sequence;
    }

    @Override
    public void dropAll(Properties configuration) {
	try (Connection connection = PostgreSQLUtils.connect(configuration)) {
	    try (Statement statement = connection.createStatement()) {
		statement.execute("DROP TABLE IF EXISTS " + SETTINGS_SCHEMA + "." + LANGUAGES_TABLE);
		statement.execute("DROP TABLE IF EXISTS " + SETTINGS_SCHEMA + "." + COUNTRIES_TABLE);
		statement.execute("DROP TABLE IF EXISTS " + SETTINGS_SCHEMA + "." + ACCOUNTS_TABLE);
		statement.execute("DROP TABLE IF EXISTS " + SETTINGS_SCHEMA + "." + SYSTEM_TABLE);
		statement.execute("DROP TABLE IF EXISTS " + SETTINGS_SCHEMA + "." + PASSWORD_TABLE_NAME);
		statement.execute("DROP SCHEMA IF EXISTS " + SETTINGS_SCHEMA);
	    }
	    connection.commit();
	} catch (NumberFormatException | SQLException e) {
	    throw new RuntimeException("Could not drop all changes for '" + getComponentName() + "'.", e);
	}
    }

}
