package com.puresoltechnologies.lifeassist.app.impl.finance.exchange;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Duration;
import java.time.Instant;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.puresoltechnologies.lifeassist.app.impl.db.DatabaseConnector;
import com.puresoltechnologies.lifeassist.app.impl.db.ExtendedSQLQueryFactory;
import com.puresoltechnologies.lifeassist.app.model.finance.QCurrencies;
import com.puresoltechnologies.lifeassist.app.model.finance.QForeignExchangeChartData;
import com.querydsl.core.Tuple;

public class PoloniexForeignExchangeDataImporter extends AbstractForeignExchangeDataImporter {

    private static final String NAME = "Poloniex";
    private static final URL POLONIEX_URL;
    static {
	try {
	    POLONIEX_URL = new URL("https://poloniex.com");
	} catch (MalformedURLException e) {
	    throw new RuntimeException(e);
	}
    }

    public PoloniexForeignExchangeDataImporter() throws SQLException {
	super();
    }

    @Override
    public String getName() {
	return NAME;
    }

    @Override
    public URL getURL() {
	return POLONIEX_URL;
    }

    @Override
    public void importCurrencies() throws IOException, SQLException {
	try {
	    URI uri = new URIBuilder() //
		    .setScheme("http") //
		    .setHost("poloniex.com") //
		    .setPath("/public") //
		    .setParameter("command", "returnCurrencies") //
		    .build();
	    HttpGet get = new HttpGet(uri);
	    try (CloseableHttpClient httpclient = HttpClients.createDefault();
		    CloseableHttpResponse response = httpclient.execute(get);
		    JsonParser parser = new JsonFactory().createParser(response.getEntity().getContent())) {
		if (parser.nextToken() != null) {
		    if (parser.isExpectedStartObjectToken()) {
			processCurrencyList(parser);
		    }
		} else {
		    throw new JsonParseException(parser, "Empty stream found.", parser.getCurrentLocation());
		}
	    }
	} catch (URISyntaxException e) {
	    throw new IOException("Could not import data.", e);
	}
    }

    private void processCurrencyList(JsonParser parser) throws SQLException, IOException {
	try (ExtendedSQLQueryFactory queryFactory = DatabaseConnector.createQueryFactory()) {
	    while (parser.nextToken() != JsonToken.END_OBJECT) {
		/*
		 * "1CR":{"id":1,"name":"1CRedit","txFee":"0.01000000","minConf"
		 * :3,"depositAddress":null,"disabled":0,"delisted":1,"frozen":
		 * 0}
		 */
		String code = parser.getCurrentName();
		parser.nextToken();
		if (parser.isExpectedStartObjectToken()) {
		    processSingleCurrencyDate(parser, queryFactory, code);
		} else {
		    throw new JsonParseException(parser, "Array object element was expected.",
			    parser.getCurrentLocation());
		}
	    }
	    queryFactory.commit();
	}
    }

    /**
     * {"id":1,"name":"1CRedit","txFee":"0.01000000","minConf":3,"depositAddress":null,"disabled":0,"delisted":1,"frozen":0}
     *
     * @param parser
     * @param queryFactory
     * @param code
     * @throws IOException
     * @throws JsonParseException
     */
    private void processSingleCurrencyDate(JsonParser parser, ExtendedSQLQueryFactory queryFactory, String code)
	    throws JsonParseException, IOException {
	Long id = null;
	String name = null;
	Double txFee = null;
	Short minConf = null;
	String depositAddress = null;
	Boolean disabled = null;
	Boolean delisted = null;
	Boolean frozen = null;
	while (parser.nextToken() != JsonToken.END_OBJECT) {
	    String fieldName = parser.getCurrentName();
	    if ("id".equals(fieldName)) {
		parser.nextToken();
		id = parser.getValueAsLong();
	    } else if ("name".equals(fieldName)) {
		parser.nextToken();
		name = parser.getValueAsString();
	    } else if ("txFee".equals(fieldName)) {
		parser.nextToken();
		txFee = parser.getValueAsDouble();
	    } else if ("minConf".equals(fieldName)) {
		parser.nextToken();
		minConf = (short) parser.getValueAsInt();
	    } else if ("depositAddress".equals(fieldName)) {
		parser.nextToken();
		depositAddress = parser.getValueAsString();
	    } else if ("disabled".equals(fieldName)) {
		parser.nextToken();
		disabled = parser.getValueAsInt() == 0 ? false : true;
	    } else if ("delisted".equals(fieldName)) {
		parser.nextToken();
		delisted = parser.getValueAsInt() == 0 ? false : true;
	    } else if ("frozen".equals(fieldName)) {
		parser.nextToken();
		frozen = parser.getValueAsInt() == 0 ? false : true;
	    } else {
		throw new JsonParseException(parser, "Unexpected field '" + fieldName + "' found.",
			parser.getCurrentLocation());
	    }
	}
	Tuple currency = queryFactory.select(QCurrencies.currencies.all()).from(QCurrencies.currencies)
		.where(QCurrencies.currencies.code.eq(code)).fetchOne();
	if (currency == null) {
	    queryFactory.insert(QCurrencies.currencies) //
		    .set(QCurrencies.currencies.code, code) //
		    .set(QCurrencies.currencies.name, name) //
		    .set(QCurrencies.currencies.e, minConf) //
		    .execute();
	}
    }

