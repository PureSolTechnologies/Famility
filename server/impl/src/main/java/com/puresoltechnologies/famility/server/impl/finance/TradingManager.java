package com.puresoltechnologies.famility.server.impl.finance;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.puresoltechnologies.famility.server.impl.db.DatabaseConnector;
import com.puresoltechnologies.famility.server.impl.db.ExtendedSQLQueryFactory;
import com.puresoltechnologies.famility.server.model.finance.QCurrencies;
import com.querydsl.core.Tuple;

public class TradingManager {

    public List<CurrencyDefinition> getCurrencies() throws SQLException {
	List<CurrencyDefinition> currencies = new ArrayList<>();
	try (ExtendedSQLQueryFactory queryFactory = DatabaseConnector.createQueryFactory()) {
	    List<Tuple> results = queryFactory.select(QCurrencies.currencies.all()).from(QCurrencies.currencies)
		    .fetch();
	    for (Tuple tuple : results) {
		String code = tuple.get(QCurrencies.currencies.code);
		String symbol = tuple.get(QCurrencies.currencies.symbol);
		Integer number = tuple.get(QCurrencies.currencies.num);
		Short e = tuple.get(QCurrencies.currencies.e);
		String name = tuple.get(QCurrencies.currencies.name);
		currencies.add(new CurrencyDefinition(code, symbol, number, e, name));
	    }
	}
	return currencies;
    }

}
