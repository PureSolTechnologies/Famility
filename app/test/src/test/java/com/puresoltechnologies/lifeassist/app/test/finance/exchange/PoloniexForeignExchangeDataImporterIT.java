package com.puresoltechnologies.lifeassist.app.test.finance.exchange;

import java.io.IOException;
import java.sql.SQLException;
import java.time.Duration;
import java.time.Instant;

import org.junit.Test;

import com.puresoltechnologies.lifeassist.app.impl.finance.exchange.PoloniexForeignExchangeDataImporter;
import com.puresoltechnologies.lifeassist.app.test.AbstractLifeAssistantTest;

public class PoloniexForeignExchangeDataImporterIT extends AbstractLifeAssistantTest {

    @Test
    public void testImportCurrencies() throws IOException, SQLException {
	PoloniexForeignExchangeDataImporter importer = new PoloniexForeignExchangeDataImporter();
	importer.importCurrencies();
    }

    @Test
    public void testImportChartData() throws IOException, SQLException {
	PoloniexForeignExchangeDataImporter importer = new PoloniexForeignExchangeDataImporter();
	importer.importChartData(Instant.ofEpochMilli(1405699200l), Instant.ofEpochMilli(9999999999l), "BTC", "XMR",
		Duration.ofSeconds(14400l));
    }

}
