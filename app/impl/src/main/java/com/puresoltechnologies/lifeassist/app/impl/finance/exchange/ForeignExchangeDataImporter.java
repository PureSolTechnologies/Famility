package com.puresoltechnologies.lifeassist.app.impl.finance.exchange;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.Duration;
import java.time.Instant;

/**
 * This interface is used to import foreign exchange data into database.
 *
 * @author Rick-Rainer Ludwig
 *
 */
public interface ForeignExchangeDataImporter {

    /**
     * This is the name of the source provider.
     *
     * @return
     */
    public String getName();

    /**
     * Returns the URL to the source provider.
     *
     * @return
     */
    public URL getURL();

    /**
     * Reads the list of currencies from the provider to import them into the
     * database.
     *
     * @throws IOException
     * @throws SQLException
     */
    public void importCurrencies() throws IOException, SQLException;

    /**
     * Imports the data from within this period.
     *
     * @param period
     * @throws IOException
     * @throws SQLException
     */
    public void importChartData(Instant start, Instant end, String have, String want, Duration period)
	    throws IOException, SQLException;

}