    @Override
    public void importChartData(long typeId, Instant start, Instant end, String have, String want, Duration period)
	    throws IOException, SQLException {
	try {
	    URI uri = new URIBuilder() //
		    .setScheme("http") //
		    .setHost("poloniex.com") //
		    .setPath("/public") //
		    .setParameter("command", "returnChartData") //
		    .setParameter("currencyPair", have + "_" + want) //
		    .setParameter("start", String.valueOf(start.toEpochMilli())) //
		    .setParameter("end", String.valueOf(end.toEpochMilli())) //
		    .setParameter("period", String.valueOf(period.getSeconds())) //
		    .build();
	    HttpGet get = new HttpGet(uri);
	    try (CloseableHttpClient httpclient = HttpClients.createDefault();
		    CloseableHttpResponse response = httpclient.execute(get);
		    JsonParser parser = new JsonFactory().createParser(response.getEntity().getContent())) {
		if (parser.nextToken() != null) {
		    if (parser.isExpectedStartArrayToken()) {
			processChartDataList(parser, typeId);
		    }
		} else {
		    throw new JsonParseException(parser, "Empty stream found.", parser.getCurrentLocation());
		}
	    }
	} catch (URISyntaxException e) {
	    throw new IOException("Could not import data.", e);
	}
    }

    private void processChartDataList(JsonParser parser, long typeId)
	    throws JsonParseException, IOException, SQLException {
	try (ExtendedSQLQueryFactory queryFactory = DatabaseConnector.createQueryFactory()) {
	    while (parser.nextToken() != JsonToken.END_ARRAY) {
		if (parser.isExpectedStartObjectToken()) {
		    processSingleChartDataDate(parser, queryFactory, typeId);
		} else {
		    throw new JsonParseException(parser, "Array object element was expected.",
			    parser.getCurrentLocation());
		}
	    }
	    queryFactory.commit();
	}
    }

    /**
     * {"date":1405699200,"high":0.0045388,"low":0.00403001,"open":0.00404545,"close":0.00427592,"volume":44.11655644,
     * "quoteVolume":10259.29079097,"weightedAverage":0.00430015}
     *
     * @param parser
     * @param queryFactory
     * @param foreignExchangeId
     * @throws JsonParseException
     * @throws IOException
     */
    private void processSingleChartDataDate(JsonParser parser, ExtendedSQLQueryFactory queryFactory,
	    long foreignExchangeId) throws JsonParseException, IOException {
	Instant date = null;
	Double high = null;
	Double low = null;
	Double open = null;
	Double close = null;
	Double volume = null;
	Double quoteVolume = null;
	Double weightedAverage = null;
	while (parser.nextToken() != JsonToken.END_OBJECT) {
	    String fieldName = parser.getCurrentName();
	    if ("date".equals(fieldName)) {
		parser.nextToken();
		date = Instant.ofEpochSecond(parser.getValueAsLong());
	    } else if ("high".equals(fieldName)) {
		parser.nextToken();
		high = parser.getValueAsDouble();
	    } else if ("low".equals(fieldName)) {
		parser.nextToken();
		low = parser.getValueAsDouble();
	    } else if ("open".equals(fieldName)) {
		parser.nextToken();
		open = parser.getValueAsDouble();
	    } else if ("close".equals(fieldName)) {
		parser.nextToken();
		close = parser.getValueAsDouble();
	    } else if ("volume".equals(fieldName)) {
		parser.nextToken();
		volume = parser.getValueAsDouble();
	    } else if ("quoteVolume".equals(fieldName)) {
		parser.nextToken();
		quoteVolume = parser.getValueAsDouble();
	    } else if ("weightedAverage".equals(fieldName)) {
		parser.nextToken();
		weightedAverage = parser.getValueAsDouble();
	    } else {
		throw new JsonParseException(parser, "Unexpected field '" + fieldName + "' found.",
			parser.getCurrentLocation());
	    }
	}
	queryFactory.insert(QForeignExchangeChartData.foreignExchangeChartData) //
		.set(QForeignExchangeChartData.foreignExchangeChartData.foreignExchangeId, foreignExchangeId) //
		.set(QForeignExchangeChartData.foreignExchangeChartData.timestamp, Timestamp.from(date)) //
		.set(QForeignExchangeChartData.foreignExchangeChartData.high, high) //
		.set(QForeignExchangeChartData.foreignExchangeChartData.low, low) //
		.set(QForeignExchangeChartData.foreignExchangeChartData.open, open) //
		.set(QForeignExchangeChartData.foreignExchangeChartData.close, close) //
		.set(QForeignExchangeChartData.foreignExchangeChartData.volume, volume) //
		.set(QForeignExchangeChartData.foreignExchangeChartData.quotevolume, quoteVolume) //
		.set(QForeignExchangeChartData.foreignExchangeChartData.weightedaverage, weightedAverage) //
		.execute();
    }

}
