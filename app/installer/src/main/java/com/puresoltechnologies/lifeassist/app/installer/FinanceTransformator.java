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

public class FinanceTransformator implements ComponentTransformator {

    static final String FINANCE_SCHEMA = "finance";
    static final String CURRENCY_TABLE_NAME = "currencies";
    static final String BANKING_INSTITUTIONS_TABLE_NAME = "banking_institutions";
    static final String FOREIGN_EXCHANGES_TABLE_NAME = "foreign_exchanges";
    static final String FOREIGN_EXCHANGE_TIME_DATA_TABLE_NAME = "foreign_exchange_time_data";
    static final String FOREIGN_EXCHANGE_CHART_DATA_TABLE_NAME = "foreign_exchange_chart_data";
    static final String BANK_ACCOUNTS_TABLE_NAME = "bank_accounts";
    static final String TRANSACTIONS_TABLE_NAME = "transactions";
    static final String ACCOUNTS_TABLE_NAME = "accounts";
    static final String ACCOUNT_STATEMENTS_TABLE_NAME = "account_statements";
    static final String COST_CENTERS_TABLE_NAME = "cost_centers";
    static final String COST_TYPES_TABLE_NAME = "cost_types";

    @Override
    public String getComponentName() {
	return "Finanze";
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
		"CREATE SCHEMA " + FINANCE_SCHEMA, "Creates the schema for finance data."));

	sequence.appendTransformation(new JDBCTransformationStep(sequence, "Rick-Rainer Ludwig", //
		"CREATE TABLE " + FINANCE_SCHEMA + "." + CURRENCY_TABLE_NAME //
			+ " (code varchar(6) not null, " //
			+ "symbol varchar(3), " //
			+ "num integer, " //
			+ "e smallint not null, " //
			+ "name varchar(128) not null, " //
			+ "disabled boolean not null default false, " //
			+ "delisted boolean not null default false, " //
			+ "frozen boolean not null default false, " //
			+ "CONSTRAINT " + CURRENCY_TABLE_NAME + "_PK PRIMARY KEY (code)" //
			+ ")",
		"Create currency table (see http://en.wikipedia.org/wiki/ISO_4217 for content)."));

	sequence.appendTransformation(new JDBCTransformationStep(sequence, "Rick-Rainer Ludwig", //
		"CREATE TABLE " + FINANCE_SCHEMA + "." + BANKING_INSTITUTIONS_TABLE_NAME //
			+ " (id bigint not null, " //
			+ "name varchar(64) not null unique, " //
			+ "url varchar, " //
			+ "bic varchar(11) unique, " //
			+ "blz varchar(8) unique, " //
			+ "CONSTRAINT " + BANKING_INSTITUTIONS_TABLE_NAME + "_PK PRIMARY KEY (id)" //
			+ ")",
		"Create banking institutions table."));

	sequence.appendTransformation(new JDBCTransformationStep(sequence, "Rick-Rainer Ludwig", //
		"CREATE SEQUENCE " + FINANCE_SCHEMA + ".banking_institution_id_seq OWNED BY " + FINANCE_SCHEMA + "."
			+ BANKING_INSTITUTIONS_TABLE_NAME + ".id"//
		, "Create exchange rate chart data table."));

	sequence.appendTransformation(new JDBCTransformationStep(sequence, "Rick-Rainer Ludwig", //
		"CREATE INDEX " + BANKING_INSTITUTIONS_TABLE_NAME + "_NAME_IDX ON " + FINANCE_SCHEMA + "."
			+ BANKING_INSTITUTIONS_TABLE_NAME + " (name)"//
		, "Create index on name for banking institutations table."));

	sequence.appendTransformation(new JDBCTransformationStep(sequence, "Rick-Rainer Ludwig", //
		"CREATE INDEX " + BANKING_INSTITUTIONS_TABLE_NAME + "_BIC_IDX ON " + FINANCE_SCHEMA + "."
			+ BANKING_INSTITUTIONS_TABLE_NAME + " (bic)"//
		, "Create index on bic for banking institutations table."));

	sequence.appendTransformation(new JDBCTransformationStep(sequence, "Rick-Rainer Ludwig", //
		"CREATE TABLE " + FINANCE_SCHEMA + "." + FOREIGN_EXCHANGES_TABLE_NAME //
			+ " (id bigserial not null, " //
			+ "banking_institution_id bigint not null, " //
			+ "name varchar(64) not null, " //
			+ "have varchar(6) not null, " //
			+ "want varchar(6) not null, " //
			+ "CONSTRAINT " + FOREIGN_EXCHANGES_TABLE_NAME + "_PK PRIMARY KEY (id), " //
			+ "CONSTRAINT from_to_curency_unique_constraint UNIQUE (have, want), " //
			+ "CONSTRAINT " + FOREIGN_EXCHANGES_TABLE_NAME + "_" + BANKING_INSTITUTIONS_TABLE_NAME
			+ "_FK FOREIGN KEY (banking_institution_id) REFERENCES " + FINANCE_SCHEMA + "."
			+ BANKING_INSTITUTIONS_TABLE_NAME + " (id), " //
			+ "CONSTRAINT " + FOREIGN_EXCHANGES_TABLE_NAME + "_" + CURRENCY_TABLE_NAME
			+ "_1_FK FOREIGN KEY (have) REFERENCES " + FINANCE_SCHEMA + "." + CURRENCY_TABLE_NAME
			+ " (code), " //
			+ "CONSTRAINT " + FOREIGN_EXCHANGES_TABLE_NAME + "_" + CURRENCY_TABLE_NAME
			+ "_2_FK FOREIGN KEY (want) REFERENCES " + FINANCE_SCHEMA + "." + CURRENCY_TABLE_NAME
			+ " (code)" //
			+ ")",
		"Create exchange rate type table."));

	sequence.appendTransformation(new JDBCTransformationStep(sequence, "Rick-Rainer Ludwig", //
		"CREATE SEQUENCE " + FINANCE_SCHEMA + ".foreign_exchange_id_seq OWNED BY " + FINANCE_SCHEMA + "."
			+ FOREIGN_EXCHANGES_TABLE_NAME + ".id"//
		, "Create exchange rate chart data table."));

	sequence.appendTransformation(new JDBCTransformationStep(sequence, "Rick-Rainer Ludwig", //
		"CREATE INDEX " + FOREIGN_EXCHANGES_TABLE_NAME + "_NAME_HAVE_WANT_IDX ON " + FINANCE_SCHEMA + "."
			+ FOREIGN_EXCHANGES_TABLE_NAME + " (name, have, want)"//
		, "Create exchange rate chart data table."));

	sequence.appendTransformation(new JDBCTransformationStep(sequence, "Rick-Rainer Ludwig", //
		"CREATE TABLE " + FINANCE_SCHEMA + "." + FOREIGN_EXCHANGE_TIME_DATA_TABLE_NAME //
			+ " (id bigserial not null, " //
			+ "foreign_exchange_id bigint not null, " //
			+ "timestamp timestamp with time zone not null, " //
			+ "have_amount money not null, " //
			+ "want_amount money not null, " //
			+ "CONSTRAINT " + FOREIGN_EXCHANGE_TIME_DATA_TABLE_NAME + "_PK PRIMARY KEY (id), " //
			+ "CONSTRAINT " + FOREIGN_EXCHANGE_TIME_DATA_TABLE_NAME + "_" + FOREIGN_EXCHANGES_TABLE_NAME
			+ "_FK FOREIGN KEY (foreign_exchange_id) REFERENCES " + FINANCE_SCHEMA + "."
			+ FOREIGN_EXCHANGES_TABLE_NAME + " (id)" //
			+ ")",
		"Create exchange rate type table."));

	sequence.appendTransformation(new JDBCTransformationStep(sequence, "Rick-Rainer Ludwig", //
		"CREATE INDEX " + FOREIGN_EXCHANGE_TIME_DATA_TABLE_NAME + "_TIMESTAMP_IDX ON " + FINANCE_SCHEMA + "."
			+ FOREIGN_EXCHANGE_TIME_DATA_TABLE_NAME + " (timestamp)"//
		, "Create exchange rate chart data table."));

	sequence.appendTransformation(new JDBCTransformationStep(sequence, "Rick-Rainer Ludwig", //
		"CREATE TABLE " + FINANCE_SCHEMA + "." + FOREIGN_EXCHANGE_CHART_DATA_TABLE_NAME //
			+ " (id bigserial not null, " //
			+ "foreign_exchange_id bigint not null, " //
			+ "timestamp timestamp with time zone not null, " //
			+ "high double precision not null, " //
			+ "low double precision not null, " //
			+ "open double precision not null, " //
			+ "close double precision not null, " //
			+ "volume double precision not null, " //
			+ "quoteVolume double precision not null, " //
			+ "weightedAverage double precision not null, " //
			+ "CONSTRAINT " + FOREIGN_EXCHANGE_CHART_DATA_TABLE_NAME + "_PK PRIMARY KEY (id), " //
			+ "CONSTRAINT " + FOREIGN_EXCHANGE_CHART_DATA_TABLE_NAME + "_" + FOREIGN_EXCHANGES_TABLE_NAME
			+ "_FK FOREIGN KEY (foreign_exchange_id) REFERENCES " + FINANCE_SCHEMA + "."
			+ FOREIGN_EXCHANGES_TABLE_NAME + " (id)" //
			+ ")",
		"Create exchange rate chart data table."));

	sequence.appendTransformation(new JDBCTransformationStep(sequence, "Rick-Rainer Ludwig", //
		"CREATE INDEX " + FOREIGN_EXCHANGE_CHART_DATA_TABLE_NAME + "_TIMESTAMP_IDX ON " + FINANCE_SCHEMA + "."
			+ FOREIGN_EXCHANGE_CHART_DATA_TABLE_NAME + " (timestamp)"//
		, "Create index for timestamp for exchange rate chart data table."));

	sequence.appendTransformation(new JDBCTransformationStep(sequence, "Rick-Rainer Ludwig", //
		"CREATE TABLE " + FINANCE_SCHEMA + "." + ACCOUNTS_TABLE_NAME //
			+ " (id bigint not null, " //
			+ "currency varchar(6) not null, " //
			+ "CONSTRAINT " + ACCOUNTS_TABLE_NAME + "_PK PRIMARY KEY (id), " //
			+ "CONSTRAINT " + ACCOUNTS_TABLE_NAME + "_ID_CURRENCY_UNIQUE UNIQUE (id, currency)" //
			+ ")",
		"Create internal accounts table."));

	sequence.appendTransformation(new JDBCTransformationStep(sequence, "Rick-Rainer Ludwig", //
		"CREATE TABLE " + FINANCE_SCHEMA + "." + BANK_ACCOUNTS_TABLE_NAME //
			+ " (account_id bigint not null, " //
			+ "banking_institution_id bigint, " //
			+ "iban varchar(14) unique, " //
			+ "currency varchar(6) not null, " //
			+ "CONSTRAINT " + BANK_ACCOUNTS_TABLE_NAME + "_PK PRIMARY KEY (account_id), " //
			+ "CONSTRAINT " + BANK_ACCOUNTS_TABLE_NAME + "_" + BANKING_INSTITUTIONS_TABLE_NAME
			+ "_FK FOREIGN KEY (banking_institution_id) REFERENCES " + FINANCE_SCHEMA + "."
			+ BANKING_INSTITUTIONS_TABLE_NAME + " (id), " //
			+ "CONSTRAINT " + BANK_ACCOUNTS_TABLE_NAME + "_" + ACCOUNTS_TABLE_NAME
			+ "_FK FOREIGN KEY (account_id, currency) REFERENCES " + FINANCE_SCHEMA + "."
			+ ACCOUNTS_TABLE_NAME + " (id, currency)" //
			+ ")",
		"Create bank accounts table."));

	sequence.appendTransformation(new JDBCTransformationStep(sequence, "Rick-Rainer Ludwig", //
		"CREATE SEQUENCE " + FINANCE_SCHEMA + ".account_id_seq OWNED BY " + FINANCE_SCHEMA + "."
			+ ACCOUNTS_TABLE_NAME + ".id"//
		, "Create sequence for account id."));

	sequence.appendTransformation(new JDBCTransformationStep(sequence, "Rick-Rainer Ludwig", //
		"CREATE INDEX " + BANK_ACCOUNTS_TABLE_NAME + "_IBAN_IDX ON " + FINANCE_SCHEMA + "."
			+ BANK_ACCOUNTS_TABLE_NAME + " (iban)"//
		, "Create index on iban for accounts table."));

	sequence.appendTransformation(new JDBCTransformationStep(sequence, "Rick-Rainer Ludwig", //
		"CREATE TABLE " + FINANCE_SCHEMA + "." + TRANSACTIONS_TABLE_NAME //
			+ " (id bigint not null, " //
			+ "CONSTRAINT " + TRANSACTIONS_TABLE_NAME + "_PK PRIMARY KEY (id)" //
			+ ")",
		"Create internal accounts table."));

	sequence.appendTransformation(new JDBCTransformationStep(sequence, "Rick-Rainer Ludwig", //
		"CREATE SEQUENCE " + FINANCE_SCHEMA + ".transaction_id_seq OWNED BY " + FINANCE_SCHEMA + "."
			+ TRANSACTIONS_TABLE_NAME + ".id"//
		, "Create transaction id sequence."));

	sequence.appendTransformation(new JDBCTransformationStep(sequence, "Rick-Rainer Ludwig", //
		"CREATE TABLE " + FINANCE_SCHEMA + "." + ACCOUNT_STATEMENTS_TABLE_NAME //
			+ " (id bigserial not null, " //
			+ "account_id bigint not null, " //
			+ "transaction_id bigint not null, " //
			+ "date_of_bookkeeping date, " //
			+ "value_date date, " //
			+ "foreign_account varchar, " //
			+ "posting_text varchar, " //
			+ "reason_for_transfer varchar, " //
			+ "recipient_or_payer varchar, " //
			+ "iban_or_account varchar(14), " //
			+ "bic_or_blz varchar(11), " //
			+ "amount varchar not null, " //
			+ "currency varchar(6) not null, " //
			+ "info varchar, " //
			+ "CONSTRAINT " //
			+ ACCOUNT_STATEMENTS_TABLE_NAME + "_PK PRIMARY KEY (id), " //
			+ "CONSTRAINT " + ACCOUNT_STATEMENTS_TABLE_NAME + "_" + ACCOUNTS_TABLE_NAME
			+ "_FK FOREIGN KEY (account_id) REFERENCES " + FINANCE_SCHEMA + "." //
			+ ACCOUNTS_TABLE_NAME + " (id), " //
			+ "CONSTRAINT " + ACCOUNT_STATEMENTS_TABLE_NAME + "_" + CURRENCY_TABLE_NAME
			+ "_FK FOREIGN KEY (currency) REFERENCES " + FINANCE_SCHEMA + "." + CURRENCY_TABLE_NAME
			+ " (code), " //
			+ "CONSTRAINT " + ACCOUNT_STATEMENTS_TABLE_NAME + "_" + TRANSACTIONS_TABLE_NAME
			+ "_FK FOREIGN KEY (transaction_id) REFERENCES " + FINANCE_SCHEMA + "." //
			+ TRANSACTIONS_TABLE_NAME + " (id)" //
			+ ")",
		"Create bank account statements table."));

	sequence.appendTransformation(new JDBCTransformationStep(sequence, "Rick-Rainer Ludwig", //
		"CREATE TABLE " + FINANCE_SCHEMA + "." + COST_CENTERS_TABLE_NAME //
			+ " (id bigserial not null, " //
			+ "name varchar(64) not null, " //
			+ "description varchar, " //
			+ "is_primary boolean, " //
			+ "CONSTRAINT " //
			+ COST_CENTERS_TABLE_NAME + "_PK PRIMARY KEY (id)" //
			+ ")",
		"Create cost centers table."));

	sequence.appendTransformation(new JDBCTransformationStep(sequence, "Rick-Rainer Ludwig", //
		"CREATE SEQUENCE " + FINANCE_SCHEMA + ".cost_center_id_seq OWNED BY " + FINANCE_SCHEMA + "."
			+ COST_CENTERS_TABLE_NAME + ".id"//
		, "Create cost center id sequence."));

	sequence.appendTransformation(new JDBCTransformationStep(sequence, "Rick-Rainer Ludwig", //
		"CREATE TABLE " + FINANCE_SCHEMA + "." + COST_TYPES_TABLE_NAME //
			+ " (id bigserial not null, " //
			+ "name varchar(64) not null, " //
			+ "description varchar, " //
			+ "CONSTRAINT " //
			+ COST_TYPES_TABLE_NAME + "_PK PRIMARY KEY (id)" //
			+ ")",
		"Create cost types table."));

	sequence.appendTransformation(new JDBCTransformationStep(sequence, "Rick-Rainer Ludwig", //
		"CREATE SEQUENCE " + FINANCE_SCHEMA + ".cost_type_id_seq OWNED BY " + FINANCE_SCHEMA + "."
			+ COST_TYPES_TABLE_NAME + ".id"//
		, "Create cost type id sequence."));

	return sequence;
    }

    @Override
    public void dropAll(Properties configuration) {
	try (Connection connection = PostgreSQLUtils.connect(configuration)) {
	    try (Statement statement = connection.createStatement()) {
		statement.execute("DROP TABLE IF EXISTS " + FINANCE_SCHEMA + "." + ACCOUNT_STATEMENTS_TABLE_NAME);
		statement.execute("DROP TABLE IF EXISTS " + FINANCE_SCHEMA + "." + TRANSACTIONS_TABLE_NAME);
		statement.execute("DROP TABLE IF EXISTS " + FINANCE_SCHEMA + "." + BANK_ACCOUNTS_TABLE_NAME);
		statement.execute("DROP TABLE IF EXISTS " + FINANCE_SCHEMA + "." + ACCOUNTS_TABLE_NAME);
		statement.execute(
			"DROP TABLE IF EXISTS " + FINANCE_SCHEMA + "." + FOREIGN_EXCHANGE_CHART_DATA_TABLE_NAME);
		statement.execute(
			"DROP TABLE IF EXISTS " + FINANCE_SCHEMA + "." + FOREIGN_EXCHANGE_TIME_DATA_TABLE_NAME);
		statement.execute("DROP TABLE IF EXISTS " + FINANCE_SCHEMA + "." + FOREIGN_EXCHANGES_TABLE_NAME);
		statement.execute("DROP TABLE IF EXISTS " + FINANCE_SCHEMA + "." + BANKING_INSTITUTIONS_TABLE_NAME);
		statement.execute("DROP TABLE IF EXISTS " + FINANCE_SCHEMA + "." + CURRENCY_TABLE_NAME);
		statement.execute("DROP SCHEMA IF EXISTS " + FINANCE_SCHEMA);
	    }
	    connection.commit();
	} catch (NumberFormatException | SQLException e) {
	    throw new RuntimeException("Could not drop all changes for '" + getComponentName() + "'.", e);
	}
    }

}
