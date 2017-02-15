package com.puresoltechnologies.lifeassist.app.impl.events;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Time;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.puresoltechnologies.lifeassist.app.api.events.Event;
import com.puresoltechnologies.lifeassist.app.api.events.EventLogger;

/**
 * This is the central event logger implementation.
 * 
 * @author Rick-Rainer Ludwig
 * 
 */
public class EventLoggerImpl implements EventLogger {

    private static final long serialVersionUID = -4162895953533068913L;

    private static Logger logger = LoggerFactory.getLogger(EventLoggerImpl.class);

    public static final String EVENTS_TABLE_NAME = "system_monitor.events";
    private static final String LOG_EVENT_STATEMENT = "UPSERT INTO " + EVENTS_TABLE_NAME
	    + " (time, component, event_id, server, type, severity, message, user, user_id, client, exception_message, exception_stacktrace)"
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
	writeToHBase(event);
    }

    protected void writeToHBase(Event event) {
	try {
	    Throwable throwable = event.getThrowable();
	    String exceptionMessage = null;
	    String exceptionStacktrace = null;
	    if (throwable != null) {
		exceptionMessage = throwable.getMessage();
		exceptionStacktrace = getStackTrace(throwable);
	    }
	    if (!connection.isClosed()) {
		String email = event.getUserEmail() != null ? event.getUserEmail().getAddress() : null;
		preparedLogEventStatement.setTime(1, new Time(event.getTime().getTime()));
		preparedLogEventStatement.setString(2, event.getComponent());
		preparedLogEventStatement.setLong(3, event.getEventId());
		preparedLogEventStatement.setString(4, server);
		preparedLogEventStatement.setString(5, event.getType().name());
		preparedLogEventStatement.setString(6, event.getSeverity().name());
		preparedLogEventStatement.setString(7, event.getMessage());
		preparedLogEventStatement.setString(8, email);
		preparedLogEventStatement.setLong(9, event.getUserId());
		preparedLogEventStatement.setString(10, event.getClientHostname());
		preparedLogEventStatement.setString(11, exceptionMessage);
		preparedLogEventStatement.setString(12, exceptionStacktrace);
		preparedLogEventStatement.execute();
		connection.commit();
	    } else {
		throw new IllegalStateException("Connection to HBase was closed already!");
	    }
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
