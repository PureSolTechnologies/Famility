package com.puresoltechnologies.lifeassist.app.impl.events;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.puresoltechnologies.lifeassist.app.api.events.Event;
import com.puresoltechnologies.lifeassist.app.api.events.EventLogger;
import com.puresoltechnologies.lifeassist.app.impl.db.DatabaseConnector;
import com.puresoltechnologies.lifeassist.app.impl.db.ExtendedSQLQueryFactory;
import com.puresoltechnologies.lifeassist.app.model.monitoring.QEventlog;

/**
 * This is the central event logger implementation.
 * 
 * @author Rick-Rainer Ludwig
 * 
 */
public class EventLoggerImpl implements EventLogger {

    private static final long serialVersionUID = -4162895953533068913L;

    private static Logger logger = LoggerFactory.getLogger(EventLoggerImpl.class);

    public static final String EVENTS_TABLE_NAME = "eventlog";
    private static final String LOG_EVENT_STATEMENT = "INSERT INTO " + EVENTS_TABLE_NAME
	    + " (time, component, event_id, server, type, severity, message, user_name, user_id, client, exception_message, exception_stacktrace)"
	    + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

    private final String server;

    {
	try {
	    server = InetAddress.getLocalHost().getHostName();
	} catch (UnknownHostException e) {
	    throw new RuntimeException(e);
	}
    }

    private final Connection connection;
    private final PreparedStatement preparedLogEventStatement;

    public EventLoggerImpl(Connection connection) {
	this.connection = connection;
	try {
	    preparedLogEventStatement = connection.prepareStatement(LOG_EVENT_STATEMENT);
	    logEvent(EventLoggerEvents.createStartEvent());
	} catch (SQLException e) {
	    throw new RuntimeException("Could not prepare statement for event logger.", e);
	}
    }

    @Override
    public void close() {
	try {
	    preparedLogEventStatement.close();
	} catch (SQLException e) {
	    logger.warn("Could not close prepared statement.", e);
	}
	logEvent(EventLoggerEvents.createStopEvent());
    }

    private String getStackTrace(Throwable throwable) {
	try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		PrintStream stream = new PrintStream(byteArrayOutputStream)) {
	    throwable.printStackTrace(stream);
	    return byteArrayOutputStream.toString();
	} catch (IOException e) {
	    logger.warn("Could not read stacktrace into PrintStream. This should never happen.", e);
	    return "<stacktrace conversion errored>";
	}
    }

    @Override
    public void logEvent(Event event) {
	writeToLogger(event);
	writeToDatabase(event);
    }

    protected void writeToDatabase(Event event) {
	try (ExtendedSQLQueryFactory queryFactory = DatabaseConnector.createQueryFactory()) {
	    Throwable throwable = event.getThrowable();
	    String exceptionMessage = null;
	    String exceptionStacktrace = null;
	    if (throwable != null) {
		exceptionMessage = throwable.getMessage();
		exceptionStacktrace = getStackTrace(throwable);
	    }
	    LocalDateTime timestamp = LocalDateTime.ofInstant(event.getTime(), ZoneId.systemDefault());
	    String email = event.getUserEmail() != null ? event.getUserEmail().getAddress() : null;

	    queryFactory.insert(QEventlog.eventlog) //
		    .set(QEventlog.eventlog.time, Timestamp.valueOf(timestamp)) //
		    .set(QEventlog.eventlog.component, event.getComponent()) //
		    .set(QEventlog.eventlog.eventId, event.getEventId()) //
		    .set(QEventlog.eventlog.server, server) //
		    .set(QEventlog.eventlog.type, event.getType().name()) //
		    .set(QEventlog.eventlog.severity, event.getSeverity().name()) //
		    .set(QEventlog.eventlog.message, event.getMessage()) //
		    .set(QEventlog.eventlog.userName, email) //
		    .set(QEventlog.eventlog.userId, event.getUserId()) //
		    .set(QEventlog.eventlog.client, event.getClientHostname()) //
		    .set(QEventlog.eventlog.exceptionMessage, exceptionMessage) //
		    .set(QEventlog.eventlog.exceptionStacktrace, exceptionStacktrace) //
		    .execute();
	    queryFactory.commit();
	} catch (SQLException e) {
	    throw new RuntimeException("Could not insert event into event log.", e);
	}
    }

    protected void writeToLogger(Event event) {
	String message = "=====| " + event.getSeverity() + " event: " + event.getMessage() + " (time=" + event.getTime()
		+ ";type=" + event.getType() + ") |=====";
	Throwable throwable = event.getThrowable();
	switch (event.getSeverity()) {
	case INFO:
	    logger.info(message);
	    break;
	case WARNING:
	    if (throwable == null) {
		logger.warn(message);
	    } else {
		logger.warn(message, throwable);
	    }
	    break;
	case ERROR:
	case FATAL:
	    logger.error(message, throwable);
	    break;
	}
    }
}
