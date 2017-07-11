package com.puresoltechnologies.famility.server.test.finance.exchange;

import java.io.IOException;
import java.sql.SQLException;
import java.time.Duration;
import java.time.Instant;

import org.junit.Test;

import com.puresoltechnologies.famility.server.impl.finance.exchange.PoloniexForeignExchangeDataImporter;
import com.puresoltechnologies.famility.server.test.AbstractLifeAssistantTest;

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
