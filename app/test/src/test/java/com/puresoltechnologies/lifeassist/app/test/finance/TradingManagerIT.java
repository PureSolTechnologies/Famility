package com.puresoltechnologies.lifeassist.app.test.finance;

import static org.junit.Assert.assertNotNull;

import java.sql.SQLException;
import java.util.List;

import org.junit.Test;

import com.puresoltechnologies.lifeassist.app.impl.finance.CurrencyDefinition;
import com.puresoltechnologies.lifeassist.app.impl.finance.TradingManager;
import com.puresoltechnologies.lifeassist.app.test.AbstractLifeAssistantTest;

public class TradingManagerIT extends AbstractLifeAssistantTest {

    private final TradingManager tradingManager = new TradingManager();

    @Test
    public void testGetCurrencies() throws SQLException {
	List<CurrencyDefinition> currencies = tradingManager.getCurrencies();
	assertNotNull(currencies);
	for (CurrencyDefinition currency : currencies) {
	    System.out.println(currency);
	}
    }

}
