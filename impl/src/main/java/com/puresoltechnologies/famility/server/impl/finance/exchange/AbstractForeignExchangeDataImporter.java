package com.puresoltechnologies.famility.server.impl.finance.exchange;

import java.io.IOException;
import java.sql.SQLException;
import java.time.Duration;
import java.time.Instant;

import com.puresoltechnologies.famility.server.impl.db.DatabaseConnector;
import com.puresoltechnologies.famility.server.impl.db.ExtendedSQLQueryFactory;
import com.puresoltechnologies.famility.server.model.finance.QBankingInstitutions;
import com.puresoltechnologies.famility.server.model.finance.QForeignExchanges;
import com.querydsl.sql.SQLExpressions;
import com.querydsl.sql.SQLQuery;

public abstract class AbstractForeignExchangeDataImporter implements ForeignExchangeDataImporter {

    private final long bankingInstitutionId;

    protected AbstractForeignExchangeDataImporter() throws SQLException {
	bankingInstitutionId = readOrCreateDataSourceMetaData();
    }

    private long readOrCreateDataSourceMetaData() throws SQLException {
	try (ExtendedSQLQueryFactory queryFactory = DatabaseConnector.createQueryFactory()) {
	    Long id = queryFactory.select(QBankingInstitutions.bankingInstitutions.id)
		    .from(QBankingInstitutions.bankingInstitutions)
		    .where(QBankingInstitutions.bankingInstitutions.name.eq(getName())).fetchOne();
	    if (id == null) {
		SQLQuery<Long> query = queryFactory
			.select(SQLExpressions.nextval("finance.banking_institution_id_seq"));
		id = query.fetchOne();
		queryFactory.insert(QBankingInstitutions.bankingInstitutions) //
			.set(QBankingInstitutions.bankingInstitutions.id, id) //
			.set(QBankingInstitutions.bankingInstitutions.name, getName()) //
			.set(QBankingInstitutions.bankingInstitutions.url, getURL().toString()) //
			.execute();
		queryFactory.commit();
	    }
	    return id;
	}
    }

    private long readOrCreateTypeMetaData(String have, String want) throws SQLException {
	try (ExtendedSQLQueryFactory queryFactory = DatabaseConnector.createQueryFactory()) {
	    Long typeId = queryFactory.select(QForeignExchanges.foreignExchanges.id)
		    .from(QForeignExchanges.foreignExchanges)
		    .where(QForeignExchanges.foreignExchanges.bankingInstitutionId.eq(bankingInstitutionId)) //
		    .where(QForeignExchanges.foreignExchanges.have.eq(have)) //
		    .where(QForeignExchanges.foreignExchanges.want.eq(want)) //
		    .fetchOne();
	    if (typeId == null) {
		SQLQuery<Long> query = queryFactory.select(SQLExpressions.nextval("finance.foreign_exchange_id_seq"));
		typeId = query.fetchOne();
		queryFactory.insert(QForeignExchanges.foreignExchanges) //
			.set(QForeignExchanges.foreignExchanges.id, typeId) //
			.set(QForeignExchanges.foreignExchanges.name, getName()) //
			.set(QForeignExchanges.foreignExchanges.bankingInstitutionId, bankingInstitutionId) //
			.set(QForeignExchanges.foreignExchanges.have, have) //
			.set(QForeignExchanges.foreignExchanges.want, want) //
			.execute();
		queryFactory.commit();
	    }
	    return typeId;
	}
    }

    public final long getSourceId() {
	return bankingInstitutionId;
    }

    @Override
    public final void importChartData(Instant start, Instant end, String have, String want, Duration period)
	    throws IOException, SQLException {
	long typeId = readOrCreateTypeMetaData(have, want);
	importChartData(typeId, start, end, have, want, period);
    }

    protected abstract void importChartData(long typeId, Instant start, Instant end, String have, String want,
	    Duration period) throws IOException, SQLException;
}
